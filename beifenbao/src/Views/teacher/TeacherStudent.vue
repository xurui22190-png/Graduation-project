<template>
  <div class="page-box">
    <!-- 查询区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="请输入学号/姓名"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="学期">
          <el-select
            v-model="searchForm.tctermid"
            placeholder="请选择学期"
            clearable
            filterable
            style="width: 180px"
            @change="handleTermChange"
          >
            <el-option
              v-for="item in termList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
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
            @change="handleClassChange"
          >
            <el-option
              v-for="item in classList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
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
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <span>授课学生列表</span>
      </template>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="sno" label="学号" min-width="140" />
        <el-table-column prop="sname" label="姓名" min-width="100" />
        <el-table-column prop="ssex" label="性别" min-width="80" />
        <el-table-column prop="cname" label="班级" min-width="140" />
        <el-table-column prop="mname" label="专业" min-width="160" />
        <el-table-column prop="stel" label="电话" min-width="140" />
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
import { ref, reactive, computed, onMounted } from "vue"
import axios from "@/utils/Axios"
import { ElMessage } from "element-plus"

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageIndex = ref(1)
const pageSize = ref(10)

const taskList = ref([])

const searchForm = reactive({
  qkey: "",
  tctermid: null,
  tcclassid: null,
  tccourseid: null
})

const buildUniqueOptions = (list, valueKey, labelKey) => {
  const map = new Map()
  ;(list || []).forEach((item) => {
    const value = item[valueKey]
    const label = item[labelKey]
    if (value !== null && value !== undefined && value !== "" && !map.has(value)) {
      map.set(value, {
        value,
        label
      })
    }
  })
  return Array.from(map.values())
}

const filteredTaskList = computed(() => {
  let arr = [...taskList.value]
  if (searchForm.tctermid) {
    arr = arr.filter((item) => Number(item.tctermid) === Number(searchForm.tctermid))
  }
  if (searchForm.tcclassid) {
    arr = arr.filter((item) => Number(item.tcclassid) === Number(searchForm.tcclassid))
  }
  return arr
})

const termList = computed(() => {
  return buildUniqueOptions(taskList.value, "tctermid", "yall")
})

const classList = computed(() => {
  let arr = [...taskList.value]
  if (searchForm.tctermid) {
    arr = arr.filter((item) => Number(item.tctermid) === Number(searchForm.tctermid))
  }
  return buildUniqueOptions(arr, "tcclassid", "cname")
})

const courseList = computed(() => {
  return buildUniqueOptions(filteredTaskList.value, "tccourseid", "crname")
})

const loadTaskList = async () => {
  try {
    const res = await axios.get("/teacherstudent/gettasklist")
    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    const msg = resp.msg ?? resp._msg ?? "获取授课任务失败"

    if (code === 200) {
      taskList.value = resp.data || []
    } else {
      ElMessage.error(msg)
    }
  } catch (error) {
    console.error("获取授课任务失败：", error)
    ElMessage.error("获取授课任务失败")
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/teacherstudent/getcoursestudents", {
      params: {
        pageIndex: pageIndex.value,
        pageSize: pageSize.value,
        qkey: searchForm.qkey,
        tctermid: searchForm.tctermid || "",
        tcclassid: searchForm.tcclassid || "",
        tccourseid: searchForm.tccourseid || ""
      }
    })

    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    const msg = resp.msg ?? resp._msg ?? "获取学生列表失败"

    if (code === 200) {
      tableData.value = resp.data || []
      total.value = resp.totalRecord || 0
    } else {
      ElMessage.error(msg)
    }
  } catch (error) {
    console.error("获取学生列表失败：", error)
    ElMessage.error("获取学生列表失败")
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
  searchForm.tctermid = null
  searchForm.tcclassid = null
  searchForm.tccourseid = null
  pageIndex.value = 1
  loadTableData()
}

const handleTermChange = () => {
  searchForm.tcclassid = null
  searchForm.tccourseid = null
}

const handleClassChange = () => {
  searchForm.tccourseid = null
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
  await loadTaskList()
  await loadTableData()
})
</script>

<style scoped>
.page-box {
  padding: 12px;
}

.search-card {
  margin-bottom: 12px;
}

.table-card {
  margin-bottom: 12px;
}

.pager-box {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
