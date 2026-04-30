<template>
  <div class="page-box">
    <div class="part-hed">
      <div class="part-hed-tit">成绩管理</div>
    </div>

    <!-- 查询区域 -->
    <el-card shadow="never" class="query-card">
      <el-form :inline="true" :model="searchForm" class="query-form">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="请输入学号/姓名"
            clearable
            style="width: 180px"
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
          >
            <el-option
              v-for="item in termList"
              :key="item.yid ?? item.yId"
              :label="item.yall ?? item.yAll"
              :value="item.yid ?? item.yId"
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
            v-model="searchForm.sccourseid"
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

        <el-form-item label="教师">
          <el-select
            v-model="searchForm.scteacherid"
            placeholder="请选择教师"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in teacherList"
              :key="item.tid ?? item.tId"
              :label="item.tname ?? item.tName"
              :value="item.tid ?? item.tId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="状态">
          <el-select
            v-model="searchForm.scstatus"
            placeholder="请选择状态"
            clearable
            style="width: 140px"
          >
            <el-option label="未设置" :value="0" />
            <el-option label="已设置" :value="1" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增成绩</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="listData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="scid" label="成绩ID" width="90" align="center" />
        <el-table-column prop="sno" label="学号" min-width="120" align="center" />
        <el-table-column prop="sname" label="姓名" min-width="100" align="center" />
        <el-table-column prop="classname" label="班级" min-width="140" align="center" />
        <el-table-column prop="yall" label="学期" min-width="130" align="center" />
        <el-table-column prop="crname" label="课程" min-width="150" align="center" />
        <el-table-column prop="tname" label="教师" min-width="100" align="center" />
        <el-table-column prop="scscore" label="分数" width="100" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="Number(scope.row.scstatus) === 1 ? 'success' : 'info'">
              {{ Number(scope.row.scstatus) === 1 ? "已设置" : "未设置" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sccreatedate" label="登记时间" min-width="170" align="center" />

        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-box">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="pagination.currentPage"
          :page-size="pagination.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="pagination.total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="680px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学生" prop="scstudentid">
              <el-select
                v-model="formData.scstudentid"
                placeholder="请选择学生"
                filterable
                clearable
                style="width: 100%"
                @change="handleStudentChange"
              >
                <el-option
                  v-for="item in studentList"
                  :key="item.sid ?? item.sId"
                  :label="`${item.sno ?? item.sNo} - ${item.sname ?? item.sName}`"
                  :value="item.sid ?? item.sId"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="班级" prop="scclassid">
              <el-select
                v-model="formData.scclassid"
                placeholder="请选择班级"
                filterable
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="item in classList"
                  :key="item.cid ?? item.cId"
                  :label="item.cname ?? item.cName"
                  :value="item.cid ?? item.cId"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="学期" prop="sctermid">
              <el-select
                v-model="formData.sctermid"
                placeholder="请选择学期"
                filterable
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="item in termList"
                  :key="item.yid ?? item.yId"
                  :label="item.yall ?? item.yAll"
                  :value="item.yid ?? item.yId"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="课程" prop="sccourseid">
              <el-select
                v-model="formData.sccourseid"
                placeholder="请选择课程"
                filterable
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="item in courseList"
                  :key="item.crid ?? item.crId"
                  :label="item.crname ?? item.crName"
                  :value="item.crid ?? item.crId"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="教师" prop="scteacherid">
              <el-select
                v-model="formData.scteacherid"
                placeholder="请选择教师"
                filterable
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="item in teacherList"
                  :key="item.tid ?? item.tId"
                  :label="item.tname ?? item.tName"
                  :value="item.tid ?? item.tId"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="分数" prop="scscore">
              <el-input-number
                v-model="formData.scscore"
                :min="0"
                :max="100"
                :precision="1"
                :step="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="状态" prop="scstatus">
              <el-select
                v-model="formData.scstatus"
                placeholder="请选择状态"
                style="width: 100%"
              >
                <el-option label="未设置" :value="0" />
                <el-option label="已设置" :value="1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import request from "@/utils/Axios"

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref("新增成绩")
const formRef = ref(null)

const listData = ref([])
const studentList = ref([])
const termList = ref([])
const classList = ref([])
const courseList = ref([])
const teacherList = ref([])

const searchForm = reactive({
  qkey: "",
  sctermid: "",
  scclassid: "",
  sccourseid: "",
  scteacherid: "",
  scstatus: ""
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  scid: null,
  scstudentid: "",
  sctermid: "",
  scclassid: "",
  sccourseid: "",
  scteacherid: "",
  scscore: 0,
  scstatus: 1
})

const rules = {
  scstudentid: [{ required: true, message: "请选择学生", trigger: "change" }],
  sctermid: [{ required: true, message: "请选择学期", trigger: "change" }],
  scclassid: [{ required: true, message: "请选择班级", trigger: "change" }],
  sccourseid: [{ required: true, message: "请选择课程", trigger: "change" }],
  scscore: [{ required: true, message: "请输入分数", trigger: "blur" }],
  scstatus: [{ required: true, message: "请选择状态", trigger: "change" }]
}

const resetFormData = () => {
  formData.scid = null
  formData.scstudentid = ""
  formData.sctermid = ""
  formData.scclassid = ""
  formData.sccourseid = ""
  formData.scteacherid = ""
  formData.scscore = 0
  formData.scstatus = 1
}

const getResultData = (res) => {
  return res?.data ?? res
}

const loadStudentList = async () => {
  try {
    const res = await request.get("/studentinfo/getall")
    const result = getResultData(res)
    studentList.value = result.data || []
  } catch (error) {
    console.error("获取学生列表失败：", error)
  }
}

const loadTermList = async () => {
  try {
    const res = await request.get("/yearterm/getall")
    const result = getResultData(res)
    termList.value = result.data || []
  } catch (error) {
    console.error("获取学期列表失败：", error)
  }
}

const loadClassList = async () => {
  try {
    const res = await request.get("/classinfo/getall")
    const result = getResultData(res)
    classList.value = result.data || []
  } catch (error) {
    console.error("获取班级列表失败：", error)
  }
}

const loadCourseList = async () => {
  try {
    const res = await request.get("/courseinfo/getall")
    const result = getResultData(res)
    courseList.value = result.data || []
  } catch (error) {
    console.error("获取课程列表失败：", error)
  }
}

const loadTeacherList = async () => {
  try {
    const res = await request.get("/teacherinfo/getall")
    const result = getResultData(res)
    teacherList.value = result.data || []
  } catch (error) {
    console.error("获取教师列表失败：", error)
  }
}

const loadInitData = async () => {
  await Promise.all([
    loadStudentList(),
    loadTermList(),
    loadClassList(),
    loadCourseList(),
    loadTeacherList()
  ])
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await request.get("/scoreinfo/getlist", {
      params: {
        pageIndex: pagination.currentPage,
        pageSize: pagination.pageSize,
        qkey: searchForm.qkey,
        sctermid: searchForm.sctermid || null,
        scclassid: searchForm.scclassid || null,
        sccourseid: searchForm.sccourseid || null,
        scteacherid: searchForm.scteacherid || null,
        scstatus: searchForm.scstatus === "" ? null : searchForm.scstatus
      }
    })

    const result = getResultData(res)

    if (result._code === 200) {
      listData.value = result.data || []
      pagination.total = result.totalRecord || 0
    } else {
      listData.value = []
      pagination.total = 0
      ElMessage.error(result._msg || "获取成绩列表失败")
    }
  } catch (error) {
    console.error("获取成绩列表失败：", error)
    ElMessage.error("获取成绩列表失败")
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.currentPage = 1
  loadTableData()
}

const handleReset = () => {
  searchForm.qkey = ""
  searchForm.sctermid = ""
  searchForm.scclassid = ""
  searchForm.sccourseid = ""
  searchForm.scteacherid = ""
  searchForm.scstatus = ""
  pagination.currentPage = 1
  loadTableData()
}

const handleCurrentChange = (page) => {
  pagination.currentPage = page
  loadTableData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  loadTableData()
}

const handleAdd = async () => {
  resetFormData()
  dialogTitle.value = "新增成绩"
  dialogVisible.value = true
  await nextTick()
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleEdit = async (row) => {
  resetFormData()

  formData.scid = row.scid
  formData.scstudentid = row.scstudentid
  formData.sctermid = row.sctermid
  formData.scclassid = row.scclassid
  formData.sccourseid = row.sccourseid
  formData.scteacherid = row.scteacherid || ""
  formData.scscore = Number(row.scscore || 0)
  formData.scstatus = Number(row.scstatus ?? 1)

  dialogTitle.value = "编辑成绩"
  dialogVisible.value = true
  await nextTick()
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleStudentChange = (value) => {
  const student = studentList.value.find(
    item => (item.sid ?? item.sId) === value
  )
  if (student) {
    formData.scclassid = student.sclassid ?? student.sClassId ?? formData.scclassid
  }
}

const handleSave = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      const url = formData.scid ? "/scoreinfo/update" : "/scoreinfo/save"

      const submitData = {
        scid: formData.scid,
        scstudentid: formData.scstudentid,
        sctermid: formData.sctermid,
        scclassid: formData.scclassid,
        sccourseid: formData.sccourseid,
        scteacherid: formData.scteacherid || null,
        scscore: formData.scscore,
        scstatus: formData.scstatus
      }

      const res = await request.post(url, submitData)
      const result = getResultData(res)

      if (result.code === 200 || result._code === 200) {
        ElMessage.success(formData.scid ? "修改成功" : "新增成功")
        dialogVisible.value = false
        loadTableData()
      } else {
        ElMessage.error(result.msg || result._msg || "保存失败")
      }
    } catch (error) {
      console.error("保存成绩失败：", error)
      ElMessage.error("保存失败")
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定删除【${row.sname} - ${row.crname}】这条成绩记录吗？`,
      "删除提示",
      {
        type: "warning"
      }
    )

    const res = await request.post("/scoreinfo/delete", null, {
      params: {
        scid: row.scid
      }
    })

    const result = getResultData(res)

    if (result.code === 200 || result._code === 200) {
      ElMessage.success("删除成功")
      if (listData.value.length === 1 && pagination.currentPage > 1) {
        pagination.currentPage -= 1
      }
      loadTableData()
    } else {
      ElMessage.error(result.msg || result._msg || "删除失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除成绩失败：", error)
      ElMessage.error("删除失败")
    }
  }
}

onMounted(async () => {
  await loadInitData()
  await loadTableData()
})
</script>

<style scoped>
.page-box {
  padding: 12px;
}

.part-hed {
  margin-bottom: 12px;
}

.part-hed-tit {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.query-card {
  margin-bottom: 12px;
}

.query-form {
  display: flex;
  flex-wrap: wrap;
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
