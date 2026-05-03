<template>
  <div class="page-box">
    <!-- 查询区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="班级 / 课程 / 教师"
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
          >
            <el-option
              v-for="item in termList"
              :key="item.yid"
              :label="item.yall"
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

        <el-form-item label="教师">
          <el-select
            v-model="searchForm.tcteacherid"
            placeholder="请选择教师"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in teacherList"
              :key="item.tid"
              :label="`${item.tname}${item.tno ? '（' + item.tno + '）' : ''}`"
              :value="item.tid"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增授课</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="index" label="#" width="60" align="center" />

        <el-table-column prop="tcid" label="授课ID" width="100" align="center" />

        <el-table-column prop="yall" label="学期" min-width="140" align="center" />

        <el-table-column prop="classname" label="班级" min-width="160" align="center" />

        <el-table-column prop="crname" label="课程" min-width="160" align="center" />

        <el-table-column prop="tname" label="教师" min-width="120" align="center" />

        <el-table-column prop="tno" label="教师工号" min-width="120" align="center" />

        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-box">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="pagination.pageIndex"
          :page-size="pagination.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="pagination.total"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <!-- 新增 / 编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formData.tcid && formData.tcid > 0 ? '编辑授课' : '新增授课'"
      width="600px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
      >
        <el-form-item label="学期" prop="tctermid">
          <el-select
            v-model="formData.tctermid"
            placeholder="请选择学期"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="item in termList"
              :key="item.yid"
              :label="item.yall"
              :value="item.yid"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="班级" prop="tcclassid">
          <el-select
            v-model="formData.tcclassid"
            placeholder="请选择班级"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="item in classList"
              :key="item.cid"
              :label="item.cname"
              :value="item.cid"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="课程" prop="tccourseid">
          <el-select
            v-model="formData.tccourseid"
            placeholder="请选择课程"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="item in courseList"
              :key="item.crid"
              :label="item.crname"
              :value="item.crid"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="教师" prop="tcteacherid">
          <el-select
            v-model="formData.tcteacherid"
            placeholder="请选择教师"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="item in teacherList"
              :key="item.tid"
              :label="`${item.tname}${item.tno ? '（' + item.tno + '）' : ''}`"
              :value="item.tid"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="handleSave">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import request from "@/utils/Axios"

const loading = ref(false)
const saveLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref()

const tableData = ref([])
const termList = ref([])
const classList = ref([])
const courseList = ref([])
const teacherList = ref([])

const searchForm = reactive({
  qkey: "",
  tctermid: null,
  tcclassid: null,
  tccourseid: null,
  tcteacherid: null
})

const pagination = reactive({
  pageIndex: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  tcid: 0,
  tctermid: null,
  tcclassid: null,
  tccourseid: null,
  tcteacherid: null
})

const rules = {
  tctermid: [{ required: true, message: "请选择学期", trigger: "change" }],
  tcclassid: [{ required: true, message: "请选择班级", trigger: "change" }],
  tccourseid: [{ required: true, message: "请选择课程", trigger: "change" }],
  tcteacherid: [{ required: true, message: "请选择教师", trigger: "change" }]
}

const resetFormData = () => {
  formData.tcid = 0
  formData.tctermid = null
  formData.tcclassid = null
  formData.tccourseid = null
  formData.tcteacherid = null
}

const normalizeIdValue = (val) => {
  if (val === "" || val === undefined || val === null) return null
  return Number(val)
}

const getListData = (res) => {
  if (!res || !res.data) return []
  return res.data.data || []
}

const getTotalData = (res) => {
  if (!res || !res.data) return 0
  return res.data.totalRecord || 0
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await request.get("/teaching/getlist", {
      params: {
        pageIndex: pagination.pageIndex,
        pageSize: pagination.pageSize,
        qkey: searchForm.qkey,
        tcTermId: searchForm.tctermid,
        tcClassId: searchForm.tcclassid,
        tcCourseId: searchForm.tccourseid,
        tcTeacherId: searchForm.tcteacherid
      }
    })

    if (res.data && (res.data.code === 200 || res.data._code === 200)) {
      tableData.value = res.data.data || []
      pagination.total = res.data.totalRecord || 0
    } else {
      ElMessage.error(res.data?.msg || res.data?._msg || "获取授课列表失败")
    }
  } catch (error) {
    console.error("获取授课列表失败：", error)
    ElMessage.error("获取授课列表失败")
  } finally {
    loading.value = false
  }
}

