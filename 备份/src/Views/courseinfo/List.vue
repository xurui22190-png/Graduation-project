<template>
  <div class="page-box">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="请输入课程编号或课程名称"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="所属专业">
          <el-select
            v-model="searchForm.crmajorid"
            placeholder="请选择专业"
            clearable
            style="width: 220px"
          >
            <el-option
              v-for="item in majorList"
              :key="item.mid"
              :label="item.mname"
              :value="item.mid"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增课程</el-button>
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
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column prop="crcode" label="课程编号" min-width="120" align="center" />

        <el-table-column prop="crname" label="课程名称" min-width="160" align="center" />

        <el-table-column label="所属专业" min-width="160" align="center">
          <template #default="scope">
            {{ getMajorName(scope.row.crmajorid) }}
          </template>
        </el-table-column>

        <el-table-column prop="crcredit" label="学分" width="80" align="center" />

        <el-table-column prop="crperiod" label="学时" width="80" align="center" />

        <el-table-column prop="crtype" label="课程类型" width="100" align="center" />

        <el-table-column prop="crexamtype" label="考核方式" width="100" align="center" />

        <el-table-column
          prop="crremark"
          label="备注"
          min-width="180"
          align="center"
          show-overflow-tooltip
        />

        <el-table-column prop="crstate" label="状态" width="90" align="center">
          <template #default="scope">
            <el-tag v-if="Number(scope.row.crstate) === 1" type="success">启用</el-tag>
            <el-tag v-else type="info">停用</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">修改</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-box">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="pagination.pageIndex"
          :page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="formData.crid ? '修改课程' : '新增课程'"
      width="650px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="课程编号" prop="crcode">
              <el-input v-model="formData.crcode" placeholder="请输入课程编号" clearable />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="课程名称" prop="crname">
              <el-input v-model="formData.crname" placeholder="请输入课程名称" clearable />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="所属专业" prop="crmajorid">
              <el-select v-model="formData.crmajorid" placeholder="请选择专业" style="width: 100%">
                <el-option
                  v-for="item in majorList"
                  :key="item.mid"
                  :label="item.mname"
                  :value="item.mid"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="学分" prop="crcredit">
              <el-input-number
                v-model="formData.crcredit"
                :min="0"
                :step="0.5"
                :precision="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="学时" prop="crperiod">
              <el-input-number
                v-model="formData.crperiod"
                :min="0"
                :step="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="课程类型" prop="crtype">
              <el-select v-model="formData.crtype" placeholder="请选择课程类型" style="width: 100%">
                <el-option label="必修" value="必修" />
                <el-option label="选修" value="选修" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="考核方式" prop="crexamtype">
              <el-select v-model="formData.crexamtype" placeholder="请选择考核方式" style="width: 100%">
                <el-option label="考试" value="考试" />
                <el-option label="考查" value="考查" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="状态" prop="crstate">
              <el-select v-model="formData.crstate" placeholder="请选择状态" style="width: 100%">
                <el-option label="启用" :value="1" />
                <el-option label="停用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="备注" prop="crremark">
              <el-input
                v-model="formData.crremark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from "vue"
import axios from "axios"
import { ElMessage, ElMessageBox } from "element-plus"

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)

const tableData = ref([])
const majorList = ref([])

const searchForm = reactive({
  qkey: "",
  crmajorid: ""
})

const pagination = reactive({
  pageIndex: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  crid: null,
  crcode: "",
  crname: "",
  crmajorid: null,
  crcredit: 0,
  crperiod: 0,
  crtype: "必修",
  crexamtype: "考试",
  crremark: "",
  crstate: 1
})

const rules = {
  crcode: [
    { required: true, message: "请输入课程编号", trigger: "blur" }
  ],
  crname: [
    { required: true, message: "请输入课程名称", trigger: "blur" }
  ],
  crmajorid: [
    { required: true, message: "请选择所属专业", trigger: "change" }
  ],
  crcredit: [
    { required: true, message: "请输入学分", trigger: "change" }
  ],
  crperiod: [
    { required: true, message: "请输入学时", trigger: "change" }
  ],
  crtype: [
    { required: true, message: "请选择课程类型", trigger: "change" }
  ],
  crexamtype: [
    { required: true, message: "请选择考核方式", trigger: "change" }
  ],
  crstate: [
    { required: true, message: "请选择状态", trigger: "change" }
  ]
}

const resetFormData = () => {
  formData.crid = null
  formData.crcode = ""
  formData.crname = ""
  formData.crmajorid = null
  formData.crcredit = 0
  formData.crperiod = 0
  formData.crtype = "必修"
  formData.crexamtype = "考试"
  formData.crremark = ""
  formData.crstate = 1
}

