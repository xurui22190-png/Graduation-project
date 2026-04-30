<template>
  <div class="page-box">
    <el-card shadow="never" class="box-card">
      <template #header>
        <div class="card-header">
          <span class="title-text">我的授课管理</span>
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索课程名称、班级或编号..."
              clearable
              style="width: 350px"
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
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column prop="yall" label="学年学期" min-width="150" align="center" />
        <el-table-column prop="crname" label="课程名称" min-width="160" />
        <el-table-column prop="classname" label="授课班级" min-width="150" align="center" />
        <el-table-column prop="cgrade" label="年级" width="90" align="center" />

        <!-- <el-table-column label="当前权重(考/平/测)" width="180" align="center">
          <template #default="scope">
            <el-tag size="small" type="info">
              {{ ((scope.row.w_exam ?? scope.row.wexam ?? 0) * 100).toFixed(0) }}% /
              {{ ((scope.row.w_regular ?? scope.row.wregular ?? 0) * 100).toFixed(0) }}% /
              {{ ((scope.row.w_test ?? scope.row.wtest ?? 0) * 100).toFixed(0) }}%
            </el-tag>
          </template>
        </el-table-column> -->

        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template #default="scope">
            <el-button
              type="success"
              size="small"
              plain
              @click="openKnowledgeDrawer(scope.row)"
            >
              知识点管理
            </el-button>
            <!-- <el-button
              type="warning"
              size="small"
              plain
              @click="openWeightDialog(scope.row)"
            >
              权重设置
            </el-button> -->
          </template>
        </el-table-column>
      </el-table>

      <el-empty
        v-if="!loading && filteredTableData.length === 0"
        :description="searchKeyword ? '未找到相关授课' : '暂无授课安排，请检查接口返回'"
      />
    </el-card>

    <el-drawer
      v-model="drawerVisible"
      :title="`${currentCourseName} - 知识点维护`"
      size="35%"
      destroy-on-close
    >
      <div class="drawer-content">
        <div class="add-bar">
          <el-input
            v-model="newPointName"
            placeholder="输入新知识点（如：递归算法）"
            @keyup.enter="handleAddKnowledgePoint"
          />
          <el-button type="primary" @click="handleAddKnowledgePoint" :loading="addLoading">添加</el-button>
        </div>

        <el-table :data="knowledgeList" border stripe v-loading="knowledgeLoading">
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="pointName" label="知识点名称" />
          <el-table-column label="操作" width="80" align="center">
            <template #default="scope">
              <el-popconfirm
                title="确定删除？"
                @confirm="handleDeletePoint(scope.row.id)"
              >
                <template #reference>
                  <el-button type="danger" size="small" link>删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>

    <el-dialog
      v-model="weightDialogVisible"
      title="设置成绩核算占比"
      width="400px"
      destroy-on-close
    >
      <el-form label-width="120px" label-position="left">
        <el-form-item label="期末考试占比">
          <el-input-number v-model="weightForm.wExam" :min="0" :max="100" :step="5" /> %
        </el-form-item>
        <el-form-item label="平时成绩占比">
          <el-input-number v-model="weightForm.wRegular" :min="0" :max="100" :step="5" /> %
        </el-form-item>
        <el-form-item label="测试成绩占比">
          <el-input-number v-model="weightForm.wTest" :min="0" :max="100" :step="5" /> %
        </el-form-item>

        <div
          class="weight-status"
          :class="{ 'is-success': totalWeight === 100, 'is-error': totalWeight !== 100 }"
        >
          当前总计：{{ totalWeight }} %
          <span v-if="totalWeight !== 100">（必须等于100%）</span>
          <el-icon v-else style="margin-left:5px"><CircleCheck /></el-icon>
        </div>
      </el-form>
      <template #footer>
        <el-button @click="weightDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleSaveWeights"
          :disabled="totalWeight !== 100"
          :loading="saveWeightLoading"
        >保存设置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Search, CircleCheck } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/Axios' // 根据实际路径调整

// ==========================================
// 1. 基础列表逻辑 (修复了 Axios 嵌套包裹问题)
// ==========================================
const loading = ref(false)
const searchKeyword = ref('')
const searchLoading = ref(false)
const tableData = ref([])
const filteredTableData = ref([])

