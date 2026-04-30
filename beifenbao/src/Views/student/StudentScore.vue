<template>
  <div class="page-box">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="课程名称 / 课程编号"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="学期">
          <el-select
            v-model="searchForm.termid"
            placeholder="请选择学期"
            clearable
            filterable
            style="width: 220px"
          >
            <el-option
              v-for="item in termList"
              :key="item.yid || item.yId"
              :label="item.yall || item.yAll"
              :value="item.yid || item.yId"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="16" class="stat-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="(val, key) in statLabels" :key="key">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">{{ val }}</div>
          <div class="stat-value">{{ statData[key] }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="chart-card">
          <template #header><span>成绩分布比例</span></template>
          <div ref="scorePieRef" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="chart-card">
          <template #header><span>课程成绩对比</span></template>
          <div ref="scoreBarRef" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="chart-card">
          <template #header><span>成绩变动趋势</span></template>
          <div ref="scoreLineRef" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%">
        <el-table-column type="index" label="#" width="60" align="center" />
        <el-table-column prop="yall" label="学期" min-width="140" show-overflow-tooltip />
        <el-table-column prop="crname" label="课程名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="scscore" label="成绩" width="100">
          <template #default="{ row }">
            <b :style="{ color: Number(row.scscore) < 60 ? '#F56C6C' : '#67C23A' }">
              {{ formatScore(row.scscore) }}
            </b>
          </template>
        </el-table-column>
        <el-table-column label="结果" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="Number(row.scscore) >= 60 ? 'success' : 'danger'">
              {{ Number(row.scscore) >= 60 ? "及格" : "不及格" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="tname" label="教师" width="120" show-overflow-tooltip />

        <el-table-column label="智能诊断" width="120" align="center" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              color="#626aef"
              plain
              @click="handleAiAnalysis(scope.row)"
            >
              <el-icon style="margin-right: 4px"><Cpu /></el-icon> AI 导师
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-box">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="pageIndex"
          :page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="aiDialogVisible"
      title="🎓 AI 专属学情导师"
      width="850px"
      destroy-on-close
      @closed="clearRadarChart"
    >
      <el-row :gutter="20">
        <el-col :span="11">
          <div style="font-weight: bold; margin-bottom: 12px; color: #606266;">📊 知识维度画像：</div>
          <div
            id="radarChartDom"
            v-loading="radarLoading"
            style="width: 100%; height: 320px; border: 1px solid #f0f2f5; border-radius: 8px;"
          ></div>
        </el-col>
        <el-col :span="13">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px;">
            <span style="font-weight: bold; color: #626aef;"><el-icon><ChatLineRound /></el-icon> 诊断报告：</span>
            <el-button type="primary" size="small" :loading="aiTextLoading" @click="getAiReport">刷新分析</el-button>
          </div>
          <el-card shadow="never" style="height: 320px; overflow-y: auto; background-color: #fafafa;">
            <div v-loading="aiTextLoading">
              <div v-if="aiAdvice" style="white-space: pre-wrap; line-height: 1.8; font-size: 14px; color: #333;">
                {{ aiAdvice }}
              </div>
              <el-empty v-else description="正在等待 AI 导师扫描..." :image-size="60" style="padding-top: 40px;" />
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, onBeforeUnmount } from "vue"
import axios from "@/utils/Axios"
import { ElMessage } from "element-plus"
import * as echarts from "echarts"
import { Cpu, ChatLineRound } from '@element-plus/icons-vue'

// --- 状态定义 ---
const loading = ref(false)
const tableData = ref([])
const termList = ref([])
const pageIndex = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchForm = reactive({ qkey: "", termid: null })

const statLabels = { totalCount: '总选课数', avgScore: '总平均分', maxScore: '历史最高', passCount: '及格门数' }
const statData = reactive({ totalCount: 0, avgScore: 0, maxScore: 0, passCount: 0 })

// --- ECharts 引用 ---
const scorePieRef = ref(null)
const scoreBarRef = ref(null)
const scoreLineRef = ref(null)
let scorePieChart = null
let scoreBarChart = null
let scoreLineChart = null
let radarInstance = null

// --- 3. 动态身份识别 (多字段兼容，解决 400 核心) ---
const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
const currentStudentId = userInfo.sid || userInfo.sId || userInfo.uId || userInfo.uid || userInfo.id || userInfo.urId
console.log("【调试】当前用户信息:", userInfo)
console.log("【调试】检测到的登录 ID:", currentStudentId)

// --- 工具函数 ---
const formatScore = (val) => (val === null || val === undefined) ? "-" : Number(val).toFixed(1)
const scoreTagType = (score) => {
  const num = Number(score)
  if (num >= 90) return "success"
  if (num >= 60) return ""
  return "danger"
}

// =================== 1. 数据加载逻辑 ===================

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/student/score/getlist", {
      params: {
        pageIndex: pageIndex.value,
        pageSize: pageSize.value,
        qkey: searchForm.qkey,
        termid: searchForm.termid || ""
      }
    })
    if (res.data?.code === 200 || res.data?._code === 200) {
      tableData.value = res.data.data || []
      total.value = Number(res.data.totalRecord || 0)
    }
  } catch (e) { console.error("表格加载失败:", e) }
  finally { loading.value = false }
}

