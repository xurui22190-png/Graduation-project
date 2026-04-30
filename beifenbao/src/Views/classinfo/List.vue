<template>
  <div class="page-box">
    <!-- 查询区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">

        <el-form-item label="关键字">
          <el-input v-model="searchForm.qkey" placeholder="班级名称或编号" clearable />
        </el-form-item>

        <!-- ⭐ 专业下拉 -->
        <el-form-item label="专业">
          <el-select v-model="searchForm.cmajorid" placeholder="请选择专业" clearable>
            <el-option
              v-for="item in majorList"
              :key="item.mid"
              :label="item.mname"
              :value="item.mid"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="年级">
          <el-input v-model="searchForm.cgrade" placeholder="请输入年级" clearable />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增班级</el-button>
        </el-form-item>

      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading">

        <el-table-column prop="cid" label="ID" width="80" />

        <!-- ⭐ 显示专业名称 -->
        <el-table-column label="专业" width="160">
          <template #default="scope">
            {{
              majorList.find(m => m.mid === scope.row.cmajorid)?.mname || '未知'
            }}
          </template>
        </el-table-column>

        <el-table-column prop="cgrade" label="年级" width="120" />
        <el-table-column prop="cname" label="班级名称" min-width="180" />
        <el-table-column prop="ccode" label="班级编号" min-width="140" />

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>

      <!-- 分页 -->
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

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px">

      <el-form ref="formRef" :model="formData" :rules="rules" label-width="90px">

        <!-- ⭐ 专业下拉 -->
        <el-form-item label="专业" prop="cmajorid">
          <el-select v-model="formData.cmajorid" placeholder="请选择专业">
            <el-option
              v-for="item in majorList"
              :key="item.mid"
              :label="item.mname"
              :value="item.mid"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="年级" prop="cgrade">
          <el-input v-model="formData.cgrade" />
        </el-form-item>

        <el-form-item label="班级名称" prop="cname">
          <el-input v-model="formData.cname" />
        </el-form-item>

        <el-form-item label="班级编号" prop="ccode">
          <el-input v-model="formData.ccode" />
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

// ⭐ 专业列表
const majorList = ref([])

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
  cmajorid: [{ required: true, message: "请选择专业", trigger: "change" }],
  cgrade: [{ required: true, message: "请输入年级", trigger: "blur" }],
  cname: [{ required: true, message: "请输入班级名称", trigger: "blur" }],
  ccode: [{ required: true, message: "请输入班级编号", trigger: "blur" }]
}

// ⭐ 加载专业列表
const loadMajorList = async () => {
  const res = await axios.get("/majorinfo/getlist")
  if (res.data._code === 200) {
    majorList.value = res.data.data || []
  }
}

// 表格数据
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/classinfo/getlist", {
      params: {
        ...searchForm,
        cmajorid: searchForm.cmajorid || 0,
        pageIndex: pagination.pageIndex,
        pageSize: pagination.pageSize
      }
    })

    if (res.data._code === 200) {
      tableData.value = res.data.data || []
      pagination.total = res.data.totalRecord || 0
    }
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageIndex = 1
  loadTableData()
}

const handleReset = () => {
  Object.assign(searchForm, { qkey: "", cmajorid: "", cgrade: "" })
  loadTableData()
}

const handleAdd = () => {
  dialogTitle.value = "新增班级"
  Object.assign(formData, {
    cid: null,
    cmajorid: "",
    cgrade: "",
    cname: "",
    ccode: ""
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = "编辑班级"
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    const url = formData.cid ? "/classinfo/update" : "/classinfo/add"
    const res = await axios.post(url, formData)

    if (res.data.code === 200) {
      ElMessage.success("操作成功")
      dialogVisible.value = false
      loadTableData()
    }
  })
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除【${row.cname}】吗？`)
  const res = await axios.post(`/classinfo/delete?id=${row.cid}`)
  if (res.data.code === 200) {
    ElMessage.success("删除成功")
    loadTableData()
  }
}

const handleCurrentChange = (p) => {
  pagination.pageIndex = p
  loadTableData()
}

const handleSizeChange = (s) => {
  pagination.pageSize = s
  loadTableData()
}

onMounted(() => {
  loadMajorList()
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
