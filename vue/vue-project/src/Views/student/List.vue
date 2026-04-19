<template>
  <div class="student-page">
    <div class="search-panel">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="请输入学号或姓名"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="性别">
          <el-select
            v-model="searchForm.ssex"
            placeholder="请选择性别"
            clearable
            style="width: 140px"
          >
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select
            v-model="searchForm.sclassid"
            placeholder="请选择班级"
            clearable
            filterable
            style="width: 220px"
          >
            <el-option
              v-for="item in classList"
              :key="item.cId ?? item.cid"
              :label="item.cName ?? item.cname"
              :value="item.cId ?? item.cid"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增学生</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-panel">
      <el-table
        :data="tableData"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="sid" label="ID" width="70" align="center" />
        <el-table-column prop="sno" label="学号" min-width="120" />
        <el-table-column prop="sname" label="姓名" min-width="100" />
        <el-table-column prop="ssex" label="性别" width="80" align="center" />
        <el-table-column prop="className" label="班级" min-width="140" />
        <el-table-column prop="mname" label="专业" min-width="140" />
        <el-table-column prop="collegeName" label="学院" min-width="140" />
        <el-table-column prop="cGrade" label="年级" width="90" align="center" />
        <el-table-column prop="stel" label="电话" min-width="130" />
        <el-table-column prop="sidcard" label="身份证号" min-width="180" show-overflow-tooltip />
        <el-table-column prop="saddress" label="家庭住址" min-width="200" show-overflow-tooltip />

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
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="formData.sid ? '编辑学生' : '新增学生'"
      width="760px"
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
            <el-form-item label="学号" prop="sno">
              <el-input
                v-model="formData.sno"
                placeholder="请输入学号"
                clearable
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="姓名" prop="sname">
              <el-input
                v-model="formData.sname"
                placeholder="请输入姓名"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="ssex">
              <el-select
                v-model="formData.ssex"
                placeholder="请选择性别"
                style="width: 100%"
              >
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="班级" prop="sclassid">
              <el-select
                v-model="formData.sclassid"
                placeholder="请选择班级"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="item in classList"
                  :key="item.cId ?? item.cid"
                  :label="item.cName ?? item.cname"
                  :value="item.cId ?? item.cid"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="专业">
              <el-input
                :model-value="formData.mName"
                placeholder="根据班级自动带出"
                readonly
              />
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="学院">
              <el-input
                :model-value="formData.collegeName"
                placeholder="根据班级自动带出"
                readonly
              />
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="年级">
              <el-input
                :model-value="formData.cGrade"
                placeholder="根据班级自动带出"
                readonly
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="电话" prop="stel">
          <el-input
            v-model="formData.stel"
            maxlength="11"
            placeholder="请输入电话"
            clearable
          />
        </el-form-item>

        <el-form-item label="身份证号" prop="sidcard">
          <el-input
            v-model="formData.sidcard"
            maxlength="18"
            placeholder="请输入身份证号"
            clearable
          />
        </el-form-item>

        <el-form-item label="家庭住址" prop="saddress">
          <el-input
            v-model="formData.saddress"
            placeholder="请输入家庭住址"
            clearable
          />
        </el-form-item>

        <el-form-item label="账号ID" prop="saccountid">
          <el-input
            v-model="formData.saccountid"
            placeholder="没有可填0"
            clearable
          />
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
import { reactive, ref, onMounted, nextTick, watch } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import request from "@/utils/Axios"

const loading = ref(false)
const saveLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref()

const searchForm = reactive({
  qkey: "",
  ssex: "",
  sclassid: null
})

const tableData = ref([])
const classList = ref([])

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  sid: null,
  sno: "",
  sname: "",
  ssex: "",
  sclassid: null,
  stel: "",
  sidcard: "",
  saddress: "",
  saccountid: 0,
  mName: "",
  collegeName: "",
  cGrade: ""
})

const rules = {
  sno: [
    { required: true, message: "请输入学号", trigger: "blur" }
  ],
  sname: [
    { required: true, message: "请输入姓名", trigger: "blur" }
  ],
  ssex: [
    { required: true, message: "请选择性别", trigger: "change" }
  ],
  sclassid: [
    { required: true, message: "请选择班级", trigger: "change" }
  ]
}

const resetFormData = () => {
  formData.sid = null
  formData.sno = ""
  formData.sname = ""
  formData.ssex = ""
  formData.sclassid = null
  formData.stel = ""
  formData.sidcard = ""
  formData.saddress = ""
  formData.saccountid = 0
  formData.mName = ""
  formData.collegeName = ""
  formData.cGrade = ""
}

