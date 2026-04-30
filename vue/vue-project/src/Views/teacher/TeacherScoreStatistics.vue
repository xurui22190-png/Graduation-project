<template>
  <div class="page-box">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="学期">
          <el-select v-model="searchForm.tctermid" placeholder="请选择学期" clearable filterable style="width: 180px" @change="handleTermChange">
            <el-option v-for="item in termList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select v-model="searchForm.tcclassid" placeholder="请选择班级" clearable filterable style="width: 180px" @change="handleClassChange">
            <el-option v-for="item in classList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="课程">
          <el-select v-model="searchForm.tccourseid" placeholder="请选择课程" clearable filterable style="width: 180px">
            <el-option v-for="item in courseList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询统计</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="info" color="#626aef" @click="handleClassDiagnosis">
            <el-icon style="margin-right: 4px"><DataLine /></el-icon> AI 教研深度分析
          </el-button>
        </el-form-item>
      </el-form>

      <div class="statistics-tip">
        <el-icon style="vertical-align: middle; margin-right: 4px;"><InfoFilled /></el-icon>
        仅统计已录入成绩的数据，未录入学生不会计入平均分、及格率、优秀率等统计结果。
      </div>
    </el-card>

    <div class="summary-box">
      <el-row :gutter="16">
        <el-col :span="3">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">参考人数</div>
            <div class="summary-value text-blue">{{ summary.totalCount }}</div>
          </el-card>
        </el-col>
        <el-col :span="3">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">平均分</div>
            <div class="summary-value text-purple">{{ summary.avgScore }}</div>
          </el-card>
        </el-col>
        <el-col :span="3">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">最高分</div>
            <div class="summary-value text-green">{{ summary.maxScore }}</div>
          </el-card>
        </el-col>
        <el-col :span="3">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">最低分</div>
            <div class="summary-value text-red">{{ summary.minScore }}</div>
          </el-card>
        </el-col>
        <el-col :span="3">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">及格率</div>
            <div class="summary-value">{{ summary.passRate }}<span class="unit">%</span></div>
          </el-card>
        </el-col>
        <el-col :span="3">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">优秀率</div>
            <div class="summary-value text-orange">{{ summary.goodRate }}<span class="unit">%</span></div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="summary-card highlight-card">
            <div class="summary-title text-white">集体最弱知识点 (AI诊断)</div>
            <div class="summary-value text-white" style="font-size: 22px; margin-top: 5px;">
              {{ summary.weakestPoint }}
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="card-header-title">成绩分布区间统计</span></template>
          <div ref="rangePieRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="card-header-title">班级平均分对比统计</span></template>
          <div ref="classBarRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="card-header-title">历史学期成绩趋势</span></template>
          <div ref="trendLineRef" class="chart-box trend-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="table-card">
      <template #header>
        <span class="card-header-title">底层成绩明细清单</span>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="sno" label="学号" min-width="130" />
        <el-table-column prop="sname" label="姓名" min-width="100" />
        <el-table-column prop="ssex" label="性别" width="80" align="center" />
        <el-table-column prop="classname" label="班级" min-width="140" />
        <el-table-column prop="crname" label="课程" min-width="140" />
        <el-table-column prop="yall" label="学期" min-width="140" />
        <el-table-column prop="scscore" label="成绩" width="100" align="center">
          <template #default="scope">
            <span :style="{ color: scope.row.scscore < 60 ? '#F56C6C' : '#67C23A', fontWeight: 'bold' }">
              {{ scope.row.scscore }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="screatedate" label="录入时间" min-width="180" />
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

    <el-dialog v-model="classAiDialogVisible" title="大模型智能教研分析系统" width="750px">
      <div v-loading="isGeneratingClassAi" style="min-height: 200px;">
        <div v-if="classAiReportText" style="padding: 5px;">
          <div style="display: flex; align-items: center; margin-bottom: 15px;">
            <el-tag size="large" type="success" effect="dark">
               <el-icon style="margin-right:4px"><Cpu /></el-icon>Ollama 本地引擎已生成智能洞察
            </el-tag>
            <span style="margin-left: 12px; color: #909399; font-size: 13px;">
              AI 已结合上述成绩大盘分布进行推理
            </span>
          </div>
          <el-card shadow="never" style="line-height: 1.8; white-space: pre-wrap; background-color: #f8fafd; border: 1px solid #e1eaf4; color: #303133; font-size: 15px; min-height: 150px;">
            {{ classAiReportText }}
          </el-card>
        </div>
      </div>
      <template #footer>
        <el-button type="primary" @click="classAiDialogVisible = false">完成审阅</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick, onBeforeUnmount } from "vue"
