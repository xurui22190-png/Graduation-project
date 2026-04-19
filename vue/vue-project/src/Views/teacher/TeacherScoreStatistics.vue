<template>
  <div class="page-box">
    <!-- 查询区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="学期">
          <el-select
            v-model="searchForm.tctermid"
            placeholder="请选择学期"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in termList"
              :key="item.yid"
              :label="item.yall || `${item.yyear || ''}${item.yterm || ''}`"
              :value="item.yid"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select
            v-model="searchForm.tcclassid"
            placeholder="请选择班级"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in classList"
              :key="item.cid"
              :label="item.cname"
              :value="item.cid"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="课程">
          <el-select
            v-model="searchForm.tccourseid"
            placeholder="请选择课程"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in courseList"
              :key="item.crid"
              :label="item.crname"
              :value="item.crid"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 汇总卡片 -->
    <div class="summary-box">
      <el-row :gutter="16">
        <el-col :span="4">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">学生人数</div>
            <div class="summary-value">{{ summary.totalCount }}</div>
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">平均分</div>
            <div class="summary-value">{{ summary.avgScore }}</div>
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">最高分</div>
            <div class="summary-value">{{ summary.maxScore }}</div>
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">最低分</div>
            <div class="summary-value">{{ summary.minScore }}</div>
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">及格率</div>
            <div class="summary-value">{{ summary.passRate }}%</div>
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover" class="summary-card">
            <div class="summary-title">优秀率</div>
            <div class="summary-value">{{ summary.goodRate }}%</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>成绩分布统计</span>
          </template>
          <div ref="rangePieRef" class="chart-box"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>班级平均分统计</span>
          </template>
          <div ref="classBarRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="24">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>学期成绩趋势</span>
          </template>
          <div ref="trendLineRef" class="chart-box trend-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 明细表 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <span>成绩明细</span>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="sno" label="学号" min-width="130" />
        <el-table-column prop="sname" label="姓名" min-width="100" />
        <el-table-column prop="ssex" label="性别" width="80" align="center" />
        <el-table-column prop="classname" label="班级" min-width="140" />
        <el-table-column prop="crname" label="课程" min-width="140" />
        <el-table-column prop="yall" label="学期" min-width="140" />
        <el-table-column prop="scscore" label="成绩" width="100" align="center" />
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, onBeforeUnmount } from "vue"
import * as echarts from "echarts"
import axios from "@/utils/Axios"
import { ElMessage } from "element-plus"

const loading = ref(false)

const pageIndex = ref(1)
const pageSize = ref(10)
const total = ref(0)

const tableData = ref([])
const termList = ref([])
const classList = ref([])
const courseList = ref([])

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
  goodRate: 0
})

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

const loadTermList = async () => {
  try {
    const res = await axios.get("/teacher/scorestatistics/termlist")
    termList.value = Array.isArray(res.data?.data) ? res.data.data : []
  } catch (error) {
    console.error("获取学期列表失败：", error)
    termList.value = []
  }
}

const loadClassList = async () => {
  try {
    const res = await axios.get("/teacher/scorestatistics/classlist")
    classList.value = Array.isArray(res.data?.data) ? res.data.data : []
  } catch (error) {
    console.error("获取班级列表失败：", error)
    classList.value = []
  }
}

