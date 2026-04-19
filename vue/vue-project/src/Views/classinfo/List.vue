<template>
  <div class="page-box">
    <!-- 查询区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="请输入班级名称或班级编号"
            clearable
          />
        </el-form-item>

        <el-form-item label="专业ID">
          <el-input
            v-model="searchForm.cmajorid"
            placeholder="请输入专业ID"
            clearable
          />
        </el-form-item>

        <el-form-item label="年级">
          <el-input
            v-model="searchForm.cgrade"
            placeholder="请输入年级"
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增班级</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never" class="table-card">
      <el-table
        :data="tableData"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="cid" label="班级ID" width="100" />
        <el-table-column prop="cmajorid" label="专业ID" width="120" />
        <el-table-column prop="cgrade" label="年级" width="120" />
        <el-table-column prop="cname" label="班级名称" min-width="200" />
        <el-table-column prop="ccode" label="班级编号" min-width="150" />

        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-box">
        <el-pagination
          background
          layout="total, prev, pager, next, sizes"
          :current-page="pagination.pageIndex"
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
      width="520px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
      >
        <el-form-item label="专业ID" prop="cmajorid">
          <el-input
            v-model="formData.cmajorid"
            placeholder="请输入专业ID"
            clearable
          />
        </el-form-item>

        <el-form-item label="年级" prop="cgrade">
          <el-input
            v-model="formData.cgrade"
            placeholder="请输入年级"
            clearable
          />
        </el-form-item>

        <el-form-item label="班级名称" prop="cname">
          <el-input
            v-model="formData.cname"
            placeholder="请输入班级名称"
            clearable
          />
        </el-form-item>

        <el-form-item label="班级编号" prop="ccode">
          <el-input
            v-model="formData.ccode"
            placeholder="请输入班级编号"
            clearable
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import axios from "axios"

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref("新增班级")
const formRef = ref()

const searchForm = reactive({
  qkey: "",
  cmajorid: "",
  cgrade: ""
})

const pagination = reactive({
  pageIndex: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  cid: null,
  cmajorid: "",
  cgrade: "",
  cname: "",
  ccode: ""
})

const rules = {
  cmajorid: [
    { required: true, message: "请输入专业ID", trigger: "blur" }
  ],
  cgrade: [
    { required: true, message: "请输入年级", trigger: "blur" }
  ],
  cname: [
    { required: true, message: "请输入班级名称", trigger: "blur" }
  ],
  ccode: [
    { required: true, message: "请输入班级编号", trigger: "blur" }
  ]
}

// 获取表格数据
const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      pageIndex: pagination.pageIndex,
      pageSize: pagination.pageSize,
      qkey: searchForm.qkey,
      cgrade: searchForm.cgrade,
      cmajorid: searchForm.cmajorid ? Number(searchForm.cmajorid) : 0
    }

    const res = await axios.get("/classinfo/getlist", { params })

    if (res.data._code === 200) {
      tableData.value = res.data.data || []
      pagination.total = res.data.totalRecord || 0
      pagination.pageIndex = res.data.pageIndex || pagination.pageIndex
      pagination.pageSize = res.data.pageSize || pagination.pageSize
    } else {
      ElMessage.error(res.data._msg || "获取班级列表失败")
    }
  } catch (error) {
    console.error("获取班级列表失败：", error)
    ElMessage.error("获取班级列表失败")
  } finally {
    loading.value = false
  }
}

// 查询
const handleSearch = () => {
  pagination.pageIndex = 1
  loadTableData()
}

// 重置
const handleReset = () => {
  searchForm.qkey = ""
  searchForm.cmajorid = ""
  searchForm.cgrade = ""
  pagination.pageIndex = 1
  pagination.pageSize = 10
  loadTableData()
}

// 分页切换
const handleCurrentChange = (page) => {
  pagination.pageIndex = page
  loadTableData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.pageIndex = 1
  loadTableData()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = "新增班级"
  formData.cid = null
  formData.cmajorid = ""
  formData.cgrade = ""
  formData.cname = ""
  formData.ccode = ""
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = "编辑班级"
  formData.cid = row.cid
  formData.cmajorid = row.cmajorid
  formData.cgrade = row.cgrade
  formData.cname = row.cname
  formData.ccode = row.ccode
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      const submitData = {
        cid: formData.cid,
        cmajorid: formData.cmajorid ? Number(formData.cmajorid) : 0,
        cgrade: formData.cgrade,
        cname: formData.cname,
        ccode: formData.ccode
      }

      const url = formData.cid ? "/classinfo/update" : "/classinfo/add"
      const res = await axios.post(url, submitData)

      if (res.data.code === 200) {
        ElMessage.success(res.data.msg || "操作成功")
        dialogVisible.value = false
        loadTableData()
      } else {
        ElMessage.error(res.data.msg || "操作失败")
      }
    } catch (error) {
      console.error("提交失败：", error)
      ElMessage.error("提交失败")
    }
  })
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除班级【${row.cname}】吗？`,
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    )

    const res = await axios.post(`/classinfo/delete?id=${row.cid}`)

    if (res.data.code === 200) {
      ElMessage.success(res.data.msg || "删除成功")
      loadTableData()
    } else {
      ElMessage.error(res.data.msg || "删除失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除失败：", error)
      ElMessage.error("删除失败")
    }
  }
}

onMounted(() => {
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
  margin-bottom: 15px;
}

.pager-box {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>
