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
                <el-radio-group v-model="studentIntent" size="small" style="margin-left: 10px" :disabled="aiLoading">
                  <el-radio-button label="job">就业</el-radio-button>
                  <el-radio-button label="grad">考研</el-radio-button>
                  <el-radio-button label="civil">考公</el-radio-button>
                </el-radio-group>
              </div>
            </div>
          </template>
          <div class="ai-content-body" v-loading="aiLoading" :element-loading-text="currentIntentProfile.loadingText">
            <div v-if="aiAdvice" class="analysis-active">
              <div class="intent-match">
                {{ currentIntentProfile.label }}匹配度：<span class="percentage">{{ matchRate }}%</span>
                <el-progress :percentage="matchRate" :stroke-width="12" :color="matchColor" />
              </div>
              <div class="advice-bubble"><p>{{ aiAdvice }}</p></div>
            </div>
            <div v-else class="empty-ai">
              <el-icon size="50" color="#C0C4CC"><Cpu /></el-icon>
              <p>本地 AI 规划导师待命 (Python 8000端口)</p>
              <span style="font-size:12px; color:#999">点击“激活 AI 引擎”，结合六维画像与成绩生成未来规划建议</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top: 20px">
      <template #header>
        <div class="table-header">
          <div class="table-title">
            <span><el-icon><List /></el-icon> 学期成绩明细列表</span>
            <el-button type="success" size="small" plain icon="DataAnalysis" @click="showScoreOverview">成绩图表</el-button>
          </div>
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

    <el-dialog v-model="dialogVisible" :title="`[${currentCourse?.crname}] 单科学情分析`" width="680px">
      <div v-loading="diagLoading">
        <div ref="courseRadarRef" style="height: 360px"></div>
        <div class="course-analysis-panel">
          <div class="course-analysis-title">
            <el-icon><DataAnalysis /></el-icon>
            <span>单科学情分析</span>
          </div>
          <div class="course-analysis-content">
            {{ courseAnalysisText || '正在生成该课程的学情分析...' }}
          </div>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="compositionVisible" :title="`[${currentCourse?.crname}] 成绩核算明细`" width="550px">
      <div class="comp-container" v-loading="compLoading">
        <div class="score-row"><span>平时成绩 ({{ formatWeight(compData.wregular) }})：</span><b>{{ formatScore(compData.regular) }} 分</b></div>
        <div class="score-row"><span>课堂测试 ({{ formatWeight(compData.wtest) }})：</span><b>{{ formatScore(compData.test) }} 分</b></div>
        <div class="score-row final"><span>期末考试 ({{ formatWeight(compData.wexam) }})：</span><b>{{ formatScore(compData.exam) }} 分</b></div>
        <el-divider>细粒度知识掌握度</el-divider>
        <el-table :data="compData.details" size="small" border stripe>
          <el-table-column prop="point" label="知识点" />
          <el-table-column label="掌握进度"><template #default="s"><el-progress :percentage="s.row.rate" /></template></el-table-column>
          <el-table-column prop="score" label="得分" width="70" />
        </el-table>
      </div>
    </el-dialog>

    <el-dialog v-model="scoreOverviewVisible" title="成绩可视化与弱项诊断" width="760px">
      <div class="score-overview-dialog">
        <div ref="scoreChartRef" class="score-chart"></div>
        <el-divider>AI 成绩弱项诊断</el-divider>
        <div class="weakness-actions">
          <el-button type="danger" plain icon="Cpu" @click="diagnoseScoreWeakness" :loading="scoreWeaknessLoading">
            弱项诊断
          </el-button>
        </div>
        <div class="score-weakness-box" v-loading="scoreWeaknessLoading" element-loading-text="AI 正在分析所有成绩...">
          {{ scoreWeaknessText || '点击“弱项诊断”，AI 将只基于上方所有课程成绩进行弱项分析。' }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, computed, watch } from 'vue'
import * as echarts from 'echarts'
import { MagicStick, List, Star, Connection, Refresh, TrendCharts, CircleCheck, Cpu, Memo, DataAnalysis, DataLine } from '@element-plus/icons-vue'
import request from '@/utils/Axios'
import { hturl } from '@/store/config.js'
import { ElMessage } from 'element-plus'