const loadCourseList = async () => {
  try {
    const res = await axios.get("/teacher/scorestatistics/courselist")
    courseList.value = Array.isArray(res.data?.data) ? res.data.data : []
  } catch (error) {
    console.error("获取课程列表失败：", error)
    courseList.value = []
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/teacher/scorestatistics/getlist", {
      params: {
        pageIndex: pageIndex.value,
        pageSize: pageSize.value,
        tctermid: searchForm.tctermid || 0,
        tcclassid: searchForm.tcclassid || 0,
        tccourseid: searchForm.tccourseid || 0
      }
    })

    const result = res.data || {}
    tableData.value = Array.isArray(result.data) ? result.data : []
    total.value = Number(result.totalRecord || 0)
  } catch (error) {
    console.error("获取成绩明细失败：", error)
    ElMessage.error("获取成绩明细失败")
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const loadSummaryData = async () => {
  try {
    const res = await axios.get("/teacher/scorestatistics/summary", {
      params: {
        tctermid: searchForm.tctermid || 0,
        tcclassid: searchForm.tcclassid || 0,
        tccourseid: searchForm.tccourseid || 0
      }
    })

    const data = res.data?.data || {}

    summary.totalCount = Number(data.totalCount || 0)
    summary.avgScore = fixNumber(data.avgScore)
    summary.maxScore = fixNumber(data.maxScore)
    summary.minScore = fixNumber(data.minScore)
    summary.passRate = fixNumber(data.passRate)
    summary.goodRate = fixNumber(data.goodRate)
  } catch (error) {
    console.error("获取统计汇总失败：", error)
    summary.totalCount = 0
    summary.avgScore = 0
    summary.maxScore = 0
    summary.minScore = 0
    summary.passRate = 0
    summary.goodRate = 0
  }
}

const loadChartData = async () => {
  try {
    const res = await axios.get("/teacher/scorestatistics/chart", {
      params: {
        tctermid: searchForm.tctermid || 0,
        tcclassid: searchForm.tcclassid || 0,
        tccourseid: searchForm.tccourseid || 0
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
  } catch (error) {
    console.error("获取图表数据失败：", error)
    chartData.rangeData = []
    chartData.classAvgData = []
    chartData.trendData = []

    await nextTick()
    renderRangePieChart()
    renderClassBarChart()
    renderTrendLineChart()
  }
}

const renderRangePieChart = () => {
  if (!rangePieRef.value) return

  if (!rangePieChart) {
    rangePieChart = echarts.init(rangePieRef.value)
  }

  rangePieChart.setOption({
    tooltip: {
      trigger: "item"
    },
    legend: {
      bottom: 0
    },
    series: [
      {
        name: "成绩分布",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        label: {
          show: true,
          formatter: "{b}\n{c}"
        },
        data: chartData.rangeData.map(item => ({
          name: item.name,
          value: Number(item.value || 0)
        }))
      }
    ]
  })
}

const renderClassBarChart = () => {
  if (!classBarRef.value) return

  if (!classBarChart) {
    classBarChart = echarts.init(classBarRef.value)
  }

  classBarChart.setOption({
    tooltip: {
      trigger: "axis"
    },
    grid: {
      left: "8%",
      right: "5%",
      bottom: "12%",
      top: "10%"
    },
    xAxis: {
      type: "category",
      axisLabel: {
        interval: 0,
        rotate: 20
      },
      data: chartData.classAvgData.map(item => item.name)
    },
    yAxis: {
      type: "value",
      min: 0,
      max: 100
    },
    series: [
      {
        name: "平均分",
        type: "bar",
        label: {
          show: true,
          position: "top"
        },
        data: chartData.classAvgData.map(item => Number(item.value || 0))
      }
    ]
  })
}

const renderTrendLineChart = () => {
  if (!trendLineRef.value) return

  if (!trendLineChart) {
    trendLineChart = echarts.init(trendLineRef.value)
  }

  trendLineChart.setOption({
    tooltip: {
      trigger: "axis"
    },
    grid: {
      left: "5%",
      right: "4%",
      bottom: "10%",
      top: "10%"
    },
    xAxis: {
      type: "category",
      data: chartData.trendData.map(item => item.name)
    },
    yAxis: {
      type: "value",
      min: 0,
      max: 100
    },
    series: [
      {
        name: "平均分趋势",
        type: "line",
        smooth: true,
        label: {
          show: true
        },
        areaStyle: {},
        data: chartData.trendData.map(item => Number(item.value || 0))
      }
    ]
  })
}

const loadAllData = async () => {
  await Promise.all([
    loadTableData(),
    loadSummaryData(),
    loadChartData()
  ])
}

const handleSearch = async () => {
  pageIndex.value = 1
  await loadAllData()
}

const handleReset = async () => {
  searchForm.tctermid = null
  searchForm.tcclassid = null
  searchForm.tccourseid = null
  pageIndex.value = 1
  pageSize.value = 10
  await loadAllData()
}

const handleCurrentChange = async (val) => {
  pageIndex.value = val
  await loadTableData()
}

const handleSizeChange = async (val) => {
  pageSize.value = val
  pageIndex.value = 1
  await loadTableData()
}

const handleResize = () => {
  if (rangePieChart) {
    rangePieChart.resize()
  }
  if (classBarChart) {
    classBarChart.resize()
  }
  if (trendLineChart) {
    trendLineChart.resize()
  }
}

onMounted(async () => {
  await Promise.all([
    loadTermList(),
    loadClassList(),
    loadCourseList()
  ])

  await loadAllData()

  window.addEventListener("resize", handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize)

  if (rangePieChart) {
    rangePieChart.dispose()
  }
  if (classBarChart) {
    classBarChart.dispose()
  }
  if (trendLineChart) {
    trendLineChart.dispose()
  }

  rangePieChart = null
  classBarChart = null
  trendLineChart = null
})
</script>

<style scoped>
.page-box {
  padding: 16px;
}

.search-card,
.table-card,
.chart-card {
  margin-bottom: 16px;
}

.summary-box {
  margin-bottom: 16px;
}

.summary-card {
  text-align: center;
  min-height: 100px;
}

.summary-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 12px;
}

.summary-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.chart-row {
  margin-bottom: 16px;
}

.chart-box {
  width: 100%;
  height: 340px;
}

.trend-box {
  height: 360px;
}

.pager-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
