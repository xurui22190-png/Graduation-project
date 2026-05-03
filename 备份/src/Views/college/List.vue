<template>
  <div class="list-container">
    <div class="module-panel">
      <span class="module-title">院系管理</span>
    </div>

    <div class="search-panel">
      <el-form class="search-form" :inline="true">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.qkey"
            placeholder="院系名称或代码"
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button @click="handleAdd">新增</el-button>
          <el-button @click="handleBack">返回上级</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-panel">
      <div class="table-wrapper">
        <el-table
          :data="listData"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="cname" label="名称" min-width="180" />
          <el-table-column prop="ccode" label="代码" min-width="120" />
          <el-table-column prop="cparentid" label="上级ID" min-width="100" />

          <el-table-column label="操作" min-width="320" fixed="right">
            <template #default="scope">
              <el-button
                size="small"
                type="primary"
                @click="handleSec(scope.$index, scope.row)"
                v-if="scope.row.cparentid == 0"
              >
                下级院系
              </el-button>

              <el-button
                size="small"
                type="warning"
                @click="handleEdit(scope.$index, scope.row)"
              >
                修改院系信息
              </el-button>

              <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
              >
                删除院系信息
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

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

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="90px"
      >
        <el-form-item label="院系名称" prop="cname">
          <el-input v-model="editForm.cname" placeholder="请输入院系名称" />
        </el-form-item>

        <el-form-item label="院系代码" prop="ccode">
          <el-input v-model="editForm.ccode" placeholder="请输入院系代码" />
        </el-form-item>

        <el-form-item label="上级ID">
          <el-input v-model="editForm.cparentid" disabled />
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

/* 当前查看的上级ID，0表示顶级 */
const currentParentId = ref(0)

/* 当前层级名称栈 */
const parentStack = ref([])

/* 搜索表单 */
const searchForm = reactive({
  qkey: ""
})

/* 表格数据 */
const listData = ref([])

/* 分页 */
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

/* 弹窗 */
const dialogVisible = ref(false)
const dialogTitle = ref("")
const editFormRef = ref()

/* 编辑表单 */
const editForm = reactive({
  cid: null,
  cname: "",
  ccode: "",
  cparentid: 0
})

/* 表单校验 */
const editRules = {
  cname: [
    { required: true, message: "请输入院系名称", trigger: "blur" }
  ],
  ccode: [
    { required: true, message: "请输入院系代码", trigger: "blur" }
  ]
}

/* 重置表单 */
const resetEditForm = () => {
  editForm.cid = null
  editForm.cname = ""
  editForm.ccode = ""
  editForm.cparentid = currentParentId.value
}

/* 刷新表格 */
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await request.get("/collegeinfo/getlist", {
      params: {
        pageIndex: pagination.currentPage,
        pageSize: pagination.pageSize,
        qkey: searchForm.qkey,
        cparentid: currentParentId.value
      }
    })

    const result = res.data || res

    if (result._code === 200) {
      listData.value = result.data || []
      pagination.total = result.totalRecord || 0
    } else {
      ElMessage.error(result._msg || "获取院系列表失败")
    }
  } catch (error) {
    console.error("获取院系列表失败：", error)
    ElMessage.error("获取院系列表失败")
  } finally {
    loading.value = false
  }
}

/* 搜索 */
const handleSearch = () => {
  pagination.currentPage = 1
  loadTableData()
}

/* 新增 */
const handleAdd = () => {
  resetEditForm()
  dialogTitle.value = "新增院系"
  dialogVisible.value = true
}

/* 修改 */
const handleEdit = async (index, row) => {
  try {
    const res = await request.get(`/collegeinfo/getmodel/${row.cid}`)
    const result = res.data || res

    if (result.code === 200 || result._code === 200) {
      const model = result.data
      editForm.cid = model.cid
      editForm.cname = model.cname
      editForm.ccode = model.ccode
      editForm.cparentid = model.cparentid

      dialogTitle.value = "修改院系"
      dialogVisible.value = true
    } else {
      ElMessage.error(result.msg || result._msg || "获取院系信息失败")
    }
  } catch (error) {
    console.error("获取院系信息失败：", error)
    ElMessage.error("获取院系信息失败")
  }
}

/* 保存 */
const handleSave = async () => {
  if (!editFormRef.value) return

  await editFormRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      let res = null

      if (editForm.cid == null) {
        res = await request.post("/collegeinfo/add", {
          cname: editForm.cname,
          ccode: editForm.ccode,
          cparentid: editForm.cparentid
        })
      } else {
        res = await request.post("/collegeinfo/update", {
          cid: editForm.cid,
          cname: editForm.cname,
          ccode: editForm.ccode,
          cparentid: editForm.cparentid
        })
      }

      const result = res.data || res

      if (result.code === 200 || result._code === 200) {
        ElMessage.success(result.msg || result._msg || "保存成功")
        dialogVisible.value = false
        pagination.currentPage = 1
        loadTableData()
      } else {
        ElMessage.error(result.msg || result._msg || "保存失败")
      }
    } catch (error) {
      console.error("保存失败：", error)
      ElMessage.error("保存失败")
    }
  })
}