// 1. 状态定义
const tableData = ref([])
const loading = ref(false)
const aiLoading = ref(false)
const diagLoading = ref(false)
const compLoading = ref(false)
const dialogVisible = ref(false)
const compositionVisible = ref(false)
const scoreOverviewVisible = ref(false)
const currentCourse = ref(null)
const courseAnalysisText = ref('')
const scoreWeaknessLoading = ref(false)
const scoreWeaknessText = ref('')

// 2. 数据定义
const studentIntent = ref('job')
const aiAdvice = ref('')
const matchRate = ref(0)
const dimensionMap = reactive({ '核心力': 75, '逻辑力': 75, '实践力': 75, '设计力': 75, '前沿力': 75, '综合力': 75 })
const intentProfiles = {
  job: {
    label: '就业',
    loadingText: '正在分析项目实践与岗位能力匹配...',
    weights: { '实践力': 0.35, '设计力': 0.25, '综合力': 0.2, '核心力': 0.1, '逻辑力': 0.1 }
  },
  grad: {
    label: '考研',
    loadingText: '正在分析理论基础与深造潜力...',
    weights: { '核心力': 0.35, '逻辑力': 0.25, '前沿力': 0.2, '综合力': 0.1, '设计力': 0.1 }
  },
  civil: {
    label: '考公',
    loadingText: '正在分析基础稳定性与规范表达能力...',
    weights: { '核心力': 0.3, '综合力': 0.25, '逻辑力': 0.25, '设计力': 0.1, '实践力': 0.1 }
  }
}
const stats = reactive({ avgScore: 0, passRate: 0 })
const compData = reactive({
  regular: null,
  test: null,
  exam: null,
  wregular: 0.3,
  wtest: 0.2,
  wexam: 0.5,
  details: []
})

const abilityRadarRef = ref(null)
const courseRadarRef = ref(null)
const scoreChartRef = ref(null)
let abilityChart = null
let scoreChart = null

// 3. 配置计算项
const statItems = computed(() => [
  { label: '平均分数', value: stats.avgScore, unit: '', icon: 'TrendCharts', type: 'blue' },
  { label: '及格率', value: stats.passRate, unit: '%', icon: 'CircleCheck', type: 'green' },
  { label: '已修课程', value: tableData.value.length, unit: '门', icon: 'DataLine', type: 'orange' },
  { label: '学业评价', value: stats.avgScore > 85 ? '优秀' : '良好', unit: '', icon: 'Star', type: 'purple' }
])
const currentIntentProfile = computed(() => intentProfiles[studentIntent.value] || intentProfiles.job)
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

const pickValue = (obj, keys, fallback = null) => {
  for (const key of keys) {
    if (obj && obj[key] !== undefined && obj[key] !== null && obj[key] !== '') {
      return obj[key]
    }
  }
  return fallback
}

const toNumber = (value, fallback = 0) => {
  const num = Number(value)
  return Number.isFinite(num) ? num : fallback
}

const formatScore = (value) => {
  const num = Number(value)
  return Number.isFinite(num) ? num.toFixed(1) : '--'
}

const formatWeight = (value) => {
  const num = Number(value)
  return Number.isFinite(num) ? `${Math.round(num * 100)}%` : '--'
}

const calculateIntentMatchRate = () => {
  const weights = currentIntentProfile.value.weights
  let weightedScore = 0
  let totalWeight = 0

  Object.entries(weights).forEach(([dimension, weight]) => {
    weightedScore += (Number(dimensionMap[dimension]) || 0) * weight
    totalWeight += weight
  })

  return totalWeight > 0 ? Math.round(weightedScore / totalWeight) : 0
}

const buildScoreSummary = () => {
  const validScores = tableData.value
    .map(item => Number(item.scscore))
    .filter(score => Number.isFinite(score))

  const sortedByScore = [...tableData.value].sort((left, right) => {
    return (Number(right.scscore) || 0) - (Number(left.scscore) || 0)
  })

  return {
    avgScore: Number(stats.avgScore) || 0,
    passRate: Number(stats.passRate) || 0,
    courseCount: tableData.value.length,
    maxScore: validScores.length ? Math.max(...validScores) : 0,
    minScore: validScores.length ? Math.min(...validScores) : 0,
    strongCourses: sortedByScore.slice(0, 3).map(item => `${item.crname || '未命名课程'}(${Number(item.scscore) || 0}分)`),
    weakCourses: sortedByScore.slice(-3).reverse().map(item => `${item.crname || '未命名课程'}(${Number(item.scscore) || 0}分)`)
  }
}