import * as echarts from "echarts"
import axios from "@/utils/Axios"
import { ElMessage } from "element-plus"
import { DataLine, InfoFilled, Cpu } from '@element-plus/icons-vue'

const loading = ref(false)

const pageIndex = ref(1)
const pageSize = ref(10)
const total = ref(0)

const tableData = ref([])
const taskList = ref([])

const searchForm = reactive({
  tctermid: null,
  tcclassid: null,
  tccourseid: null
})

const summary = reactive({
  totalCount: 0,
  avgScore: 0,
  maxScore: 0,
  minScore: 0,
  passRate: 0,
  goodRate: 0,
  weakestPoint: '-' // 💡 新增：最弱知识点字段
})

const classAiDialogVisible = ref(false)
const isGeneratingClassAi = ref(false)
const classAiReportText = ref("")

const rangePieRef = ref(null)
const classBarRef = ref(null)
const trendLineRef = ref(null)

let rangePieChart = null
let classBarChart = null
let trendLineChart = null

const chartData = reactive({
  rangeData: [],
  classAvgData: [],
  trendData: []
})

const fixNumber = (val, digits = 2) => {
  if (val === null || val === undefined || val === "") return 0
  const num = Number(val)
  if (Number.isNaN(num)) return 0
  return Number(num.toFixed(digits))
}

// =================== 级联下拉框核心逻辑 ===================
const buildUniqueOptions = (list, valueKey, labelKey) => {
  const map = new Map()
  ;(list || []).forEach((item) => {
    const value = item[valueKey]
    const label = item[labelKey]
    if (value !== null && value !== undefined && value !== "" && !map.has(value)) {
      map.set(value, { value, label })
    }
  })
  return Array.from(map.values())
}

const termList = computed(() => buildUniqueOptions(taskList.value, "tctermid", "yall"))

const classList = computed(() => {
  let arr = [...taskList.value]
  if (searchForm.tctermid) arr = arr.filter((item) => item.tctermid === searchForm.tctermid)
  return buildUniqueOptions(arr, "tcclassid", "cname")
})

const courseList = computed(() => {
  let arr = [...taskList.value]
  if (searchForm.tctermid) arr = arr.filter((item) => item.tctermid === searchForm.tctermid)
  if (searchForm.tcclassid) arr = arr.filter((item) => item.tcclassid === searchForm.tcclassid)
  return buildUniqueOptions(arr, "tccourseid", "crname")
})

const handleTermChange = () => { searchForm.tcclassid = null; searchForm.tccourseid = null }
const handleClassChange = () => { searchForm.tccourseid = null }

const loadTaskList = async () => {
  try {
    const res = await axios.get("/teacherscore/gettasklist")
    const resp = res?.data || {}
    if ((resp.code ?? resp._code) === 200) taskList.value = resp.data || []
  } catch (error) { console.error("获取授课任务失败：", error) }
}

// =================== 数据加载逻辑 ===================
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/teacher/scorestatistics/getlist", {
      params: {
        pageIndex: pageIndex.value, pageSize: pageSize.value,
        tctermid: searchForm.tctermid || 0, tcclassid: searchForm.tcclassid || 0, tccourseid: searchForm.tccourseid || 0
      }
    })
    const result = res.data || {}
    tableData.value = Array.isArray(result.data) ? result.data : []
    total.value = Number(result.totalRecord || 0)
  } catch (error) {
    tableData.value = []; total.value = 0
  } finally { loading.value = false }
}