const fetchAllForCharts = async () => {
  try {
    // 请求全量数据用于图表和统计（pageSize设大一些）
    const res = await axios.get("/student/score/getlist", {
      params: { pageIndex: 1, pageSize: 1000, qkey: searchForm.qkey, termid: searchForm.termid }
    })
    if (res.data?.code === 200 || res.data?._code === 200) {
      renderAllVisuals(res.data.data || [])
    }
  } catch (e) { console.error("全量数据加载失败:", e) }
}

const loadTermList = async () => {
  try {
    const res = await axios.get("/yearterm/getall")
    termList.value = res.data?.data || []
  } catch (e) { console.error("学期加载失败:", e) }
}

// =================== 2. 图表渲染核心 (补全饼/柱/线) ===================

const renderAllVisuals = (data) => {
  // A. 计算统计数值
  const scores = data.map(i => Number(i.scscore)).filter(s => !isNaN(s))
  statData.totalCount = data.length
  statData.maxScore = scores.length ? Math.max(...scores).toFixed(1) : 0
  statData.avgScore = scores.length ? (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(1) : 0
  statData.passCount = data.filter(i => Number(i.scscore) >= 60).length

  nextTick(() => {
    // B. 渲染饼图
    if (scorePieRef.value) {
      if (scorePieChart) scorePieChart.dispose()
      scorePieChart = echarts.init(scorePieRef.value)
      const ex = data.filter(i => i.scscore >= 90).length
      const gd = data.filter(i => i.scscore >= 75 && i.scscore < 90).length
      const ps = data.filter(i => i.scscore >= 60 && i.scscore < 75).length
      const fl = data.filter(i => i.scscore < 60).length
      scorePieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}门 ({d}%)' },
        legend: { bottom: 0, icon: 'circle' },
        color: ['#67C23A', '#409EFF', '#E6A23C', '#F56C6C'],
        series: [{ type: 'pie', radius: ['40%', '70%'], data: [
          { value: ex, name: '优秀' }, { value: gd, name: '良好' }, { value: ps, name: '及格' }, { value: fl, name: '不及格' }
        ]}]
      })
    }

    // C. 渲染柱状图 (取前10条展示)
    if (scoreBarRef.value) {
      if (scoreBarChart) scoreBarChart.dispose()
      scoreBarChart = echarts.init(scoreBarRef.value)
      const barData = data.slice(0, 10)
      scoreBarChart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { bottom: 60, left: 40, right: 20 },
        xAxis: { type: 'category', data: barData.map(i => i.crname), axisLabel: { rotate: 25, fontSize: 10 } },
        yAxis: { type: 'value', max: 100 },
        series: [{ name: '分数', type: 'bar', data: barData.map(i => i.scscore), barMaxWidth: 30, itemStyle: { color: '#409EFF' } }]
      })
    }

    // D. 渲染折线图 (按学期展示平均分)
    if (scoreLineRef.value) {
      if (scoreLineChart) scoreLineChart.dispose()
      scoreLineChart = echarts.init(scoreLineRef.value)
      const termGroup = {}
      data.forEach(i => { if(!termGroup[i.yall]) termGroup[i.yall] = []; termGroup[i.yall].push(Number(i.scscore)) })
      const xKeys = Object.keys(termGroup)
      const yAvg = xKeys.map(k => (termGroup[k].reduce((a,b)=>a+b,0)/termGroup[k].length).toFixed(1))
      scoreLineChart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: xKeys },
        yAxis: { type: 'value', min: 0, max: 100 },
        series: [{ name: '平均成绩', type: 'line', smooth: true, data: yAvg, itemStyle: { color: '#67C23A' }, areaStyle: { color: 'rgba(103,194,58,0.1)' } }]
      })
    }
  })
}

