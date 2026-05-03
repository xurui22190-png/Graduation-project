<template>
  <div class="list-container">
    <div class="module-panel">
      <span class="module-title">专业管理</span>
    </div>

    <!-- 搜索 -->
    <div class="search-panel">
      <el-form class="search-form" :inline="true">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.qkey" placeholder="专业名称或代码" clearable />
        </el-form-item>

        <el-form-item label="所属院系">
          <el-select v-model="searchForm.mcollegeid" placeholder="请选择院系" clearable>
            <el-option
              v-for="item in collegeList"
              :key="item.cid"
              :label="item.cname"
              :value="item.cid"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格 -->
    <div class="table-panel">
      <div class="table-wrapper">
        <el-table :data="listData" border style="width: 100%" v-loading="loading">

          <el-table-column prop="mname" label="专业名称" min-width="180" />
          <el-table-column prop="mcode" label="专业代码" min-width="120" />

          <!-- 显示院系名称 -->
          <el-table-column label="所属院系" min-width="160">
            <template #default="scope">
              {{
                collegeList.find(c => c.cid === scope.row.mcollegeid)?.cname || '未知'
              }}
            </template>
          </el-table-column>

          <el-table-column label="操作" min-width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" type="warning" @click="handleEdit(scope.$index, scope.row)">
                修改
              </el-button>

              <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>

        </el-table>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-panel">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 30]"
        :page-size="pagination.pageSize"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="100px">

        <el-form-item label="专业名称" prop="mname">
          <el-input v-model="editForm.mname" />
        </el-form-item>

        <el-form-item label="专业代码" prop="mcode">
          <el-input v-model="editForm.mcode" />
        </el-form-item>

        <!-- ⭐ 这里改成下拉 -->
        <el-form-item label="所属院系" prop="mcollegeid">
          <el-select v-model="editForm.mcollegeid" placeholder="请选择院系">
            <el-option
              v-for="item in collegeList"
              :key="item.cid"
              :label="item.cname"
              :value="item.cid"
            />
          </el-select>
        </el-form-item>

      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import request from "@/utils/Axios"

const loading = ref(false)

const searchForm = reactive({
  qkey: "",
  mcollegeid: ""
})

const listData = ref([])

// ⭐ 院系列表
const collegeList = ref([])

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref("")
const editFormRef = ref()

const editForm = reactive({
  mid: null,
  mname: "",
  mcode: "",
  mcollegeid: ""
})

const editRules = {
  mname: [{ required: true, message: "请输入专业名称", trigger: "blur" }],
  mcode: [{ required: true, message: "请输入专业代码", trigger: "blur" }],
  mcollegeid: [{ required: true, message: "请选择院系", trigger: "change" }]
}

// ⭐ 加载院系列表
const loadCollegeList = async () => {
  try {
    const res = await request.get("/collegeinfo/getlist")
    const result = res.data || res

    if (result._code === 200 || result.code === 200) {
      collegeList.value = result.data || []
    } else {
      ElMessage.error("获取院系列表失败")
    }
  } catch (error) {
    console.error("院系加载失败：", error)
  }
}

const resetEditForm = () => {
  editForm.mid = null
  editForm.mname = ""
  editForm.mcode = ""
  editForm.mcollegeid = ""
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await request.get("/majorinfo/getlist", {
      params: {
        pageIndex: pagination.currentPage,
        pageSize: pagination.pageSize,
        qkey: searchForm.qkey,
        mcollegeid: searchForm.mcollegeid || null
      }
    })

    const result = res.data || res

    if (result._code === 200 || result.code === 200) {
      listData.value = result.data || []
      pagination.total = result.totalRecord || result.total || 0
    } else {
      ElMessage.error("获取专业列表失败")
    }
  } catch (error) {
    ElMessage.error("获取专业列表失败")
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
  searchForm.mcollegeid = ""
  loadTableData()
}

const handleAdd = () => {
  resetEditForm()
  dialogTitle.value = "新增专业"
  dialogVisible.value = true
}

const handleEdit = async (index, row) => {
  const res = await request.get(`/majorinfo/getmodel/${row.mid}`)
  const result = res.data || res

  if (result.code === 200 || result._code === 200) {
    Object.assign(editForm, result.data)
    dialogTitle.value = "修改专业"
    dialogVisible.value = true
  }
}

const handleSave = async () => {
  await editFormRef.value.validate(async (valid) => {
    if (!valid) return

    const url = editForm.mid ? "/majorinfo/update" : "/majorinfo/add"
    const res = await request.post(url, editForm)
    const result = res.data || res

    if (result.code === 200 || result._code === 200) {
      ElMessage.success("保存成功")
      dialogVisible.value = false
      loadTableData()
    } else {
      ElMessage.error("保存失败")
    }
  })
}

const handleDelete = async (index, row) => {
  await ElMessageBox.confirm(`确定删除【${row.mname}】吗？`)

  const res = await request.delete(`/majorinfo/delete/${row.mid}`)
  const result = res.data || res

  if (result.code === 200 || result._code === 200) {
    ElMessage.success("删除成功")
    loadTableData()
  }
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  loadTableData()
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
  loadTableData()
}

onMounted(() => {
  loadTableData()
  loadCollegeList() // ⭐ 必须加载
})
</script>

<style>
body {
  background-color: #f0f2f5;
  margin: 0;
  padding: 0;
  overflow: hidden;
}
</style>

<style scoped>
.list-container {
  min-height: 100vh;
  padding: 24px;
  background: #f0f2f5;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
  overflow-y: auto;
}

.module-panel {
  margin-bottom: 24px;
  flex-shrink: 0;
}
.module-title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2f3d;
  position: relative;
  padding-left: 12px;
}
.module-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  background: linear-gradient(to bottom, #409eff, #36d1dc);
  border-radius: 2px;
}

.search-panel {
  background: #ffffff;
  border-radius: 16px;
  padding: 24px 24px 4px 24px;
  margin-bottom: 24px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.03);
  flex-shrink: 0;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 16px;
}
.search-form .el-form-item {
  margin-right: 0;
  margin-bottom: 20px;
  flex: 0 0 auto;
}
.search-form .el-form-item:last-child {
  margin-left: auto;
}
.search-form .el-input {
  width: 220px;
}

.table-panel {
  background: #ffffff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.03);
  display: flex;
  flex-direction: column;
}

.table-wrapper {
  max-height: 520px;
  overflow: auto;
  border-radius: 12px;
}

:deep(.el-table) {
  border: none;
  border-radius: 12px;
  overflow: hidden;
}
:deep(.el-table th.el-table__cell) {
  background: #f8fafd;
  color: #2c3e50;
  font-weight: 600;
  font-size: 14px;
  border-bottom: 2px solid #e9eef4;
  padding: 12px 0;
}
:deep(.el-table td.el-table__cell) {
  padding: 12px 0;
  color: #3a4a5e;
}

:deep(.el-button--small) {
  border-radius: 20px;
  padding: 8px 18px;
  font-weight: 500;
}

.pagination-panel {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  background: #ffffff;
  border-radius: 16px;
  padding: 16px 24px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.03);
  flex-shrink: 0;
}

@media (max-width: 768px) {
  .list-container {
    padding: 16px;
  }

  .search-form {
    flex-direction: column;
    align-items: stretch;
    gap: 0;
  }

  .search-form .el-form-item {
    width: 100%;
    margin-bottom: 16px;
  }

  .search-form .el-form-item:last-child {
    margin-left: 0;
    display: flex;
    justify-content: flex-end;
  }

  .search-form .el-input {
    width: 100%;
  }
}
</style>