const buildPlanningPayload = () => {
  const firstCourse = tableData.value[0] || {}

  return {
    studentId: firstCourse.sid,
    studentName: firstCourse.sname || '该生',
    studentNo: firstCourse.sno || '',
    intent: studentIntent.value,
    dimensions: { ...dimensionMap },
    scoreSummary: buildScoreSummary(),
    courses: tableData.value.map(item => ({
      term: item.yall || `${item.yyear || ''}${item.yterm || ''}`,
      courseName: item.crname || '未命名课程',
      score: Number(item.scscore) || 0,
      regularScore: Number(pickValue(item, ['scRegular', 'sc_regular', 'scregular'], 0)) || 0,
      testScore: Number(pickValue(item, ['scTest', 'sc_test', 'sctest'], 0)) || 0,
      examScore: Number(pickValue(item, ['scExam', 'sc_exam', 'scexam'], 0)) || 0
    }))
  }
}

const buildScoreWeaknessPayload = () => {
  const firstCourse = tableData.value[0] || {}
  const summary = buildScoreSummary()

  return {
    studentName: firstCourse.sname || '该生',
    studentNo: firstCourse.sno || '',
    avgScore: summary.avgScore,
    passRate: summary.passRate,
    courseCount: summary.courseCount,
    maxScore: summary.maxScore,
    minScore: summary.minScore,
    courses: tableData.value.map(item => ({
      term: item.yall || `${item.yyear || ''}${item.yterm || ''}`,
      courseName: item.crname || '未命名课程',
      score: Number(item.scscore) || 0,
      regularScore: Number(pickValue(item, ['scRegular', 'sc_regular', 'scregular'], 0)) || 0,
      testScore: Number(pickValue(item, ['scTest', 'sc_test', 'sctest'], 0)) || 0,
      examScore: Number(pickValue(item, ['scExam', 'sc_exam', 'scexam'], 0)) || 0
    }))
  }
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
    await analyzeAcademic(list)
  } catch (e) {
    console.error("Fetch Error:", e)
    ElMessage.error("后端服务连接失败")
  } finally {
    loading.value = false
  }
}

const analyzeAcademic = async (list) => {
  const nums = list.map(i => parseFloat(i.scscore) || 0)

  if (nums.length > 0) {
    stats.avgScore = (nums.reduce((a, b) => a + b, 0) / nums.length).toFixed(1)
    stats.passRate = Math.round(
      (nums.filter(s => s >= 60).length / nums.length) * 100
    )
  }

  // ✅ 从后端获取一个课程做分析
  if (list.length > 0) {
    const first = list[0]

    const res = await request.get('/diagnosis/getRadarData', {
      params: {
        studentId: first.sid,
        courseId: first.sccourseid
      }
    })

    const resp = res?.data || res
    const code = resp.code ?? resp._code
    if (code === 200 && resp.data) {
      const values = resp.data.data || []

      // 映射成 6 维（智能拆分）
      const keys = Object.keys(dimensionMap)
      keys.forEach((k, i) => {
        dimensionMap[k] = values[i % values.length] || 60
      })
    }
  }

  nextTick(initAbilityChart)
}

