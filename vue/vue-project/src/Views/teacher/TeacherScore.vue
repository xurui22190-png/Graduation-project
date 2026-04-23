<template>
  <div class="page-box">
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
            v-model="searchForm.sctermid"
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
            v-model="searchForm.scclassid"
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
            v-model="searchForm.sccourseid"
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
        <el-form-item label="录入状态">
  <el-select
    v-model="searchForm.scstatus"
    placeholder="请选择状态"
    clearable
    style="width: 150px"
  >
    <el-option label="全部" :value="null" />
    <el-option label="已录入" :value="1" />
    <el-option label="未录入" :value="0" />
  </el-select>
</el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleOpenEntryDialog">成绩录入</el-button>
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
        <el-table-column prop="sno" label="学号" min-width="120" />
        <el-table-column prop="sname" label="姓名" min-width="100" />
        <el-table-column prop="cname" label="班级" min-width="140" />
        <el-table-column prop="crname" label="课程" min-width="140" />
        <el-table-column prop="yall" label="学期" min-width="140" />
        <el-table-column prop="tname" label="教师" min-width="100" />
        <el-table-column prop="scscore" label="成绩" min-width="90" />
        <el-table-column label="状态" min-width="90">
          <template #default="scope">
            <el-tag v-if="scope.row.scstatus === 1" type="success">已录入</el-tag>
            <el-tag v-else type="info">未录入</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sccreatedate" label="登记时间" min-width="180" />
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
      v-model="dialogVisible"
      title="成绩录入"
      width="980px"
      destroy-on-close
    >
      <el-form :inline="true" :model="entryForm" class="entry-form">
        <el-form-item label="学期">
          <el-select
            v-model="entryForm.sctermid"
            placeholder="请选择学期"
            clearable
            filterable
            style="width: 180px"
            @change="handleEntryTermChange"
          >
            <el-option
              v-for="item in entryTermList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select
            v-model="entryForm.scclassid"
            placeholder="请选择班级"
            clearable
            filterable
            style="width: 180px"
            @change="handleEntryClassChange"
          >
            <el-option
              v-for="item in entryClassList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="课程">
          <el-select
            v-model="entryForm.sccourseid"
            placeholder="请选择课程"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in entryCourseList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="loadStudentScoreList">加载学生</el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="studentLoading"
        :data="studentTableData"
        border
        stripe
        max-height="420"
      >
        <el-table-column prop="sno" label="学号" min-width="120" />
        <el-table-column prop="sname" label="姓名" min-width="100" />
        <el-table-column prop="cname" label="班级" min-width="140" />
        <el-table-column label="成绩" min-width="140">
          <template #default="scope">
            <el-input-number
              v-model="scope.row.scscore"
              :min="0"
              :max="100"
              :precision="2"
              controls-position="right"
              style="width: 120px"
            />
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="120">
          <template #default="scope">
            <el-select v-model="scope.row.scstatus" style="width: 100px">
              <el-option label="未录入" :value="0" />
              <el-option label="已录入" :value="1" />
            </el-select>
          </template>
        </el-table-column>
      </el-table>

      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleSaveScore">保存成绩</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from "vue"
import axios from "@/utils/Axios"
import { ElMessage } from "element-plus"

const loading = ref(false)
const studentLoading = ref(false)
const dialogVisible = ref(false)

const tableData = ref([])
const total = ref(0)
const pageIndex = ref(1)
const pageSize = ref(10)

const taskList = ref([])
const studentTableData = ref([])

const searchForm = reactive({
  qkey: "",
  sctermid: null,
  scclassid: null,
  sccourseid: null,
  scstatus: null
})

const entryForm = reactive({
  sctermid: null,
  scclassid: null,
  sccourseid: null
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
  if (searchForm.sctermid) {
    arr = arr.filter((item) => item.tctermid === searchForm.sctermid)
  }
  if (searchForm.scclassid) {
    arr = arr.filter((item) => item.tcclassid === searchForm.scclassid)
  }
  return arr
})

const termList = computed(() => {
  return buildUniqueOptions(taskList.value, "tctermid", "yall")
})

const classList = computed(() => {
  let arr = [...taskList.value]
  if (searchForm.sctermid) {
    arr = arr.filter((item) => item.tctermid === searchForm.sctermid)
  }
  return buildUniqueOptions(arr, "tcclassid", "cname")
})

const courseList = computed(() => {
  return buildUniqueOptions(filteredTaskList.value, "tccourseid", "crname")
})

const entryTermList = computed(() => {
  return buildUniqueOptions(taskList.value, "tctermid", "yall")
})

const entryClassList = computed(() => {
  let arr = [...taskList.value]
  if (entryForm.sctermid) {
    arr = arr.filter((item) => item.tctermid === entryForm.sctermid)
  }
  return buildUniqueOptions(arr, "tcclassid", "cname")
})

const entryCourseList = computed(() => {
  let arr = [...taskList.value]
  if (entryForm.sctermid) {
    arr = arr.filter((item) => item.tctermid === entryForm.sctermid)
  }
  if (entryForm.scclassid) {
    arr = arr.filter((item) => item.tcclassid === entryForm.scclassid)
  }
  return buildUniqueOptions(arr, "tccourseid", "crname")
})