const loadSummaryData = async () => {
  try {
    const res = await axios.get("/teacher/scorestatistics/summary", {
      params: {
        tctermid: searchForm.tctermid || 0, tcclassid: searchForm.tcclassid || 0, tccourseid: searchForm.tccourseid || 0
      }
    })
    const data = res.data?.data || {}
    summary.totalCount = Number(data.totalCount || 0)
    summary.avgScore = fixNumber(data.avgScore)
    summary.maxScore = fixNumber(data.maxScore)
    summary.minScore = fixNumber(data.minScore)
    summary.passRate = fixNumber(data.passRate)
    summary.goodRate = fixNumber(data.goodRate)
  } catch (error) { /* 置零逻辑略 */ }
}

// 💡 核心新增：去获取最弱知识点的数据
const loadWeakPointData = async () => {
  // 只有在选了具体班级和课程时，才有弱点分析意义
  if (!searchForm.tcclassid || !searchForm.tccourseid) {
    summary.weakestPoint = '请选择班级课程'
    return
  }
  summary.weakestPoint = '分析中...'
  try {
    const res = await axios.get("/diagnosis/classAnalysis", {
      params: { classId: searchForm.tcclassid, courseId: searchForm.tccourseid }
    })
    const data = res.data?.data || {}
    summary.weakestPoint = data.weakestPoint || '未检测到明显短板'
  } catch (error) {
    summary.weakestPoint = '检测失败'
  }
}

const loadChartData = async () => {
  try {
    const res = await axios.get("/teacher/scorestatistics/chart", {
      params: {
        tctermid: searchForm.tctermid || 0, tcclassid: searchForm.tcclassid || 0, tccourseid: searchForm.tccourseid || 0
      }
    })
    const data = res.data?.data || {}
    chartData.rangeData = Array.isArray(data.rangeData) ? data.rangeData : []
    chartData.classAvgData = Array.isArray(data.classAvgData) ? data.classAvgData : []
    chartData.trendData = Array.isArray(data.trendData) ? data.trendData : []
    await nextTick()
    renderRangePieChart()
    renderClassBarChart()
    renderTrendLineChart()
  } catch (error) { /* 错误处理略 */ }
}

// =================== Echarts 渲染逻辑 ===================
const renderRangePieChart = () => {
  if (!rangePieRef.value) return
  if (!rangePieChart) rangePieChart = echarts.init(rangePieRef.value)
  rangePieChart.setOption({
    tooltip: { trigger: "item" },
    legend: { bottom: 0 },
    color: ['#67C23A', '#409EFF', '#E6A23C', '#F56C6C'],
    series: [{
      name: "成绩分布", type: "pie", radius: ["40%", "70%"],
      itemStyle: { borderRadius: 5, borderColor: '#fff', borderWidth: 2 },
      label: { show: true, formatter: "{b}\n{c}人 ({d}%)" },
      data: chartData.rangeData.map(item => ({ name: item.name, value: Number(item.value || 0) }))
    }]
  })
}

const renderClassBarChart = () => {
  if (!classBarRef.value) return
  if (!classBarChart) classBarChart = echarts.init(classBarRef.value)
  classBarChart.setOption({
    tooltip: { trigger: "axis" },
    grid: { left: "8%", right: "5%", bottom: "12%", top: "10%" },
    xAxis: { type: "category", axisLabel: { interval: 0, rotate: 20 }, data: chartData.classAvgData.map(item => item.name) },
    yAxis: { type: "value", min: 0, max: 100 },
    series: [{
      name: "平均分", type: "bar", barWidth: '40%',
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#83bff6' }, { offset: 1, color: '#188df0' }
        ]), borderRadius: [4, 4, 0, 0]
      },
      label: { show: true, position: "top", color: '#666' },
      data: chartData.classAvgData.map(item => Number(item.value || 0))
    }]
  })
}

const renderTrendLineChart = () => {
  if (!trendLineRef.value) return
  if (!trendLineChart) trendLineChart = echarts.init(trendLineRef.value)
  trendLineChart.setOption({
    tooltip: { trigger: "axis" },
    grid: { left: "5%", right: "4%", bottom: "10%", top: "10%" },
    xAxis: { type: "category", data: chartData.trendData.map(item => item.name) },
    yAxis: { type: "value", min: 0, max: 100 },
    series: [{
      name: "平均分趋势", type: "line", smooth: true, symbolSize: 8,
      itemStyle: { color: '#626aef' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(98, 106, 239, 0.4)' }, { offset: 1, color: 'rgba(98, 106, 239, 0.05)' }
        ])
      },
      label: { show: true },
      data: chartData.trendData.map(item => Number(item.value || 0))
    }]
  })
}