const normalizeMajorItem = (item) => {
  return {
    ...item,
    mid: item.mid ?? item.mId ?? null,
    mname: item.mname ?? item.mName ?? ""
  }
}

const normalizeCourseItem = (item) => {
  return {
    ...item,
    crid: item.crid ?? item.crId ?? null,
    crcode: item.crcode ?? item.crCode ?? "",
    crname: item.crname ?? item.crName ?? "",
    crmajorid: item.crmajorid ?? item.crMajorId ?? null,
    crcredit: item.crcredit ?? item.crCredit ?? 0,
    crperiod: item.crperiod ?? item.crPeriod ?? 0,
    crtype: item.crtype ?? item.crType ?? "必修",
    crexamtype: item.crexamtype ?? item.crExamType ?? "考试",
    crremark: item.crremark ?? item.crRemark ?? "",
    crstate: item.crstate ?? item.crState ?? 1
  }
}

const getMajorName = (mid) => {
  const item = majorList.value.find((m) => String(m.mid) === String(mid))
  return item ? item.mname : ""
}

const loadMajorList = async () => {
  try {
    const res = await axios.get("/majorinfo/getlist", {
      params: {
        pageIndex: 1,
        pageSize: 9999,
        qkey: "",
        mcollegeid: 0
      }
    })

    if (res.data && (res.data._code === 200 || res.data.code === 200)) {
      majorList.value = (res.data.data || []).map((item) => normalizeMajorItem(item))
    } else {
      ElMessage.error(res.data?._msg || res.data?.msg || "获取专业列表失败")
    }
  } catch (error) {
    console.error("获取专业列表失败：", error)
    ElMessage.error("获取专业列表失败")
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/courseinfo/getlist", {
      params: {
        pageIndex: pagination.pageIndex,
        pageSize: pagination.pageSize,
        qkey: searchForm.qkey,
        crmajorid: searchForm.crmajorid || ""
      }
    })

    if (res.data && (res.data._code === 200 || res.data.code === 200)) {
      tableData.value = (res.data.data || []).map((item) => normalizeCourseItem(item))
      pagination.total = res.data.totalRecord || 0
    } else {
      ElMessage.error(res.data?._msg || res.data?.msg || "获取课程列表失败")
    }
  } catch (error) {
    console.error("获取课程列表失败：", error)
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
  searchForm.crmajorid = ""
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

  const currentRow = normalizeCourseItem(row)

  formData.crid = currentRow.crid
  formData.crcode = currentRow.crcode
  formData.crname = currentRow.crname
  formData.crmajorid = currentRow.crmajorid
  formData.crcredit = currentRow.crcredit
  formData.crperiod = currentRow.crperiod
  formData.crtype = currentRow.crtype
  formData.crexamtype = currentRow.crexamtype
  formData.crremark = currentRow.crremark
  formData.crstate = currentRow.crstate

  dialogVisible.value = true
  await nextTick()
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleSubmit = async () => {
  if (!formRef.value) {
    return
  }

  try {
    await formRef.value.validate()
  } catch (error) {
    return
  }

  submitLoading.value = true
  try {
    const url = formData.crid ? "/courseinfo/update" : "/courseinfo/add"

    const payload = {
      crid: formData.crid,
      crcode: formData.crcode,
      crname: formData.crname,
      crmajorid: formData.crmajorid,
      crcredit: formData.crcredit,
      crperiod: formData.crperiod,
      crtype: formData.crtype,
      crexamtype: formData.crexamtype,
      crremark: formData.crremark,
      crstate: formData.crstate
    }

    const res = await axios.post(url, payload)

    if (res.data && (res.data.code === 200 || res.data._code === 200)) {
      ElMessage.success(res.data.msg || res.data._msg || "操作成功")
      dialogVisible.value = false
      loadTableData()
    } else {
      ElMessage.error(res.data?.msg || res.data?._msg || "操作失败")
    }
  } catch (error) {
    console.error("提交失败：", error)
    ElMessage.error("提交失败")
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除课程【${row.crname}】吗？`, "提示", {
      type: "warning"
    })

    const res = await axios.get("/courseinfo/delete", {
      params: {
        id: row.crid
      }
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
    if (error === "cancel" || error === "close") {
      return
    }
    console.error("删除失败：", error)
    ElMessage.error("删除失败")
  }
}

onMounted(async () => {
  await loadMajorList()
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
  min-height: 400px;
}

.pager-box {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