const loadTermList = async () => {
  try {
    const res = await request.get("/yearterm/getall")
    termList.value = res.data?.data || []
  } catch (error) {
    console.error("获取学期列表失败：", error)
    ElMessage.error("获取学期列表失败")
  }
}

const loadClassList = async () => {
  try {
    const res = await request.get("/classinfo/getall")
    classList.value = res.data?.data || []
  } catch (error) {
    console.error("获取班级列表失败：", error)
    ElMessage.error("获取班级列表失败")
  }
}

const loadCourseList = async () => {
  try {
    const res = await request.get("/courseinfo/getall")
    courseList.value = res.data?.data || []
  } catch (error) {
    console.error("获取课程列表失败：", error)
    ElMessage.error("获取课程列表失败")
  }
}

const loadTeacherList = async () => {
  try {
    const res = await request.get("/teacherinfo/getall")
    teacherList.value = res.data?.data || []
  } catch (error) {
    console.error("获取教师列表失败：", error)
    ElMessage.error("获取教师列表失败")
  }
}

const loadInitData = async () => {
  await Promise.all([
    loadTermList(),
    loadClassList(),
    loadCourseList(),
    loadTeacherList()
  ])
}

const handleSearch = () => {
  pagination.pageIndex = 1
  loadTableData()
}

const handleReset = () => {
  searchForm.qkey = ""
  searchForm.tctermid = null
  searchForm.tcclassid = null
  searchForm.tccourseid = null
  searchForm.tcteacherid = null
  pagination.pageIndex = 1
  loadTableData()
}

const handlePageChange = (page) => {
  pagination.pageIndex = page
  loadTableData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.pageIndex = 1
  loadTableData()
}

const handleAdd = async () => {
  resetFormData()
  dialogVisible.value = true
  await nextTick()
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleEdit = async (row) => {
  resetFormData()

  formData.tcid = row.tcid
  formData.tctermid = normalizeIdValue(row.tctermid)
  formData.tcclassid = normalizeIdValue(row.tcclassid)
  formData.tccourseid = normalizeIdValue(row.tccourseid)
  formData.tcteacherid = normalizeIdValue(row.tcteacherid)

  dialogVisible.value = true
  await nextTick()
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleSave = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    saveLoading.value = true
    try {
      const submitData = {
        tcid: formData.tcid,
        tctermid: formData.tctermid,
        tcclassid: formData.tcclassid,
        tccourseid: formData.tccourseid,
        tcteacherid: formData.tcteacherid
      }

      let res = null
      if (formData.tcid && formData.tcid > 0) {
        res = await request.post("/teaching/update", submitData)
      } else {
        res = await request.post("/teaching/add", submitData)
      }

      if (res.data && (res.data.code === 200 || res.data._code === 200)) {
        ElMessage.success(res.data.msg || res.data._msg || "保存成功")
        dialogVisible.value = false
        loadTableData()
      } else {
        ElMessage.error(res.data?.msg || res.data?._msg || "保存失败")
      }
    } catch (error) {
      console.error("保存授课失败：", error)
      ElMessage.error("保存授课失败")
    } finally {
      saveLoading.value = false
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定删除授课记录【${row.classname || ""} / ${row.crname || ""} / ${row.tname || ""}】吗？`,
      "删除确认",
      {
        type: "warning"
      }
    )

    const res = await request.post("/teaching/delete", null, {
      params: { id: row.tcid }
    })

    if (res.data && (res.data.code === 200 || res.data._code === 200)) {
      ElMessage.success(res.data.msg || res.data._msg || "删除成功")
      if (tableData.value.length === 1 && pagination.pageIndex > 1) {
        pagination.pageIndex -= 1
      }
      loadTableData()
    } else {
      ElMessage.error(res.data?.msg || res.data?._msg || "删除失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除授课失败：", error)
      ElMessage.error("删除授课失败")
    }
  }
}

onMounted(async () => {
  await loadInitData()
  loadTableData()
})
</script>

<style scoped>
.page-box {
  padding: 15px;
}

.search-card {
  margin-bottom: 15px;
}

.table-card {
  min-height: 500px;
}

.pager-box {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>