// =================== 操作及 AI 逻辑 ===================
const handleClassDiagnosis = async () => {
  if (!searchForm.tcclassid || !searchForm.tccourseid) {
    ElMessage.warning("请先在上方筛选框中精确选择具体的【班级】和【课程】！"); return;
  }
  classAiDialogVisible.value = true;
  isGeneratingClassAi.value = true;
  classAiReportText.value = "AI 教研室主任正在扫描全班成绩，寻找集体薄弱项，请稍候...";
  try {
    const res = await axios.get('/diagnosis/getClassAiReport', {
      params: { classId: searchForm.tcclassid, courseId: searchForm.tccourseid }
    });
    const resp = res?.data || {};
    if ((resp.code ?? resp._code) === 200) {
      classAiReportText.value = resp.data;
      ElMessage.success("智能数据洞察生成成功！");
    } else {
      classAiReportText.value = "分析失败：" + (resp.msg || "未知错误");
    }
  } catch (e) {
    classAiReportText.value = "无法连接大模型服务，请检查 Python 引擎与 Ollama 是否正常运行。";
  } finally {
    isGeneratingClassAi.value = false;
  }
}

const loadAllData = async () => {
  await Promise.all([
    loadTableData(),
    loadSummaryData(),
    loadChartData(),
    loadWeakPointData() // 加入知识点诊断请求
  ])
}

const handleSearch = async () => { pageIndex.value = 1; await loadAllData() }
const handleReset = async () => {
  searchForm.tctermid = null; searchForm.tcclassid = null; searchForm.tccourseid = null;
  pageIndex.value = 1; pageSize.value = 10; await loadAllData()
}
const handleCurrentChange = async (val) => { pageIndex.value = val; await loadTableData() }
const handleSizeChange = async (val) => { pageSize.value = val; pageIndex.value = 1; await loadTableData() }
const handleResize = () => { if (rangePieChart) rangePieChart.resize(); if (classBarChart) classBarChart.resize(); if (trendLineChart) trendLineChart.resize(); }

onMounted(async () => {
  await loadTaskList()
  await loadAllData()
  window.addEventListener("resize", handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize)
  if (rangePieChart) rangePieChart.dispose()
  if (classBarChart) classBarChart.dispose()
  if (trendLineChart) trendLineChart.dispose()
})
</script>

<style scoped>
/* =========== 补充完整的 CSS 样式 =========== */
.page-box {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);
}

.search-card,
.table-card,
.chart-card {
  margin-bottom: 16px;
  border-radius: 8px;
  border: none;
}

.card-header-title {
  font-weight: bold;
  font-size: 15px;
  color: #303133;
}

.statistics-tip {
  margin-top: 10px;
  padding: 8px 12px;
  background: #e1f3d8;
  color: #67c23a;
  font-size: 13px;
  border-radius: 4px;
  line-height: 1.5;
}

.summary-box {
  margin-bottom: 16px;
}

.summary-card {
  text-align: center;
  min-height: 90px;
  border-radius: 8px;
  border: none;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.summary-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

.summary-title {
  font-size: 13px;
  color: #909399;
  margin-bottom: 8px;
}

.summary-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.unit {
  font-size: 14px;
  font-weight: normal;
  margin-left: 2px;
}

/* 数值颜色辅助 */
.text-blue { color: #409EFF; }
.text-purple { color: #626aef; }
.text-green { color: #67C23A; }
.text-red { color: #F56C6C; }
.text-orange { color: #E6A23C; }
.text-white { color: #ffffff !important; }

/* 重点突出最弱知识点的渐变卡片 */
.highlight-card {
  background: linear-gradient(135deg, #ff7e5f 0%, #feb47b 100%);
  color: #fff;
}

.chart-row {
  margin-bottom: 16px;
}

.chart-box {
  width: 100%;
  height: 300px;
}

.trend-box {
  height: 350px;
}

.pager-box {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
