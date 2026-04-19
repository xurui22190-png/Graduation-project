<template>
  <div class="page-box">
    <el-card shadow="never" class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的授课</span>
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="请输入课程名称、班级名称或课程编号进行搜索"
              clearable
              style="width: 300px"
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
              <template #append>
                <el-button @click="handleSearch" :loading="searchLoading">搜索</el-button>
              </template>
            </el-input>
          </div>
        </div>
      </template>

      <el-table
        :data="filteredTableData"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="yall" label="学年学期" min-width="160" align="center" />
        <el-table-column prop="crcode" label="课程编号" min-width="120" align="center" />
        <el-table-column prop="crname" label="课程名称" min-width="160" align="center" />
        <el-table-column prop="classname" label="授课班级" min-width="160" align="center" />
        <el-table-column prop="classcode" label="班级编号" min-width="140" align="center" />
        <el-table-column prop="cgrade" label="年级" width="100" align="center" />
      </el-table>

      <el-empty
        v-if="!loading && filteredTableData.length === 0"
        :description="searchKeyword ? '未找到相关授课信息' : '暂无授课安排'"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue"
import axios from "@/utils/Axios"
import { ElMessage } from "element-plus"
import { Search } from "@element-plus/icons-vue"

const loading = ref(false)
const searchLoading = ref(false)
const tableData = ref([])
const searchKeyword = ref("")

// 过滤后的数据
const filteredTableData = computed(() => {
  if (!searchKeyword.value.trim()) {
    return tableData.value
  }

  const keyword = searchKeyword.value.trim().toLowerCase()
  return tableData.value.filter(item => {
    return (
      (item.crname && item.crname.toLowerCase().includes(keyword)) ||
      (item.classname && item.classname.toLowerCase().includes(keyword)) ||
      (item.crcode && item.crcode.toLowerCase().includes(keyword)) ||
      (item.classcode && item.classcode.toLowerCase().includes(keyword)) ||
      (item.yall && item.yall.toLowerCase().includes(keyword)) ||
      (item.cgrade && item.cgrade.toString().includes(keyword))
    )
  })
})

// 处理搜索
const handleSearch = () => {
  // 搜索逻辑已在 computed 中实现，这里只是为了配合 UI 交互
  // 可以添加一些搜索统计提示
  if (searchKeyword.value.trim()) {
    const resultCount = filteredTableData.value.length
    ElMessage.info(`找到 ${resultCount} 条相关记录`)
  }
}

// 加载数据
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/teaching/mylist")
    if (res.data.code === 200) {
      tableData.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || "获取授课信息失败")
    }
  } catch (error) {
    console.error("获取授课信息失败：", error)
    ElMessage.error("获取授课信息失败")
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadTableData()
})
</script>

<style scoped>
.page-box {
  padding: 10px;
}

.box-card {
  min-height: 500px;
}

.card-header {
  font-size: 16px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.search-box {
  font-weight: normal;
}
</style>