/* 删除 */
const handleDelete = async (index, row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除【${row.cname}】吗？`,
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    )

    const res = await request.delete(`/collegeinfo/delete/${row.cid}`)
    const result = res.data || res

    if (result.code === 200 || result._code === 200) {
      ElMessage.success(result.msg || result._msg || "删除成功")

      if (listData.value.length === 1 && pagination.currentPage > 1) {
        pagination.currentPage = pagination.currentPage - 1
      }

      loadTableData()
    } else {
      ElMessage.error(result.msg || result._msg || "删除失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除失败：", error)
    }
  }
}

/* 返回上级 */
const handleBack = () => {
  if (parentStack.value.length === 0) {
    currentParentId.value = 0
    ElMessage.warning("当前已经是顶级院系")
    return
  }

  const lastParentId = parentStack.value.pop()
  currentParentId.value = lastParentId
  pagination.currentPage = 1
  searchForm.qkey = ""
  loadTableData()
}

/* 查看下级院系 */
const handleSec = (index, row) => {
  parentStack.value.push(currentParentId.value)
  currentParentId.value = row.cid
  pagination.currentPage = 1
  searchForm.qkey = ""
  loadTableData()
}

/* 重置 */
const handleReset = () => {
  searchForm.qkey = ""
  pagination.currentPage = 1
  loadTableData()
}

/* 每页条数变化 */
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.currentPage = 1
  loadTableData()
}

/* 当前页变化 */
const handleCurrentChange = (val) => {
  pagination.currentPage = val
  loadTableData()
}

/* 初始化 */
onMounted(() => {
  loadTableData()
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
  height: 100vh;
  padding: 24px;
  background: #f0f2f5;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  overflow: hidden;
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
  transition: box-shadow 0.3s ease;
  flex-shrink: 0;
}
.search-panel:hover {
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.06);
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
  transition: box-shadow 0.3s ease;
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}
.table-panel:hover {
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.06);
}

.table-wrapper {
  flex: 1;
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
  white-space: nowrap;
}
:deep(.el-table td.el-table__cell) {
  padding: 12px 0;
  color: #3a4a5e;
  white-space: nowrap;
}
:deep(.el-table--border) {
  border-radius: 12px;
}
:deep(.el-table--border th.el-table__cell) {
  border-right: 1px solid #e9eef4;
}
:deep(.el-table--border td.el-table__cell) {
  border-right: 1px solid #e9eef4;
}

:deep(.el-button--small) {
  border-radius: 20px;
  padding: 8px 18px;
  font-weight: 500;
  transition: all 0.2s ease;
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
:deep(.el-pagination) {
  font-weight: 400;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px;
}
:deep(.el-pagination .el-select .el-input) {
  width: 100px;
}
:deep(.el-pagination__total) {
  color: #5e6e82;
  margin-right: 10px;
}
:deep(.el-pagination__sizes) {
  margin-right: 10px;
}
:deep(.el-pagination .btn-prev),
:deep(.el-pagination .btn-next) {
  background: #ffffff;
  border-radius: 8px;
  margin: 0 2px;
}
:deep(.el-pager .number) {
  background: #ffffff;
  border-radius: 8px;
  margin: 0 2px;
  min-width: 32px;
}
:deep(.el-pager .number.active) {
  background: #409eff;
  color: white;
  font-weight: 500;
}

@media (max-width: 1024px) {
  .list-container {
    padding: 20px;
  }
  .search-panel {
    padding: 20px 20px 0 20px;
  }
  .search-form .el-input {
    width: 180px;
  }
}

@media (max-width: 768px) {
  .list-container {
    padding: 16px;
  }

  .search-panel {
    padding: 16px 16px 0 16px;
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

  .table-panel {
    padding: 16px;
  }

  :deep(.el-table th.el-table__cell),
  :deep(.el-table td.el-table__cell) {
    padding: 10px 8px;
    font-size: 13px;
  }

  .pagination-panel {
    padding: 12px 16px;
    justify-content: center;
  }
  :deep(.el-pagination) {
    flex-wrap: wrap;
    justify-content: center;
  }
  :deep(.el-pagination__total),
  :deep(.el-pagination__sizes) {
    margin-right: 0;
    width: 100%;
    text-align: center;
  }
  :deep(.el-pagination__sizes .el-input) {
    width: 100px;
    margin: 0 auto;
  }
}

@media (max-width: 375px) {
  .list-container {
    padding: 12px;
  }
  .search-panel {
    padding: 12px 12px 0 12px;
  }
  .table-panel {
    padding: 12px;
  }
  .pagination-panel {
    padding: 10px 12px;
  }
}
</style>