const loadClassList = async () => {
  try {
    const res = await request.get("/classinfo/getlist", {
      params: {
        pageIndex: 1,
        pageSize: 999,
        qkey: "",
        cgrade: "",
        cmajorid: 0
      }
    })

    const result = res.data
    classList.value = result.data || []
    console.log("classList =", classList.value)
  } catch (error) {
    console.error("获取班级列表失败：", error)
    ElMessage.error("获取班级列表失败")
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await request.get("/studentinfo/getlist", {
      params: {
        pageIndex: pagination.currentPage,
        pageSize: pagination.pageSize,
        qkey: searchForm.qkey,
        ssex: searchForm.ssex,
        sclassid: searchForm.sclassid || 0
      }
    })

    const result = res.data
    tableData.value = result.data || []
    pagination.total = result.totalRecord || 0
    console.log("tableData =", tableData.value)
  } catch (error) {
    console.error("获取学生列表失败：", error)
    ElMessage.error("获取学生列表失败")
  } finally {
    loading.value = false
  }
}

const fillClassExtraInfo = (classId) => {
  const current = classList.value.find(item =>
    (item.cId ?? item.cid) === classId
  )

  if (!current) {
    formData.mName = ""
    formData.collegeName = ""
    formData.cGrade = ""
    return
  }

  formData.mName = current.mName ?? current.mname ?? ""
  formData.collegeName = current.collegeName ?? current.collegename ?? ""
  formData.cGrade = current.cGrade ?? current.cgrade ?? ""
}

watch(
  () => formData.sclassid,
  (newVal) => {
    fillClassExtraInfo(newVal)
  }
)

const handleSearch = () => {
  pagination.currentPage = 1
  loadTableData()
}

const handleReset = () => {
  searchForm.qkey = ""
  searchForm.ssex = ""
  searchForm.sclassid = null
  pagination.currentPage = 1
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

  formData.sid = row.sid
  formData.sno = row.sno
  formData.sname = row.sname
  formData.ssex = row.ssex
  formData.sclassid = row.sclassid ?? row.sClassId ?? null
  formData.stel = row.stel ?? row.sTel ?? ""
  formData.sidcard = row.sidcard ?? row.sIdcard ?? ""
  formData.saddress = row.saddress ?? row.sAddress ?? ""
  formData.saccountid = row.saccountid ?? row.sAccountId ?? 0
  formData.mName = row.mName ?? row.mname ?? ""
  formData.collegeName = row.collegeName ?? row.collegename ?? ""
  formData.cGrade = row.cGrade ?? row.cgrade ?? ""

  dialogVisible.value = true

  await nextTick()
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleSave = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
  } catch (error) {
    return
  }

  saveLoading.value = true
  try {
    const postData = {
      sid: formData.sid,
      sno: formData.sno,
      sname: formData.sname,
      ssex: formData.ssex,
      sclassid: formData.sclassid,
      stel: formData.stel,
      sidcard: formData.sidcard,
      saddress: formData.saddress,
      saccountid: Number(formData.saccountid || 0)
    }

    console.log("提交的数据：", postData)

    const url = formData.sid ? "/studentinfo/edit" : "/studentinfo/add"
    const res = await request.post(url, postData)
    const result = res.data

    console.log("保存返回：", result)

    if (result.code === 200 || result._code === 200) {
      ElMessage.success(result.msg || result._msg || "保存成功")
      dialogVisible.value = false
      await loadTableData()
    } else {
      ElMessage.error(result.msg || result._msg || "保存失败")
    }
  } catch (error) {
    console.error("保存学生失败：", error)
    ElMessage.error("保存学生失败")
  } finally {
    saveLoading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定删除学生【${row.sname}】吗？`,
      "提示",
      { type: "warning" }
    )

    const res = await request.post("/studentinfo/delete", [row.sid])
    const result = res.data

    if (result.code === 200 || result._code === 200) {
      ElMessage.success(result.msg || result._msg || "删除成功")

      if (tableData.value.length === 1 && pagination.currentPage > 1) {
        pagination.currentPage -= 1
      }

      await loadTableData()
    } else {
      ElMessage.error(result.msg || result._msg || "删除失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除学生失败：", error)
      ElMessage.error("删除学生失败")
    }
  }
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.currentPage = 1
  loadTableData()
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
  loadTableData()
}

onMounted(async () => {
  await loadClassList()
  await loadTableData()
})
</script>
<style scoped>
.student-page {
  padding: 16px;
  background: #f5f7fa;
  min-height: 100%;
  box-sizing: border-box;
}

.search-panel,
.table-panel {
  background: #ffffff;
  border-radius: 8px;
  padding: 16px;
  box-sizing: border-box;
}

.search-panel {
  margin-bottom: 16px;
}

.pager-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

:deep(.el-dialog__body) {
  padding-top: 10px;
}
</style>
