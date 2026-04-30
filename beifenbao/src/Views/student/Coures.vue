<template>
  <div class="page-box">
    <div class="page-title">我的课程</div>

    <el-card shadow="never" class="search-card">
      <el-form :inline="true" class="search-form">
        <el-form-item label="课程关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="请输入课程名称或课程编号"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="学期">
          <el-select
            v-model="searchForm.ytid"
            placeholder="请选择学期"
            clearable
            style="width: 220px"
          >
            <el-option
              v-for="item in termList"
              :key="item.yId ?? item.ytid"
              :label="item.yAll ?? item.ytname"
              :value="item.yId ?? item.ytid"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="crcode" label="课程编号" min-width="120" align="center" />
        <el-table-column prop="crname" label="课程名称" min-width="180" />
        <el-table-column prop="crcredit" label="学分" width="80" align="center" />
        <el-table-column prop="crperiod" label="课时" width="80" align="center" />
        <el-table-column prop="crtype" label="课程类型" width="100" align="center" />
        <el-table-column prop="crexamtype" label="考核方式" width="100" align="center" />
        <el-table-column prop="tname" label="任课教师" width="120" align="center" />
        <el-table-column prop="ytname" label="学期" min-width="140" align="center" />
        <el-table-column prop="cname" label="班级" min-width="140" align="center" />
      </el-table>

      <div class="pager-box">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="pagination.pageIndex"
          :page-size="pagination.pageSize"
          :page-sizes="[10, 15, 20, 30]"
          :total="pagination.total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue"
import { ElMessage } from "element-plus"
import request from "@/utils/Axios"

const loading = ref(false)
const tableData = ref([])
const termList = ref([])

const searchForm = reactive({
  qkey: "",
  ytid: null
})

const pagination = reactive({
  pageIndex: 1,
  pageSize: 10,
  total: 0
})

const loadTermList = async () => {
  try {
    const res = await request.get("/yearterm/getall")
    console.log("学期列表返回：", res.data)

    let rawList = []

    if (res.data && res.data._code === 200) {
      rawList = Array.isArray(res.data.data) ? res.data.data : []
    } else if (Array.isArray(res.data)) {
      rawList = res.data
    } else if (res.data && Array.isArray(res.data.data)) {
      rawList = res.data.data
    }

    termList.value = rawList
      .map(item => ({
        ytid: item.ytid ?? item.yId ?? item.id ?? null,
        ytname: item.ytname ?? item.yAll ?? item.name ?? ""
      }))
      .filter(item => item.ytid !== null && item.ytid !== undefined && item.ytname !== "")
  } catch (error) {
    console.error("获取学期列表失败：", error)
    termList.value = []
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await request.get("/student/course/getlist", {
      params: {
        pageIndex: pagination.pageIndex,
        pageSize: pagination.pageSize,
        qkey: searchForm.qkey,
        ytid: searchForm.ytid
      }
    })

    console.log("学生课程返回：", res.data)

    if (res.data && res.data._code === 200) {
      tableData.value = res.data.data || []
      pagination.total = res.data.totalRecord || 0
    } else {
      tableData.value = []
      pagination.total = 0
      ElMessage.error(res.data?._msg || "获取课程列表失败")
    }
  } catch (error) {
    console.error("获取课程列表失败：", error)
    tableData.value = []
    pagination.total = 0
    ElMessage.error("获取课程列表失败")
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageIndex = 1
  loadTableData()
}

const handleReset = () => {
  searchForm.qkey = ""
  searchForm.ytid = null
  pagination.pageIndex = 1
  loadTableData()
}

const handleCurrentChange = (page) => {
  pagination.pageIndex = page
  loadTableData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.pageIndex = 1
  loadTableData()
}

onMounted(async () => {
  await loadTermList()
  await loadTableData()
})
</script>

<style scoped>
.page-box {
  padding: 15px;
}

.page-title {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
}

.search-card {
  margin-bottom: 15px;
}

.table-card {
  min-height: 400px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}

.pager-box {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>