const reAnalyze = async () => {
  aiLoading.value = true;
  aiAdvice.value = ""; // 先清空，给用户反馈
  try {
    const firstCourse = tableData.value[0]
    if (!firstCourse || !firstCourse.sid || tableData.value.length === 0) {
      ElMessage.warning("暂无可用于 AI 规划分析的成绩数据")
      return
    }

    const res = await request.post('/diagnosis/getPlanningReport', buildPlanningPayload());

    console.log("📥 前端收到完整响应对象:", res);

    // 🌟 暴力抓取逻辑：确保拿到那段话
    let finalAdvice = "";

    const resp = res?.data || res

    if (typeof resp === 'string') {
        finalAdvice = resp;
    } else if (resp.data) {
        // 如果 res.data 还是个对象，继续往下钻，否则直接赋值
        finalAdvice = typeof resp.data === 'object' ? resp.data.data : resp.data;
    } else if (resp.msg && resp.msg !== 'success') {
        // 有时候后端把内容错放在了 msg 字段里
        finalAdvice = resp.msg;
    }

    if (finalAdvice) {
      aiAdvice.value = finalAdvice;
      matchRate.value = calculateIntentMatchRate();
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

watch(studentIntent, () => {
  matchRate.value = calculateIntentMatchRate()
  if (aiAdvice.value && !aiLoading.value) {
    reAnalyze()
  }
})

const showDiagnosis = async (row) => {
  currentCourse.value = row
  dialogVisible.value = true
  diagLoading.value = true
  courseAnalysisText.value = ''

  try {
    const params = {
      studentId: row.sid,
      courseId: row.sccourseid
    }

    const radarRes = await request.get('/diagnosis/getRadarData', { params })
    streamCourseAnalysis(params)

    const resp = radarRes.data || {}

    if (resp.code !== 200) {
      ElMessage.error(resp.msg || "获取雷达图失败")
      return
    }

    const radar = resp.data || {}

    const indicators = radar.indicators || []
    const values = (radar.data || []).map((value) => {
      const num = Number(value)
      return Number.isFinite(num) ? num : 0
    })

    if (!indicators.length || !values.length) {
      ElMessage.warning("暂无雷达数据")
      return
    }

    await nextTick()

    const chart = echarts.init(courseRadarRef.value)

    chart.setOption({
      tooltip: { trigger: 'item' },

      radar: {
        indicator: indicators.map((item, index) => {
          const source = item && typeof item === 'object' ? item : {}
          const name = typeof item === 'string'
            ? item
            : source.name || source.pointName || source.label || `知识点${index + 1}`

          return {
            name,
            max: Number(source.max) || 100
          }
        }),
        splitNumber: 5,
        splitArea: { show: true }
      },

      series: [
        {
          type: 'radar',
          data: [
            {
              value: values,
              name: '能力值',
              areaStyle: {
                color: 'rgba(64,158,255,0.4)'
              }
            }
          ]
        }
      ]
    })

  } catch (e) {
    console.error(e)
    ElMessage.error("单科学情分析加载失败")
  } finally {
    diagLoading.value = false
  }
}

const streamCourseAnalysis = (params) => {
  const token = localStorage.getItem("token") || ""
  if (!window.EventSource || !token) {
    loadCourseAnalysisFallback(params)
    return
  }

  const query = new URLSearchParams({
    studentId: String(params.studentId),
    courseId: String(params.courseId),
    intent: studentIntent.value || "job",
    token
  })

  const source = new EventSource(`${hturl}/diagnosis/streamReport?${query.toString()}`)
  let hasChunk = false
  courseAnalysisText.value = ''

  source.addEventListener("diagnosis", (event) => {
    hasChunk = true
    courseAnalysisText.value += event.data || ""
  })

  source.addEventListener("done", () => {
    source.close()
    if (!hasChunk) {
      courseAnalysisText.value = "AI 分析完成，但未返回有效内容。"
    }
  })

  source.onerror = () => {
    source.close()
    if (!hasChunk) {
      loadCourseAnalysisFallback(params)
    }
  }
}

const loadCourseAnalysisFallback = async (params) => {
  try {
    const analysisRes = await request.get('/diagnosis/getCourseAnalysis', { params })
    const analysisResp = analysisRes?.data || analysisRes
    if (typeof analysisResp === 'string') {
      courseAnalysisText.value = analysisResp
    } else if (analysisResp.data) {
      courseAnalysisText.value = typeof analysisResp.data === 'object' ? analysisResp.data.data : analysisResp.data
    } else if (analysisResp.msg && analysisResp.msg !== 'success') {
      courseAnalysisText.value = analysisResp.msg
    }
  } catch (error) {
    courseAnalysisText.value = "AI 分析暂时不可用，请稍后重试。"
  }
}

const showScoreOverview = async () => {
  if (!tableData.value.length) {
    ElMessage.warning("暂无可视化的成绩数据")
    return
  }

  scoreOverviewVisible.value = true
  await nextTick()
  initScoreChart()
}

const initScoreChart = () => {
  if (!scoreChartRef.value) return
  if (!scoreChart) scoreChart = echarts.init(scoreChartRef.value)

  const courses = tableData.value.map(item => item.crname || '未命名课程')
  const scores = tableData.value.map(item => Number(item.scscore) || 0)

  scoreChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 45, right: 24, top: 35, bottom: 70 },
    xAxis: {
      type: 'category',
      data: courses,
      axisLabel: {
        interval: 0,
        rotate: courses.length > 5 ? 30 : 0
      }
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 100,
      name: '分数'
    },
    series: [
      {
        name: '总分',
        type: 'bar',
        data: scores,
        itemStyle: {
          color: (params) => {
            const value = Number(params.value) || 0
            if (value < 60) return '#F56C6C'
            if (value < 75) return '#E6A23C'
            return '#409EFF'
          },
          borderRadius: [6, 6, 0, 0]
        },
        label: {
          show: true,
          position: 'top'
        }
      }
    ]
  })

  scoreChart.resize()
}

