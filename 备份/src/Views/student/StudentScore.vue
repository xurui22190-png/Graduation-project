<template>
  <div class="student-dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="(item, index) in statItems" :key="index">
        <div class="stat-card" :class="item.type">
          <div class="stat-icon"><el-icon><component :is="item.icon" /></el-icon></div>
          <div class="stat-info">
            <div class="label">{{ item.label }}</div>
            <div class="number">{{ item.value }}{{ item.unit }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="10">
        <el-card shadow="hover">
          <template #header><div class="card-header"><b><el-icon><DataAnalysis /></el-icon> 个人能力 6 维画像图</b></div></template>
          <div ref="abilityRadarRef" style="height: 350px"></div>
        </el-card>
      </el-col>

      <el-col :span="14">
        <el-card shadow="hover" class="ai-card">
          <template #header>
            <div class="ai-header-wrapper">
              <span class="ai-title"><el-icon><MagicStick /></el-icon> AI 本地导师 (Qwen-1.8B)</span>
              <div class="header-ops">
                <el-button type="warning" size="small" icon="Connection" @click="reAnalyze" :loading="aiLoading">
                  {{ aiAdvice ? '重新诊断' : '激活 AI 引擎' }}
                </el-button>
                <el-radio-group v-model="studentIntent" size="small" style="margin-left: 10px">
                  <el-radio-button label="job">就业</el-radio-button>
                  <el-radio-button label="grad">考研</el-radio-button>
                  <el-radio-button label="civil">考公</el-radio-button>
                </el-radio-group>
              </div>
            </div>
          </template>
          <div class="ai-content-body" v-loading="aiLoading" element-loading-text="正在读取学情数据...">
            <div v-if="aiAdvice" class="analysis-active">
              <div class="intent-match">
                意向匹配度：<span class="percentage">{{ matchRate }}%</span>
                <el-progress :percentage="matchRate" :stroke-width="12" :color="matchColor" />
              </div>
              <div class="advice-bubble"><p>{{ aiAdvice }}</p></div>
            </div>
            <div v-else class="empty-ai">
              <el-icon size="50" color="#C0C4CC"><Cpu /></el-icon>
              <p>本地 AI 诊断引擎待命 (Python 8000端口)</p>
              <span style="font-size:12px; color:#999">点击“激活 AI 引擎”进行大模型深度学情诊断</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top: 20px">
      <template #header>
        <div class="table-header">
          <span><el-icon><List /></el-icon> 学期成绩明细列表</span>
          <el-button type="primary" size="small" link icon="Refresh" @click="loadTableData">刷新数据</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="yall" label="学年学期" width="180" />
        <el-table-column prop="crname" label="课程名称" />
        <el-table-column prop="scscore" label="总分" width="100">
          <template #default="scope">
            <b :style="{ color: scope.row.scscore < 60 ? '#F56C6C' : '#409EFF' }">{{ scope.row.scscore }}</b>
          </template>
        </el-table-column>
        <el-table-column prop="tname" label="任课教师" width="120" />
        <el-table-column label="诊断操作" width="200" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" link @click="showComposition(scope.row)">组成</el-button>
            <el-button type="success" size="small" link @click="showDiagnosis(scope.row)">雷达</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="`[${currentCourse?.crname}] 知识点弱项分析`" width="500px">
      <div v-loading="diagLoading"><div ref="courseRadarRef" style="height: 380px"></div></div>
    </el-dialog>

    <el-dialog v-model="compositionVisible" :title="`[${currentCourse?.crname}] 成绩核算明细`" width="550px">
      <div class="comp-container" v-loading="compLoading">
        <div class="score-row"><span>平时成绩 (30%)：</span><b>{{ compData.regular }} 分</b></div>
        <div class="score-row"><span>课堂测试 (20%)：</span><b>{{ compData.test }} 分</b></div>
        <div class="score-row final"><span>期末考试 (50%)：</span><b>{{ compData.exam }} 分</b></div>
        <el-divider>细粒度知识掌握度</el-divider>
        <el-table :data="compData.details" size="small" border stripe>
          <el-table-column prop="point" label="知识点" />
          <el-table-column label="掌握进度"><template #default="s"><el-progress :percentage="s.row.rate" /></template></el-table-column>
          <el-table-column prop="score" label="得分" width="70" />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, computed } from 'vue'