// =================== 3. AI 导师逻辑 (解决参数缺失) ===================

const aiDialogVisible = ref(false), radarLoading = ref(false), aiTextLoading = ref(false), aiAdvice = ref(""), selectedCourseId = ref(null)

const handleAiAnalysis = (row) => {
  // 1. 提取课程 ID (多字段适配)
  selectedCourseId.value = row.scCourseId || row.sccourseid || row.crid || row.courseId

  if (!selectedCourseId.value) {
    ElMessage.error("获取课程信息失败")
    return
  }

  // 2. 检查用户 ID 存在性，防止 400
  if (!currentStudentId) {
    ElMessage.error("身份过期，请重新登录后再试")
    return
  }

  aiAdvice.value = ""
  aiDialogVisible.value = true

  // 3. 弹窗打开后异步加载
  nextTick(() => {
    loadRadarData()
    getAiReport()
  })
}

const loadRadarData = async () => {
  radarLoading.value = true
  try {
    const res = await axios.get('/diagnosis/getRadarData', {
      params: {
        studentId: currentStudentId,
        courseId: selectedCourseId.value
      }
    })
    if (res.data?.code === 200) {
      const dom = document.getElementById('radarChartDom')
      if (radarInstance) radarInstance.dispose()
      radarInstance = echarts.init(dom)
      radarInstance.setOption({
        radar: {
          indicator: res.data.data.indicator,
          shape: 'circle',
          axisName: { color: '#666' }
        },
        series: [{
          type: 'radar',
          data: [{
            value: res.data.data.data,
            name: '掌握度',
            areaStyle: { color: 'rgba(98, 106, 239, 0.3)' },
            lineStyle: { color: '#626aef' }
          }]
        }]
      })
    }
  } catch (e) { console.error("雷达图 400/500 错误:", e) }
  finally { radarLoading.value = false }
}

const getAiReport = async () => {
  aiTextLoading.value = true
  try {
    const res = await axios.get('/diagnosis/getAiReport', {
      params: {
        studentId: currentStudentId,
        courseId: selectedCourseId.value
      }
    })
    if (res.data?.code === 200) {
      aiAdvice.value = res.data.data
    }
  } catch (e) {
    console.error("AI 建议 400/500 错误:", e)
    aiAdvice.value = "连接诊断引擎失败，请稍后再试。"
  } finally { aiTextLoading.value = false }
}

const clearRadarChart = () => {
  if (radarInstance) { radarInstance.dispose(); radarInstance = null }
}

// --- 基础交互 ---
const handleSearch = () => { pageIndex.value = 1; loadTableData(); fetchAllForCharts(); }
const handleReset = () => { searchForm.qkey = ""; searchForm.termid = null; handleSearch(); }
const handleCurrentChange = (val) => { pageIndex.value = val; loadTableData() }
const handleSizeChange = (val) => { pageSize.value = val; pageIndex.value = 1; loadTableData() }

onMounted(async () => {
  await loadTermList()
  loadTableData()
  fetchAllForCharts()
  window.addEventListener("resize", () => {
    scorePieChart?.resize(); scoreBarChart?.resize(); scoreLineChart?.resize(); radarInstance?.resize()
  })
})

onBeforeUnmount(() => {
  scorePieChart?.dispose(); scoreBarChart?.dispose(); scoreLineChart?.dispose()
  if (radarInstance) radarInstance.dispose()
})
</script>

<style scoped>
.page-box { padding: 16px; background-color: #f5f7fa; min-height: 100vh; }
.search-card, .table-card, .chart-card, .stat-row { margin-bottom: 16px; border: none; border-radius: 8px; }
.stat-card { text-align: center; border: none; }
.stat-title { font-size: 13px; color: #909399; margin-bottom: 4px; }
.stat-value { font-size: 24px; font-weight: bold; color: #303133; }
.pager-box { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