const loadTaskList = async () => {
  try {
    const res = await axios.get("/teacherscore/gettasklist")
    if (res.data && (res.data.code === 200 || res.data._code === 200)) {
      taskList.value = res.data.data || []
    } else {
      ElMessage.error(res.data?.msg || res.data?._msg || "获取授课任务失败")
    }
  } catch (error) {
    console.error("获取授课任务失败：", error)
    ElMessage.error("获取授课任务失败")
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/teacherstudent/getlist", {
  params: {
    pageIndex: pageIndex.value,
    pageSize: pageSize.value,
    qkey: searchForm.qkey,
    sctermid: searchForm.sctermid || "",
    scclassid: searchForm.scclassid || "",
    sccourseid: searchForm.sccourseid || "",
    scstatus: searchForm.scstatus
  }
})

    if (res.data && res.data._code === 200) {
      tableData.value = res.data.data || []
      total.value = res.data.totalRecord || 0
    } else {
      ElMessage.error(res.data?._msg || "获取成绩列表失败")
    }
  } catch (error) {
    console.error("获取成绩列表失败：", error)
    ElMessage.error("获取成绩列表失败")
  } finally {
    loading.value = false
  }
}

const loadStudentScoreList = async () => {
  if (!entryForm.sctermid || !entryForm.scclassid || !entryForm.sccourseid) {
    ElMessage.warning("请先选择学期、班级和课程")
    return
  }

  studentLoading.value = true
  try {
    const res = await axios.get("/teacherscore/getstudents", {
      params: {
        sctermid: entryForm.sctermid,
        scclassid: entryForm.scclassid,
        sccourseid: entryForm.sccourseid
      }
    })

    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    const msg = resp.msg ?? resp._msg ?? "获取学生成绩列表失败"
    const list = Array.isArray(resp.data) ? resp.data : []

    if (code === 200) {
      studentTableData.value = list
  .filter((item) => item && typeof item === "object")
  .map((item) => ({
    sid: item.sid ?? null,
    sno: item.sno ?? "",
    sname: item.sname ?? "",
    sclassid: item.sclassid ?? null,
    cname: item.cname ?? "",
    scid: item.scid ?? null,
    scscore: item.scscore ?? null,
    scstatus: item.scstatus ?? 0
  }))
    } else {
      ElMessage.error(msg)
    }
  } catch (error) {
    console.error("获取学生成绩列表失败：", error)
    ElMessage.error("获取学生成绩列表失败")
  } finally {
    studentLoading.value = false
  }
}

const handleSearch = () => {
  pageIndex.value = 1
  loadTableData()
}

const handleReset = () => {
  searchForm.qkey = ""
  searchForm.sctermid = null
  searchForm.scclassid = null
  searchForm.sccourseid = null
  searchForm.scstatus = null
  pageIndex.value = 1
  loadTableData()
}

const handleTermChange = () => {
  searchForm.scclassid = null
  searchForm.sccourseid = null
}

const handleClassChange = () => {
  searchForm.sccourseid = null
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

const handleOpenEntryDialog = () => {
  entryForm.sctermid = searchForm.sctermid || null
  entryForm.scclassid = searchForm.scclassid || null
  entryForm.sccourseid = searchForm.sccourseid || null
  studentTableData.value = []
  dialogVisible.value = true
}

const handleEntryTermChange = () => {
  entryForm.scclassid = null
  entryForm.sccourseid = null
  studentTableData.value = []
}

const handleEntryClassChange = () => {
  entryForm.sccourseid = null
  studentTableData.value = []
}

const handleSaveScore = async () => {
  if (!entryForm.sctermid || !entryForm.scclassid || !entryForm.sccourseid) {
    ElMessage.warning("请先选择学期、班级和课程")
    return
  }

  if (!studentTableData.value || studentTableData.value.length === 0) {
    ElMessage.warning("请先加载学生数据")
    return
  }

  const scoreList = studentTableData.value
    .filter((item) => item && item.sid)
    .map((item) => {
      let scoreValue = item.scscore

      if (scoreValue === "" || scoreValue === undefined) {
        scoreValue = null
      }

      if (scoreValue !== null) {
        scoreValue = Number(scoreValue)
      }

      return {
        sid: item.sid,
        scid: item.scid ?? null,
        scscore: scoreValue,
        scstatus: scoreValue === null ? 0 : 1
      }
    })

  try {
    studentLoading.value = true

    const res = await axios.post("/teacherstudent/savescores", {
      tctermid: entryForm.sctermid,
      tcclassid: entryForm.scclassid,
      tccourseid: entryForm.sccourseid,
      scoreList
    })

    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    const msg = resp.msg ?? resp._msg ?? "保存失败"

    if (code === 200) {
      ElMessage.success(msg || "保存成功")
      await loadStudentScoreList()
      await loadTableData()
      dialogVisible.value = false
    } else {
      ElMessage.error(msg)
    }
  } catch (error) {
    console.error("保存成绩失败：", error)
    ElMessage.error("保存成绩失败")
  } finally {
    studentLoading.value = false
  }
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

.entry-form {
  margin-bottom: 16px;
}
</style>
