<template>
  <div class="page-box">
    <!-- 查询区域 -->
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

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">课程数</div>
          <div class="stat-value">{{ statData.totalCount }}</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">平均分</div>
          <div class="stat-value">{{ statData.avgScore }}</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">最高分</div>
          <div class="stat-value">{{ statData.maxScore }}</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">及格数</div>
          <div class="stat-value">{{ statData.passCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>成绩分布</span>
          </template>
          <div ref="scorePieRef" class="chart-box"></div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>课程成绩对比</span>
          </template>
          <div ref="scoreBarRef" class="chart-box"></div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>学期成绩趋势</span>
          </template>
          <div ref="scoreLineRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 表格区域 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="index" label="#" width="60" />

        <el-table-column
          prop="yall"
          label="学期"
          min-width="140"
          show-overflow-tooltip
        />

        <el-table-column
          prop="crcode"
          label="课程编号"
          min-width="120"
          show-overflow-tooltip
        />

        <el-table-column
          prop="crname"
          label="课程名称"
          min-width="180"
          show-overflow-tooltip
        />

        <el-table-column prop="scscore" label="成绩" width="100">
          <template #default="{ row }">
            <el-tag :type="scoreTagType(row.scscore)">
              {{ formatScore(row.scscore) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="是否及格" width="100">
          <template #default="{ row }">
            <el-tag :type="Number(row.scscore) >= 60 ? 'success' : 'danger'">
              {{ Number(row.scscore) >= 60 ? "及格" : "不及格" }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="scstatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="Number(row.scstatus) === 1 ? 'success' : 'info'">
              {{ Number(row.scstatus) === 1 ? "已设置" : "未设置" }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="tname"
          label="任课教师"
          width="120"
          show-overflow-tooltip
        />
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
import axios from "@/utils/Axios"
import { ElMessage } from "element-plus"
import * as echarts from "echarts"

const loading = ref(false)
const tableData = ref([])
const termList = ref([])

const pageIndex = ref(1)
const pageSize = ref(10)
const total = ref(0)

const scorePieRef = ref(null)
const scoreBarRef = ref(null)
const scoreLineRef = ref(null)

let scorePieChart = null
let scoreBarChart = null
let scoreLineChart = null

const searchForm = reactive({
  qkey: "",
  termid: null
})

const statData = reactive({
  totalCount: 0,
  avgScore: 0,
  maxScore: 0,
  passCount: 0
})

const formatScore = (val) => {
  if (val === null || val === undefined || val === "") return "-"
  const num = Number(val)
  return Number.isNaN(num) ? val : num.toFixed(1)
}

const scoreTagType = (score) => {
  const num = Number(score)
  if (Number.isNaN(num)) return "info"
  if (num >= 90) return "success"
  if (num >= 80) return ""
  if (num >= 60) return "warning"
  return "danger"
}

const calcStatData = () => {
  const list = tableData.value || []
  statData.totalCount = list.length

  if (!list.length) {
    statData.avgScore = 0
    statData.maxScore = 0
    statData.passCount = 0
    return
  }

  const scoreList = list
    .map((item) => Number(item.scscore))
    .filter((item) => !Number.isNaN(item))

  if (!scoreList.length) {
    statData.avgScore = 0
    statData.maxScore = 0
    statData.passCount = 0
    return
  }

  const sum = scoreList.reduce((a, b) => a + b, 0)
  statData.avgScore = (sum / scoreList.length).toFixed(1)
  statData.maxScore = Math.max(...scoreList).toFixed(1)
  statData.passCount = scoreList.filter((item) => item >= 60).length
}

const initCharts = () => {
  if (scorePieRef.value && !scorePieChart) {
    scorePieChart = echarts.init(scorePieRef.value)
  }
  if (scoreBarRef.value && !scoreBarChart) {
    scoreBarChart = echarts.init(scoreBarRef.value)
  }
  if (scoreLineRef.value && !scoreLineChart) {
    scoreLineChart = echarts.init(scoreLineRef.value)
  }
}

const renderPieChart = () => {
  if (!scorePieChart) return

  const list = tableData.value || []

  let excellent = 0
  let good = 0
  let pass = 0
  let fail = 0

  list.forEach((item) => {
    const score = Number(item.scscore)
    if (Number.isNaN(score)) return

    if (score >= 90) {
      excellent++
    } else if (score >= 80) {
      good++
    } else if (score >= 60) {
      pass++
    } else {
      fail++
    }
  })

  scorePieChart.setOption({
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
        center: ["50%", "45%"],
        data: [
          { value: excellent, name: "优秀(90+)" },
          { value: good, name: "良好(80-89)" },
          { value: pass, name: "及格(60-79)" },
          { value: fail, name: "不及格(<60)" }
        ],
        label: {
          formatter: "{b}\n{c}"
        }
      }
    ]
  })
}

const renderBarChart = () => {
  if (!scoreBarChart) return

  const list = tableData.value || []
  const xData = list.map((item) => item.crname || "-")
  const yData = list.map((item) => {
    const score = Number(item.scscore)
    return Number.isNaN(score) ? 0 : score
  })

  scoreBarChart.setOption({
    tooltip: {
      trigger: "axis"
    },
    grid: {
      left: 40,
      right: 20,
      top: 30,
      bottom: 60
    },
    xAxis: {
      type: "category",
      data: xData,
      axisLabel: {
        interval: 0,
        rotate: 30
      }
    },
    yAxis: {
      type: "value",
      min: 0,
      max: 100
    },
    series: [
      {
        name: "成绩",
        type: "bar",
        data: yData,
        barMaxWidth: 40,
        label: {
          show: true,
          position: "top"
        }
      }
    ]
  })
}

const renderLineChart = () => {
  if (!scoreLineChart) return

  const list = tableData.value || []
  const termMap = {}

  list.forEach((item) => {
    const termName = item.yall || "-"
    const score = Number(item.scscore)
    if (Number.isNaN(score)) return

    if (!termMap[termName]) {
      termMap[termName] = []
    }
    termMap[termName].push(score)
  })

  const xData = Object.keys(termMap)
  const yData = xData.map((termName) => {
    const arr = termMap[termName]
    const sum = arr.reduce((a, b) => a + b, 0)
    return Number((sum / arr.length).toFixed(1))
  })

  scoreLineChart.setOption({
    tooltip: {
      trigger: "axis"
    },
    grid: {
      left: 40,
      right: 20,
      top: 30,
      bottom: 40
    },
    xAxis: {
      type: "category",
      data: xData
    },
    yAxis: {
      type: "value",
      min: 0,
      max: 100
    },
    series: [
      {
        name: "平均成绩",
        type: "line",
        data: yData,
        smooth: true,
        label: {
          show: true
        }
      }
    ]
  })
}

const renderCharts = async () => {
  await nextTick()
  initCharts()
  renderPieChart()
  renderBarChart()
  renderLineChart()
}

const handleResize = () => {
  if (scorePieChart) {
    scorePieChart.resize()
  }
  if (scoreBarChart) {
    scoreBarChart.resize()
  }
  if (scoreLineChart) {
    scoreLineChart.resize()
  }
}

const loadTermList = async () => {
  try {
    const res = await axios.get("/yearterm/getall")
    const result = res.data

    if (result && (result.code === 200 || result._code === 200)) {
      termList.value = result.data || []
    } else {
      termList.value = []
    }
  } catch (error) {
    console.error("获取学期列表失败：", error)
    termList.value = []
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/student/score/getlist", {
      params: {
        pageIndex: pageIndex.value,
        pageSize: pageSize.value,
        qkey: searchForm.qkey,
        termid: searchForm.termid
      }
    })

    const result = res.data

    if (result && (result._code === 200 || result.code === 200)) {
      tableData.value = result.data || []
      total.value = Number(result.totalRecord || 0)
      calcStatData()
      await renderCharts()
    } else {
      tableData.value = []
      total.value = 0
      calcStatData()
      await renderCharts()
      ElMessage.error(result?._msg || result?.msg || "获取成绩列表失败")
    }
  } catch (error) {
    console.error("获取成绩列表失败：", error)
    tableData.value = []
    total.value = 0
    calcStatData()
    await renderCharts()
    ElMessage.error("获取成绩列表失败")
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pageIndex.value = 1
  loadTableData()
}

const handleReset = () => {
  searchForm.qkey = ""
  searchForm.termid = null
  pageIndex.value = 1
  loadTableData()
}

const handleCurrentChange = (val) => {
  pageIndex.value = val
  loadTableData()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  pageIndex.value = 1
  loadTableData()
}

onMounted(async () => {
  window.addEventListener("resize", handleResize)
  await loadTermList()
  await loadTableData()
})

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize)

  if (scorePieChart) {
    scorePieChart.dispose()
  }
  if (scoreBarChart) {
    scoreBarChart.dispose()
  }
  if (scoreLineChart) {
    scoreLineChart.dispose()
  }

  scorePieChart = null
  scoreBarChart = null
  scoreLineChart = null
})
</script>

<style scoped>
.page-box {
  padding: 16px;
}

.search-card,
.table-card {
  margin-bottom: 16px;
}

.stat-row {
  margin-bottom: 16px;
}

.stat-card {
  text-align: center;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.chart-row {
  margin-bottom: 16px;
}

.chart-card {
  margin-bottom: 16px;
}

.chart-box {
  width: 100%;
  height: 320px;
}

.pager-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