const diagnoseScoreWeakness = async () => {
  if (!tableData.value.length) {
    ElMessage.warning("暂无可用于弱项诊断的成绩数据")
    return
  }

  scoreWeaknessLoading.value = true
  scoreWeaknessText.value = ''

  try {
    const res = await request.post('/diagnosis/getScoreWeaknessReport', buildScoreWeaknessPayload())
    const resp = res?.data || res

    if (typeof resp === 'string') {
      scoreWeaknessText.value = resp
    } else if (resp.data) {
      scoreWeaknessText.value = typeof resp.data === 'object' ? resp.data.data : resp.data
    } else if (resp.msg && resp.msg !== 'success') {
      scoreWeaknessText.value = resp.msg
    }

    if (!scoreWeaknessText.value) {
      scoreWeaknessText.value = 'AI 分析完成，但未返回有效内容，请稍后重试。'
    }
  } catch (e) {
    console.error(e)
    ElMessage.error("成绩弱项诊断失败")
  } finally {
    scoreWeaknessLoading.value = false
  }
}

const showComposition = async (row) => {
  currentCourse.value = row
  compositionVisible.value = true
  compLoading.value = true
  compData.regular = pickValue(row, ['scRegular', 'sc_regular', 'scregular'])
  compData.test = pickValue(row, ['scTest', 'sc_test', 'sctest'])
  compData.exam = pickValue(row, ['scExam', 'sc_exam', 'scexam'])
  compData.wregular = toNumber(pickValue(row, ['wregular', 'w_regular'], 0.3), 0.3)
  compData.wtest = toNumber(pickValue(row, ['wtest', 'w_test'], 0.2), 0.2)
  compData.wexam = toNumber(pickValue(row, ['wexam', 'w_exam'], 0.5), 0.5)
  compData.details = []

  try {
    const res = await request.get('/scoreDetail/getCourseDetail', {
      params: {
        sid: row.sid,
        courseId: row.sccourseid
      }
    })

    const list = deepFindArray(res)

    console.log("📊 成绩明细数据：", list)

    if (!list || list.length === 0) {
      ElMessage.warning("暂无成绩明细")
      return
    }

    // ✅ 正确字段映射（关键！！）
    compData.details = list.map(item => ({
      point: item.point,            // ✅ 正确
      rate: Number(item.rate) || 0, // ✅ 后端已经算好了
      score: Number(item.score) || 0
    }))

  } catch (e) {
    console.error(e)
    ElMessage.error("加载成绩构成失败")
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
.course-analysis-panel { background: #fcfcfc; border-left: 5px solid #409EFF; border-radius: 8px; padding: 14px 16px; margin-top: 8px; line-height: 1.7; box-shadow: 0 2px 10px rgba(0,0,0,0.03); }
.course-analysis-title { display: flex; align-items: center; gap: 6px; font-weight: 700; color: #303133; margin-bottom: 8px; }
.course-analysis-content { white-space: pre-line; color: #606266; font-size: 14px; }
.score-row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px dashed #eee; }
.score-row.final { color: #409EFF; font-weight: bold; border-bottom: none; font-size: 16px; }
.table-header { display: flex; justify-content: space-between; align-items: center; }
.table-title { display: flex; align-items: center; gap: 10px; }
.score-chart { height: 340px; width: 100%; }
.weakness-actions { display: flex; justify-content: flex-start; margin-bottom: 12px; }
.score-weakness-box { min-height: 130px; background: #fcfcfc; border-left: 5px solid #F56C6C; border-radius: 8px; padding: 14px 16px; line-height: 1.7; color: #606266; white-space: pre-line; box-shadow: 0 2px 10px rgba(0,0,0,0.03); }
</style>
