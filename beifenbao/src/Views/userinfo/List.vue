<template>
  <div class="page-box">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="请输入账号或姓名"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="性别">
          <el-select
            v-model="searchForm.usex"
            placeholder="全部"
            clearable
            style="width: 120px"
          >
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>

        <el-form-item label="角色">
          <el-select
            v-model="searchForm.urole"
            placeholder="全部"
            clearable
            style="width: 140px"
          >
            <el-option label="全部" :value="0" />
            <el-option label="学生" :value="1" />
            <el-option label="教师" :value="2" />
            <el-option label="管理员" :value="3" />
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
      >
        <el-table-column prop="uid" label="ID" width="80" align="center" />
        <el-table-column prop="ulog" label="账号" min-width="150" />
        <el-table-column prop="uname" label="姓名" min-width="120" />
        <el-table-column prop="usex" label="性别" width="90" align="center" />

        <el-table-column label="角色" width="110" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.urole === 1" type="warning">学生</el-tag>
            <el-tag v-else-if="row.urole === 2" type="success">教师</el-tag>
            <el-tag v-else-if="row.urole === 3" type="danger">管理员</el-tag>
            <el-tag v-else>未知</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="ucreatedate" label="创建时间" min-width="180" />

        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">
              修改
            </el-button>
            <el-button size="small" type="warning" @click="handleResetPwd(row)">
              重置密码
            </el-button>
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

    <el-dialog
      v-model="dialogVisible"
      title="修改用户"
      width="520px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="90px"
      >
        <el-form-item label="账号" prop="ulog">
          <el-input v-model="formData.ulog" placeholder="请输入账号" />
        </el-form-item>

        <el-form-item label="姓名" prop="uname">
          <el-input v-model="formData.uname" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="性别" prop="usex">
          <el-select
            v-model="formData.usex"
            placeholder="请选择性别"
            style="width: 100%"
          >
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>

        <el-form-item label="角色" prop="urole">
          <el-select
            v-model="formData.urole"
            placeholder="请选择角色"
            style="width: 100%"
          >
            <el-option label="学生" :value="1" />
            <el-option label="教师" :value="2" />
            <el-option label="管理员" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="头像" prop="uphoto">
          <el-input v-model="formData.uphoto" placeholder="请输入头像地址，可为空" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import axios from "axios"

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref()

const tableData = ref([])

const searchForm = reactive({
  qkey: "",
  usex: "",
  urole: 0
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  uid: null,
  ulog: "",
  uname: "",
  usex: "",
  uphoto: "",
  urole: 3
})

const formRules = {
  ulog: [{ required: true, message: "请输入账号", trigger: "blur" }],
  uname: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  usex: [{ required: true, message: "请选择性别", trigger: "change" }],
  urole: [{ required: true, message: "请选择角色", trigger: "change" }]
}

const getAuthHeaders = () => {
  const token = localStorage.getItem("token") || sessionStorage.getItem("token") || ""
  return token ? { Authorization: token } : {}
}

const resetFormData = () => {
  formData.uid = null
  formData.ulog = ""
  formData.uname = ""
  formData.usex = ""
  formData.uphoto = ""
  formData.urole = 3
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("http://127.0.0.1:9192/api/userinfo/getlist", {
      params: {
        pageIndex: pagination.currentPage,
        pageSize: pagination.pageSize,
        qkey: searchForm.qkey,
        usex: searchForm.usex,
        urole: searchForm.urole
      },
      headers: getAuthHeaders(),
      timeout: 18000
    })

    const result = res.data
    if (result && result._code === 200) {
      tableData.value = result.data || []
      pagination.total = result.totalRecord || 0
    } else {
      tableData.value = []
      pagination.total = 0
      ElMessage.error(result?._msg || "获取用户列表失败")
    }
  } catch (err) {
    console.error("获取用户列表失败：", err)
    tableData.value = []
    pagination.total = 0

    if (err?.response?.status === 401) {
      ElMessage.error("登录已失效，请重新登录")
      return
    }

    ElMessage.error(
      err?.response?.data?._msg ||
      err?.response?.data?.msg ||
      err?.message ||
      "获取用户列表失败"
    )
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
  searchForm.usex = ""
  searchForm.urole = 0
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

const handleEdit = async (row) => {
  resetFormData()

  formData.uid = row.uid
  formData.ulog = row.ulog
  formData.uname = row.uname
  formData.usex = row.usex
  formData.uphoto = row.uphoto || ""
  formData.urole = row.urole

  dialogVisible.value = true
  await nextTick()

  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      const payload = {
        uid: formData.uid,
        ulog: formData.ulog,
        uname: formData.uname,
        usex: formData.usex,
        uphoto: formData.uphoto,
        urole: formData.urole
      }

      const res = await axios.post(
        "http://127.0.0.1:9192/api/userinfo/update",
        payload,
        {
          headers: {
            "Content-Type": "application/json",
            ...getAuthHeaders()
          },
          timeout: 18000
        }
      )

      const result = res.data
const code = result?._code ?? result?.code
const msg = result?._msg ?? result?.msg

if (code === 200) {
  ElMessage.success(msg || "修改成功")
  dialogVisible.value = false
  loadTableData()
} else {
  ElMessage.error(msg || "修改失败")
}
    } catch (err) {
      console.error("提交失败：", err)

      if (err?.response?.status === 401) {
        ElMessage.error("登录已失效，请重新登录")
        return
      }

      ElMessage.error(
        err?.response?.data?._msg ||
        err?.response?.data?.msg ||
        err?.message ||
        "提交失败"
      )
    } finally {
      submitLoading.value = false
    }
  })
}

const handleResetPwd = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定重置【${row.uname}】的密码为 123456 吗？`,
      "重置密码确认",
      {
        type: "warning"
      }
    )

    const res = await axios.post(
      "http://127.0.0.1:9192/api/userinfo/resetpwd",
      {
        uid: row.uid
      },
      {
        headers: {
          "Content-Type": "application/json",
          ...getAuthHeaders()
        },
        timeout: 18000
      }
    )

    const result = res.data
    if (result && result.code === 200) {
      ElMessage.success(result.msg || "重置成功")
    } else {
      ElMessage.error(result?.msg || "重置失败")
    }
  } catch (err) {
    if (err === "cancel" || err === "close") return

    console.error("重置密码失败：", err)

    if (err?.response?.status === 401) {
      ElMessage.error("登录已失效，请重新登录")
      return
    }

    ElMessage.error(
      err?.response?.data?.msg ||
      err?.response?.data?._msg ||
      err?.message ||
      "重置失败"
    )
  }
}

onMounted(() => {
  loadTableData()
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
  min-height: 500px;
}

.pager-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