import * as echarts from 'echarts'
import { MagicStick, List, Star, Connection, Refresh, TrendCharts, CircleCheck, Cpu, Memo, DataAnalysis, DataLine } from '@element-plus/icons-vue'
import request from '@/utils/Axios'
import { ElMessage } from 'element-plus'

// 1. 状态定义
const tableData = ref([])
const loading = ref(false)
const aiLoading = ref(false)
const diagLoading = ref(false)
const compLoading = ref(false)
const dialogVisible = ref(false)
const compositionVisible = ref(false)
const currentCourse = ref(null)

// 2. 数据定义
const studentIntent = ref('job')
const aiAdvice = ref('')
const matchRate = ref(0)
const dimensionMap = reactive({ '核心力': 75, '逻辑力': 75, '实践力': 75, '设计力': 75, '前沿力': 75, '综合力': 75 })
const stats = reactive({ avgScore: 0, passRate: 0 })
const compData = reactive({ regular: 0, test: 0, exam: 0, details: [] })

const abilityRadarRef = ref(null)
const courseRadarRef = ref(null)
let abilityChart = null

// 3. 配置计算项
const statItems = computed(() => [
  { label: '平均分数', value: stats.avgScore, unit: '', icon: 'TrendCharts', type: 'blue' },
  { label: '及格率', value: stats.passRate, unit: '%', icon: 'CircleCheck', type: 'green' },
  { label: '已修课程', value: tableData.value.length, unit: '门', icon: 'DataLine', type: 'orange' },
  { label: '学业评价', value: stats.avgScore > 85 ? '优秀' : '良好', unit: '', icon: 'Star', type: 'purple' }
])
const matchColor = computed(() => matchRate.value < 60 ? '#f56c6c' : (matchRate.value < 85 ? '#e6a23c' : '#67c23a'))

// 4. 数据解析逻辑
const deepFindArray = (obj) => {
  if (!obj) return []
  if (Array.isArray(obj)) return obj
  const keys = ['data', 'records', 'list']
  for (let key of keys) {
    if (obj[key]) {
      const res = deepFindArray(obj[key])
      if (res.length > 0) return res
    }
  }
  return []
}

// 5. 业务方法
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await request.get('/student/score/getlist', { params: { pageIndex: 1, pageSize: 100 } })
    let list = deepFindArray(res)

    // 如果无数据则显示 Mock
    if (list.length === 0) {
      list = [
        { yall: '2025-2026-1', crname: 'Java程序设计', scscore: 92, tname: '教研组', sid: 2, sccourseid: 1 },
        { yall: '2025-2026-1', crname: '数据结构', scscore: 85, tname: '教研组', sid: 2, sccourseid: 5 }
      ]
    }
    tableData.value = list
    analyzeAcademic(list)
  } catch (e) {
    console.error("Fetch Error:", e)
    ElMessage.error("后端服务连接失败")
  } finally {
    loading.value = false
  }
}

const analyzeAcademic = (list) => {
  const nums = list.map(i => parseFloat(i.scscore) || 0)
  if (nums.length > 0) {
    stats.avgScore = (nums.reduce((a, b) => a + b, 0) / nums.length).toFixed(1)
    stats.passRate = Math.round((nums.filter(s => s >= 60).length / nums.length) * 100)
  }

  const config = {
    '核心力': ['Java', '数据结构', '原理'],
    '逻辑力': ['数学', '线代', '物理'],
    '实践力': ['项目', '实训', '实习'],
    '设计力': ['数据库', '软件工程'],
    '前沿力': ['人工智能', 'Python'],
    '综合力': ['思想', '英语', '体']
  }
  Object.keys(config).forEach(k => {
    const matched = list.filter(c => config[k].some(key => (c.crname || "").includes(key)))
    if (matched.length > 0) {
      dimensionMap[k] = Math.round(matched.reduce((s, c) => s + parseFloat(c.scscore), 0) / matched.length)
    }
  })
  nextTick(initAbilityChart)
}

