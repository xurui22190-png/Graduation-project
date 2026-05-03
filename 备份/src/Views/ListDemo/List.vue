<template>
  <div class="list-container">
    <div class="module-panel">
      <span class="module-title">用户管理</span>
    </div>

    <div class="search-panel">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="角色">
          <el-select v-model="searchForm.urole" placeholder="全部" clearable>
            <el-option label="学生" value="1" />
            <el-option label="教师" value="2" />
          </el-select>
        </el-form-item>

        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.qkey"
            placeholder="账号或姓名"
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格卡片区域，flex:1 占满剩余高度，内部滚动 -->
    <div class="table-panel">
      <div class="table-wrapper">
        <el-table
          :data="listData"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="ulog" label="账号" min-width="120" />
          <el-table-column prop="uname" label="姓名" min-width="100" />
          <el-table-column prop="usex" label="性别" min-width="80" />

          <el-table-column prop="urole" label="角色" min-width="100">
            <template #default="scope">
              <span v-if="scope.row.urole == 1">学生</span>
              <span v-else-if="scope.row.urole == 2">教师</span>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button
                size="small"
                type="danger"
                @click="handleSetPwd(scope.$index, scope.row)"
              >
                重置密码
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
  </div>
</template>

<script setup>
import { reactive, ref } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"

const loading = ref(false)

/* 搜索表单 */
const searchForm = reactive({
  urole: "",
  qkey: ""
})

/* 原始数据 */
const sourceData = ref([
  {
    id: 1,
    ulog: "2024001",
    uname: "张三",
    usex: "男",
    urole: 1
  },
  {
    id: 2,
    ulog: "2024002",
    uname: "李四",
    usex: "女",
    urole: 2
  },
  {
    id: 3,
    ulog: "2024003",
    uname: "王五",
    usex: "男",
    urole: 1
  }
])

/* 表格数据 */
const listData = ref([...sourceData.value])

/* 分页 */
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: sourceData.value.length
})

/* 搜索 */
const handleSearch = () => {
  loading.value = true

  setTimeout(() => {
    let result = [...sourceData.value]

    // 按角色筛选
    if (searchForm.urole !== "") {
      result = result.filter(item => String(item.urole) === String(searchForm.urole))
    }

    // 按关键词筛选：账号或姓名
    if (searchForm.qkey.trim() !== "") {
      const keyword = searchForm.qkey.trim()
      result = result.filter(item =>
        item.ulog.includes(keyword) || item.uname.includes(keyword)
      )
    }

    listData.value = result
    pagination.total = result.length
    pagination.currentPage = 1

    loading.value = false
    ElMessage.success("搜索完成")
  }, 300)
}

/* 重置 */
const handleReset = () => {
  searchForm.urole = ""
  searchForm.qkey = ""

  listData.value = [...sourceData.value]
  pagination.total = sourceData.value.length
  pagination.currentPage = 1
}

/* 重置密码 */
const handleSetPwd = async (index, row) => {
  try {
    await ElMessageBox.confirm(
      `确定要重置【${row.uname}】的密码吗？`,
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    )

    console.log("重置密码：", index, row)
    ElMessage.success("密码已重置")
  } catch {
    ElMessage.info("已取消操作")
  }
}

/* 每页条数变化 */
const handleSizeChange = (val) => {
  pagination.pageSize = val
  console.log("每页条数：", val)
}

/* 当前页变化 */
const handleCurrentChange = (val) => {
  pagination.currentPage = val
  console.log("当前页：", val)
}
</script>

<style>
/* 全局重置 */
body {
  background-color: #f0f2f5;
  margin: 0;
  padding: 0;
  overflow: hidden; /* 可选，防止 body 滚动，但 .list-container 已占满视口 */
}
</style>

<style scoped>
.list-container {
  height: 100vh; /* 固定视口高度 */
  padding: 24px;
  background: #f0f2f5;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 防止容器自身滚动 */
}

/* 模块标题 */
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

/* 搜索卡片 */
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

/* 搜索表单内部间距 */
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

/* 调整 select 和 input 宽度 */
.search-form .el-select {
  width: 160px;
}
.search-form .el-input {
  width: 200px;
}

/* 表格卡片 - 占满剩余高度 */
.table-panel {
  background: #ffffff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.03);
  transition: box-shadow 0.3s ease;
  flex: 1; /* 占据剩余空间 */
  display: flex;
  flex-direction: column;
  min-height: 0; /* 允许收缩，防止溢出 */
}
.table-panel:hover {
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.06);
}

/* 表格滚动容器 */
.table-wrapper {
  flex: 1;
  overflow: auto; /* 表格内容过多时内部滚动 */
  border-radius: 12px;
}

/* 表格样式优化 */
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

/* 操作按钮 */
:deep(.el-button--small) {
  border-radius: 20px;
  padding: 8px 18px;
  font-weight: 500;
  transition: all 0.2s ease;
}
:deep(.el-button--small.el-button--danger) {
  background: #ff4d4f;
  border-color: #ff4d4f;
}
:deep(.el-button--small.el-button--danger:hover) {
  background: #ff7875;
  border-color: #ff7875;
  transform: translateY(-2px);
  box-shadow: 0 6px 14px rgba(255, 77, 79, 0.25);
}

/* 分页区域 */
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

/* ========== 响应式增强 ========== */
/* 中等屏幕（平板） */
@media (max-width: 1024px) {
  .list-container {
    padding: 20px;
  }
  .search-panel {
    padding: 20px 20px 0 20px;
  }
  .search-form .el-select {
    width: 140px;
  }
  .search-form .el-input {
    width: 180px;
  }
}

/* 小屏幕（手机） */
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
  .search-form .el-select,
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

/* 超小屏幕（如 iPhone SE） */
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