const fetchTableData = async () => {
  loading.value = true
  try {
    const response = await request.get('/teaching/getlist')

    // 🌟 核心修复：手动剥离 Axios 的 data 外壳
    const res = response.data

    if (res.code === 200 || res._code === 200) {
      // 你的9条授课记录在里层的 data 属性里
      const realData = res.data || []
      tableData.value = realData
      filteredTableData.value = realData
    } else {
      ElMessage.error(res.msg || res._msg || '获取列表失败')
    }
  } catch (error) {
    console.error("请求报错：", error)
    ElMessage.error('网络或接口异常，无法获取授课列表')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  const keyword = searchKeyword.value.toLowerCase()
  filteredTableData.value = tableData.value.filter(item =>
    (item.crName && item.crName.toLowerCase().includes(keyword)) ||
    (item.className && item.className.toLowerCase().includes(keyword)) ||
    (item.crCode && item.crCode.toLowerCase().includes(keyword))
  )
}

// ==========================================
// 2. 知识点管理逻辑 (同步修复 Axios 包裹)
// ==========================================
const drawerVisible = ref(false)
const knowledgeLoading = ref(false)
const addLoading = ref(false)
const newPointName = ref('')
const knowledgeList = ref([])
const currentCourseId = ref(null)
const currentCourseName = ref('')

const openKnowledgeDrawer = (row) => {
  currentCourseId.value = row.tcCourseId || row.tccourseid || row.courseId
  currentCourseName.value = row.crName || row.crname
  drawerVisible.value = true
  fetchKnowledgeList()
}

const fetchKnowledgeList = async () => {
  knowledgeLoading.value = true
  try {
    const response = await request.get(`/knowledge/list/${currentCourseId.value}`)
    const res = response.data // 🌟 剥离外壳

    if (res.code === 200 || res._code === 200) {
      knowledgeList.value = res.data || []
    }
  } catch (error) {
    console.error(error)
  } finally {
    knowledgeLoading.value = false
  }
}

const handleAddKnowledgePoint = async () => {
  if (!newPointName.value.trim()) return
  addLoading.value = true
  try {
    const response = await request.post('/knowledge/save', {
      courseId: currentCourseId.value,
      pointName: newPointName.value.trim()
    })
    const res = response.data // 🌟 剥离外壳

    if (res.code === 200 || res._code === 200) {
      ElMessage.success('添加成功')
      newPointName.value = ''
      fetchKnowledgeList()
    } else {
      ElMessage.error(res.msg || res._msg || '添加失败')
    }
  } finally {
    addLoading.value = false
  }
}

const handleDeletePoint = async (id) => {
  const response = await request.delete(`/knowledge/delete/${id}`)
  const res = response.data // 🌟 剥离外壳

  if (res.code === 200 || res._code === 200) {
    ElMessage.success('已删除')
    fetchKnowledgeList()
  }
}

// ==========================================
// 3. 权重设置逻辑 (同步修复 Axios 包裹)
// ==========================================
const weightDialogVisible = ref(false)
const saveWeightLoading = ref(false)
const currentTeachingId = ref(null)
const weightForm = reactive({ wExam: 0, wRegular: 0, wTest: 0 })

const totalWeight = computed(() => weightForm.wExam + weightForm.wRegular + weightForm.wTest)

// 你的代码里应该有类似这样的方法：
// 你的代码里应该有类似这样的方法：
const openWeightDialog = (row) => {
  console.log("【调试】点击的行数据：", row) // 👉 看看控制台打印出的 ID 叫 tcId 还是 tcid

  weightForm.value = {
    // 👉 兼容写法：不管后端传过来的是大写还是小写，都能拿到！
    tcId: row.tcId || row.tcid,

    w_exam: (row.w_exam || 0) * 100,
    w_regular: (row.w_regular || 0) * 100,
    w_test: (row.w_test || 0) * 100
  }
  weightDialogVisible.value = true
}

const handleSaveWeights = () => {
  // 核心逻辑：强制构造一个和后端实体类成员变量名完全一致的对象
  const submitData = {
    tcid: weightForm.value.tcId || weightForm.value.tcid,
    // 注意：这里必须和 Teaching.java 里的 private Double 后面的变量名一致
    wregular: Number(weightForm.value.w_regular) / 100,
    wtest: Number(weightForm.value.w_test) / 100,
    wexam: Number(weightForm.value.w_exam) / 100
  };

  console.log("【检查点】最终发给后端的 JSON：", JSON.stringify(submitData));

  request.post('/teaching/updateWeights', submitData).then(res => {
    if (res.code === 200 || res._code === 200) {
      ElMessage.success("保存成功！");
      weightDialogVisible.value = false;
      // 这里的报错先不管，只要弹出“保存成功”就说明数据库操作完了
      window.location.reload(); // 👈 暴力刷新整个页面，确保数据重新加载
    }
  });
};
onMounted(() => {
  fetchTableData()
})
</script>

<style scoped>
.page-box { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.title-text { font-size: 18px; font-weight: bold; }
.drawer-content { padding: 20px; }
.add-bar { display: flex; gap: 10px; margin-bottom: 20px; }
.weight-status {
  margin-top: 20px;
  padding: 12px;
  border-radius: 4px;
  text-align: right;
  font-weight: bold;
  transition: all 0.3s;
}
.is-success { background-color: #f0f9eb; color: #67c23a; }
.is-error { background-color: #fef0f0; color: #f56c6c; }
</style>