const reAnalyze = async () => {
  aiLoading.value = true;
  aiAdvice.value = ""; // 先清空，给用户反馈
  try {
    const res = await request.get('/diagnosis/getAiReport', {
      params: {
        studentId: 2,
        courseId: 1,
        intent: studentIntent.value
      }
    });

    console.log("📥 前端收到完整响应对象:", res);

    // 🌟 暴力抓取逻辑：确保拿到那段话
    let finalAdvice = "";

    if (typeof res === 'string') {
        finalAdvice = res;
    } else if (res.data) {
        // 如果 res.data 还是个对象，继续往下钻，否则直接赋值
        finalAdvice = typeof res.data === 'object' ? res.data.data : res.data;
    } else if (res.msg && res.msg !== 'success') {
        // 有时候后端把内容错放在了 msg 字段里
        finalAdvice = res.msg;
    }

    if (finalAdvice) {
      aiAdvice.value = finalAdvice;
      matchRate.value = 85;
      ElMessage.success("AI 导师已就绪");
    } else {
      aiAdvice.value = "AI 导师思考完毕，但信号传输异常，请检查控制台。";
    }
  } catch (e) {
    console.error("AI 激活失败:", e);
    ElMessage.error("连接超时，请确认 Python 端口已开启");
  } finally {
    aiLoading.value = false;
  }
};

const showDiagnosis = async (row) => {
  currentCourse.value = row
  dialogVisible.value = true
  diagLoading.value = true
  try {
    const res = await request.get('/diagnosis/getRadarData', { params: { studentId: row.sid, courseId: row.sccourseid } })
    await nextTick()
    const chart = echarts.init(courseRadarRef.value)
    const indicators = (res && res.code === 200) ? res.data.indicators : ['知识点1', '知识点2', '知识点3']
    const values = (res && res.code === 200) ? res.data.data : [80, 70, 90]
    chart.setOption({
      radar: { indicator: indicators.map(n => ({ name: n, max: 100 })), splitArea: { show: true } },
      series: [{ type: 'radar', data: [{ value: values, areaStyle: { color: 'rgba(103, 194, 58, 0.4)' } }] }]
    })
  } catch (e) {
    console.error(e)
  } finally {
    diagLoading.value = false
  }
}

const showComposition = async (row) => {
  currentCourse.value = row
  compositionVisible.value = true
  compLoading.value = true
  try {
    const score = parseFloat(row.scscore)
    compData.regular = (score * 0.95).toFixed(1)
    compData.test = (score * 0.9).toFixed(1)
    compData.exam = ((score - compData.regular*0.3 - compData.test*0.2)/0.5).toFixed(1)

    const res = await request.get('/scoreDetail/getCourseDetail', { params: { sid: row.sid, courseId: row.sccourseid } })
    const details = deepFindArray(res)
    compData.details = details.length > 0 ? details : [{ point: '核心环节', rate: 80, score: (score*0.8).toFixed(1) }]
  } catch (e) {
    console.error(e)
  } finally {
    compLoading.value = false
  }
}

const initAbilityChart = () => {
  if (!abilityRadarRef.value) return
  if (!abilityChart) abilityChart = echarts.init(abilityRadarRef.value)
  abilityChart.setOption({
    radar: { indicator: Object.keys(dimensionMap).map(k => ({ name: k, max: 100 })), splitArea: { show: true } },
    series: [{ type: 'radar', data: [{ value: Object.values(dimensionMap), areaStyle: { color: 'rgba(64, 158, 255, 0.4)' } }] }]
  })
}

onMounted(() => {
  loadTableData()
})
</script>

<style scoped>
.student-dashboard { padding: 20px; background: #f5f7f9; min-height: 100vh; }
.stat-card { background: #fff; padding: 20px; border-radius: 12px; display: flex; align-items: center; box-shadow: 0 4px 10px rgba(0,0,0,0.05); }
.stat-icon { width: 45px; height: 45px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 22px; margin-right: 12px; }
.blue .stat-icon { background: #eef5fe; color: #409EFF; }
.green .stat-icon { background: #f0f9eb; color: #67C23A; }
.orange .stat-icon { background: #fdf6ec; color: #E6A23C; }
.purple .stat-icon { background: #f5f0ff; color: #9B59B6; }
.stat-info .number { font-size: 22px; font-weight: bold; margin-top: 4px; }
.ai-header-wrapper { display: flex; justify-content: space-between; align-items: center; }
.ai-card { min-height: 410px; }
.ai-content-body { min-height: 300px; display: flex; flex-direction: column; justify-content: center; }
.empty-ai { text-align: center; color: #999; }
.advice-bubble { background: #fcfcfc; padding: 15px; border-radius: 10px; border-left: 5px solid #67C23A; box-shadow: 0 2px 10px rgba(0,0,0,0.03); margin-top: 15px; line-height: 1.6; }
.score-row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px dashed #eee; }
.score-row.final { color: #409EFF; font-weight: bold; border-bottom: none; font-size: 16px; }
.table-header { display: flex; justify-content: space-between; align-items: center; }
</style>
