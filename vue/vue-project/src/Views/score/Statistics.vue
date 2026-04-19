<template>
  <div class="page-box">
    <!-- 查询区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="课程 / 班级"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="学期">
          <el-select
            v-model="searchForm.yid"
            placeholder="请选择学期"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in termList"
              :key="item.yid ?? item.yId"
              :label="item.yall ?? item.yAll"
              :value="item.yid ?? item.yId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="院系">
          <el-select
            v-model="searchForm.collegeid"
            placeholder="请选择院系"
            clearable
            filterable
            style="width: 180px"
            @change="handleCollegeChange"
          >
            <el-option
              v-for="item in collegeList"
              :key="item.cid ?? item.cId"
              :label="item.cname ?? item.cName"
              :value="item.cid ?? item.cId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="专业">
          <el-select
            v-model="searchForm.majorid"
            placeholder="请选择专业"
            clearable
            filterable
            style="width: 180px"
            @change="handleMajorChange"
          >
            <el-option
              v-for="item in majorList"
              :key="item.mid ?? item.mId"
              :label="item.mname ?? item.mName"
              :value="item.mid ?? item.mId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select
            v-model="searchForm.cid"
            placeholder="请选择班级"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in classList"
              :key="item.cid ?? item.cId"
              :label="item.cname ?? item.cName"
              :value="item.cid ?? item.cId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="课程">
          <el-select
            v-model="searchForm.crid"
            placeholder="请选择课程"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in courseList"
              :key="item.crid ?? item.crId"
              :label="item.crname ?? item.crName"
              :value="item.crid ?? item.crId"
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
    <el-row :gutter="16" class="summary-row">
      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-title">成绩总数</div>
          <div class="summary-value">{{ summaryData.totalcount || 0 }}</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-title">学生人数</div>
          <div class="summary-value">{{ summaryData.studentcount || 0 }}</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-title">平均分</div>
          <div class="summary-value">{{ summaryData.avgscore || 0 }}</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-title">及格率</div>
          <div class="summary-value">{{ summaryData.passrate || 0 }}%</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-title">优秀率</div>
          <div class="summary-value">{{ summaryData.excellentrate || 0 }}%</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-title">最高分</div>
          <div class="summary-value">{{ summaryData.maxscore || 0 }}</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-title">最低分</div>
          <div class="summary-value">{{ summaryData.minscore || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">分数段饼图</div>
          </template>
          <div ref="rangePieRef" class="chart-box"></div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">课程平均分柱状图</div>
          </template>
          <div ref="courseBarRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">课程平均分折线图</div>
          </template>
          <div ref="courseLineRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">成绩趋势图</div>
          </template>
          <div ref="trendLineRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分数段统计 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">分数段统计</div>
      </template>

      <el-table :data="rangeData" border stripe>
        <el-table-column prop="rangename" label="分数段" align="center" />
        <el-table-column prop="num" label="人数" align="center" />
      </el-table>
    </el-card>

    <!-- 统计表格 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">课程成绩统计</div>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="crname" label="课程名称" min-width="160" />
        <el-table-column prop="cname" label="班级名称" min-width="160" />
        <el-table-column prop="totalnum" label="人数" width="90" align="center" />
        <el-table-column prop="avgscore" label="平均分" width="100" align="center" />
        <el-table-column prop="maxscore" label="最高分" width="100" align="center" />
        <el-table-column prop="minscore" label="最低分" width="100" align="center" />
        <el-table-column label="及格率" width="100" align="center">
          <template #default="scope">
            {{ scope.row.passrate }}%
          </template>
        </el-table-column>
        <el-table-column label="优秀率" width="100" align="center">
          <template #default="scope">
            {{ scope.row.excellentrate }}%
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-box">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="totalRecord"
          :current-page="searchForm.pageIndex"
          :page-size="searchForm.pageSize"
          :page-sizes="[10, 20, 30, 50]"
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

const termList = ref([])
const collegeList = ref([])
const majorList = ref([])
const classList = ref([])
const courseList = ref([])

const allMajorList = ref([])
const allClassList = ref([])

const rangeData = ref([])
const tableData = ref([])
const trendData = ref([])
const totalRecord = ref(0)

const summaryData = reactive({
  totalcount: 0,
  studentcount: 0,
  avgscore: 0,
  maxscore: 0,
  minscore: 0,
  passrate: 0,
  excellentrate: 0
})

const searchForm = reactive({
  qkey: "",
  yid: "",
  collegeid: "",
  majorid: "",
  cid: "",
  crid: "",
  pageIndex: 1,
  pageSize: 10
})

const rangePieRef = ref(null)
const courseBarRef = ref(null)
const courseLineRef = ref(null)
const trendLineRef = ref(null)

let rangePieChart = null
let courseBarChart = null
let courseLineChart = null
let trendLineChart = null

const isSuccess = (res) => {
  const code = res?.data?._code ?? res?.data?.code
  return Number(code) === 200
}

const getDataList = (res) => {
  return res?.data?.data || []
}

const resetSummaryData = () => {
  Object.assign(summaryData, {
    totalcount: 0,
    studentcount: 0,
    avgscore: 0,
    maxscore: 0,
    minscore: 0,
    passrate: 0,
    excellentrate: 0
  })
}

const loadTermList = async () => {
  try {
    const res = await axios.get("/yearterm/getall")
    termList.value = isSuccess(res) ? getDataList(res) : []
  } catch (error) {
    console.log("获取学期列表失败：", error)
    termList.value = []
  }
}

const loadCollegeList = async () => {
  try {
    const res = await axios.get("/collegeinfo/getall")
    collegeList.value = isSuccess(res) ? getDataList(res) : []
  } catch (error) {
    console.log("获取院系列表失败：", error)
    collegeList.value = []
  }
}

const loadMajorList = async () => {
  try {
    const res = await axios.get("/majorinfo/getall")
    if (isSuccess(res)) {
      allMajorList.value = getDataList(res)
      majorList.value = [...allMajorList.value]
    } else {
      allMajorList.value = []
      majorList.value = []
    }
  } catch (error) {
    console.log("获取专业列表失败：", error)
    allMajorList.value = []
    majorList.value = []
  }
}

const loadClassList = async () => {
  try {
    const res = await axios.get("/classinfo/getall")
    if (isSuccess(res)) {
      allClassList.value = getDataList(res)
      classList.value = [...allClassList.value]
    } else {
      allClassList.value = []
      classList.value = []
    }
  } catch (error) {
    console.log("获取班级列表失败：", error)
    allClassList.value = []
    classList.value = []
  }
}

const loadCourseList = async () => {
  try {
    const res = await axios.get("/courseinfo/getall")
    courseList.value = isSuccess(res) ? getDataList(res) : []
  } catch (error) {
    console.log("获取课程列表失败：", error)
    courseList.value = []
  }
}

const handleCollegeChange = () => {
  searchForm.majorid = ""
  searchForm.cid = ""

  if (!searchForm.collegeid) {
    majorList.value = [...allMajorList.value]
    classList.value = [...allClassList.value]
    return
  }

  majorList.value = allMajorList.value.filter(item => {
    const mcollegeid = item.mcollegeid ?? item.mCollegeId
    return String(mcollegeid) === String(searchForm.collegeid)
  })

  const majorIds = majorList.value.map(item => String(item.mid ?? item.mId))

  classList.value = allClassList.value.filter(item => {
    const cmajorid = item.cmajorid ?? item.cMajorId
    return majorIds.includes(String(cmajorid))
  })
}

const handleMajorChange = () => {
  searchForm.cid = ""

  if (!searchForm.majorid) {
    if (!searchForm.collegeid) {
      classList.value = [...allClassList.value]
      return
    }

    const majorIds = majorList.value.map(item => String(item.mid ?? item.mId))
    classList.value = allClassList.value.filter(item => {
      const cmajorid = item.cmajorid ?? item.cMajorId
      return majorIds.includes(String(cmajorid))
    })
    return
  }

  classList.value = allClassList.value.filter(item => {
    const cmajorid = item.cmajorid ?? item.cMajorId
    return String(cmajorid) === String(searchForm.majorid)
  })
}

const loadSummary = async () => {
  try {
    const res = await axios.get("/scorestats/summary", { params: searchForm })
    if (isSuccess(res)) {
      Object.assign(summaryData, {
        totalcount: res.data.data?.totalcount ?? 0,
        studentcount: res.data.data?.studentcount ?? 0,
        avgscore: res.data.data?.avgscore ?? 0,
        maxscore: res.data.data?.maxscore ?? 0,
        minscore: res.data.data?.minscore ?? 0,
        passrate: res.data.data?.passrate ?? 0,
        excellentrate: res.data.data?.excellentrate ?? 0
      })
    } else {
      resetSummaryData()
    }
  } catch (error) {
    console.log("获取汇总统计失败：", error)
    resetSummaryData()
  }
}

const loadRangeData = async () => {
  try {
    const res = await axios.get("/scorestats/range", { params: searchForm })
    rangeData.value = isSuccess(res) ? getDataList(res) : []
  } catch (error) {
    console.log("获取分数段统计失败：", error)
    rangeData.value = []
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/scorestats/list", { params: searchForm })
    if (isSuccess(res)) {
      tableData.value = res.data.data || []
      totalRecord.value = res.data.totalRecord || 0
    } else {
      tableData.value = []
      totalRecord.value = 0
    }
  } catch (error) {
    console.log("获取成绩统计列表失败：", error)
    tableData.value = []
    totalRecord.value = 0
    ElMessage.error("获取成绩统计列表失败")
  } finally {
    loading.value = false
  }
}

const loadTrendData = async () => {
  try {
    const res = await axios.get("/scorestats/trend", { params: searchForm })
    if (isSuccess(res)) {
      trendData.value = res.data.data || []
    } else {
      trendData.value = []
    }
  } catch (error) {
    console.log("获取趋势图数据失败：", error)
    trendData.value = []
  }
}

const initCharts = () => {
  if (rangePieRef.value && !rangePieChart) {
    rangePieChart = echarts.init(rangePieRef.value)
  }
  if (courseBarRef.value && !courseBarChart) {
    courseBarChart = echarts.init(courseBarRef.value)
  }
  if (courseLineRef.value && !courseLineChart) {
    courseLineChart = echarts.init(courseLineRef.value)
  }
  if (trendLineRef.value && !trendLineChart) {
    trendLineChart = echarts.init(trendLineRef.value)
  }
}

const renderRangePieChart = () => {
  if (!rangePieChart) return

  const chartData = (rangeData.value || []).map(item => ({
    name: item.rangename,
    value: item.num
  }))

  rangePieChart.setOption({
    tooltip: {
      trigger: "item"
    },
    legend: {
      bottom: 0
    },
    series: [
      {
        name: "分数段人数",
        type: "pie",
        radius: ["40%", "70%"],
        center: ["50%", "45%"],
        data: chartData,
        label: {
          formatter: "{b}: {c}"
        }
      }
    ]
  })
}

const renderCourseBarChart = () => {
  if (!courseBarChart) return

  const topList = (tableData.value || []).slice(0, 10)

  courseBarChart.setOption({
    tooltip: {
      trigger: "axis"
    },
    grid: {
      left: 40,
      right: 20,
      top: 30,
      bottom: 80
    },
    xAxis: {
      type: "category",
      data: topList.map(item => `${item.crname}-${item.cname}`),
      axisLabel: {
        rotate: 30
      }
    },
    yAxis: {
      type: "value",
      name: "平均分"
    },
    series: [
      {
        name: "平均分",
        type: "bar",
        data: topList.map(item => item.avgscore || 0),
        barMaxWidth: 40
      }
    ]
  })
}

const renderCourseLineChart = () => {
  if (!courseLineChart) return

  const topList = (tableData.value || []).slice(0, 10)

  courseLineChart.setOption({
    tooltip: {
      trigger: "axis"
    },
    grid: {
      left: 40,
      right: 20,
      top: 30,
      bottom: 80
    },
    xAxis: {
      type: "category",
      data: topList.map(item => `${item.crname}-${item.cname}`),
      axisLabel: {
        rotate: 30
      }
    },
    yAxis: {
      type: "value",
      name: "平均分"
    },
    series: [
      {
        name: "平均分",
        type: "line",
        smooth: true,
        data: topList.map(item => item.avgscore || 0)
      }
    ]
  })
}

const renderTrendLineChart = () => {
  if (!trendLineChart) return

  if (!trendData.value || trendData.value.length === 0) {
    trendLineChart.setOption({
      title: {
        text: "暂无趋势数据",
        left: "center",
        top: "middle",
        textStyle: {
          fontSize: 16,
          fontWeight: "normal",
          color: "#999"
        }
      },
      xAxis: {
        show: false,
        type: "category",
        data: []
      },
      yAxis: {
        show: false,
        type: "value"
      },
      series: []
    })
    return
  }

  trendLineChart.setOption({
    color: ["#5470C6", "#91CC75", "#FAC858"],
    tooltip: {
      trigger: "axis"
    },
    legend: {
      top: 10,
      data: ["平均分", "及格率", "优秀率"]
    },
    grid: {
      left: 50,
      right: 50,
      top: 60,
      bottom: 50
    },
    xAxis: {
      type: "category",
      data: trendData.value.map(item => item.termname),
      axisLabel: {
        interval: 0
      }
    },
    yAxis: [
      {
        type: "value",
        name: "平均分",
        min: 0,
        max: 100
      },
      {
        type: "value",
        name: "比率%",
        min: 0,
        max: 100
      }
    ],
    series: [
      {
        name: "平均分",
        type: "line",
        smooth: true,
        symbol: "circle",
        symbolSize: 8,
        label: {
          show: true,
          position: "top",
          formatter: "{c}"
        },
        lineStyle: {
          width: 3
        },
        data: trendData.value.map(item => item.avgscore || 0)
      },
      {
        name: "及格率",
        type: "line",
        smooth: true,
        yAxisIndex: 1,
        symbol: "circle",
        symbolSize: 8,
        label: {
          show: true,
          position: "top",
          formatter: "{c}%"
        },
        lineStyle: {
          width: 3
        },
        data: trendData.value.map(item => item.passrate || 0)
      },
      {
        name: "优秀率",
        type: "line",
        smooth: true,
        yAxisIndex: 1,
        symbol: "circle",
        symbolSize: 8,
        label: {
          show: true,
          position: "top",
          formatter: "{c}%"
        },
        lineStyle: {
          width: 3
        },
        data: trendData.value.map(item => item.excellentrate || 0)
      }
    ]
  })
}

const renderAllCharts = async () => {
  await nextTick()
  initCharts()
  renderRangePieChart()
  renderCourseBarChart()
  renderCourseLineChart()
  renderTrendLineChart()
}

const handleResize = () => {
 if (rangePieChart) {
  rangePieChart.resize()
}
if (courseBarChart) {
  courseBarChart.resize()
}
if (courseLineChart) {
  courseLineChart.resize()
}
if (trendLineChart) {
  trendLineChart.resize()
}
}

const loadAllData = async () => {
  await Promise.all([
    loadSummary(),
    loadRangeData(),
    loadTableData(),
    loadTrendData()
  ])
  await renderAllCharts()
}

const handleSearch = async () => {
  searchForm.pageIndex = 1
  await loadAllData()
}

const handleReset = async () => {
  searchForm.qkey = ""
  searchForm.yid = ""
  searchForm.collegeid = ""
  searchForm.majorid = ""
  searchForm.cid = ""
  searchForm.crid = ""
  searchForm.pageIndex = 1
  searchForm.pageSize = 10

  majorList.value = [...allMajorList.value]
  classList.value = [...allClassList.value]

  await loadAllData()
}

const handleCurrentChange = async (page) => {
  searchForm.pageIndex = page
  await loadTableData()
  await renderAllCharts()
}

const handleSizeChange = async (size) => {
  searchForm.pageSize = size
  searchForm.pageIndex = 1
  await loadTableData()
  await renderAllCharts()
}

onMounted(async () => {
  await Promise.all([
    loadTermList(),
    loadCollegeList(),
    loadMajorList(),
    loadClassList(),
    loadCourseList()
  ])

  await loadAllData()

  window.addEventListener("resize", handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize)

  rangePieChart?.dispose()
  courseBarChart?.dispose()
  courseLineChart?.dispose()
  trendLineChart?.dispose()

  rangePieChart = null
  courseBarChart = null
  courseLineChart = null
  trendLineChart = null
})
</script>

<style scoped>
.page-box {
  padding: 15px;
}

.search-card,
.table-card,
.chart-card {
  margin-bottom: 16px;
}

.summary-row,
.chart-row {
  margin-bottom: 16px;
}

.summary-card {
  margin-bottom: 16px;
}

.summary-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.summary-value {
  font-size: 26px;
  font-weight: 700;
  color: #303133;
}

.card-header {
  font-size: 16px;
  font-weight: 600;
}

.chart-box {
  width: 100%;
  height: 360px;
}

.pager-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
