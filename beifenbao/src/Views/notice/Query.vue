<template>
  <div class="page-box">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="标题">
          <el-input
            v-model="queryForm.qkey"
            placeholder="请输入公告标题"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="类型">
          <el-select
            v-model="queryForm.ntype"
            placeholder="请选择类型"
            clearable
            style="width: 160px"
          >
            <el-option label="普通公告" value="普通公告" />
            <el-option label="系统公告" value="系统公告" />
            <el-option label="考试通知" value="考试通知" />
            <el-option label="课程通知" value="课程通知" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="70" />

        <el-table-column label="标题" min-width="280" show-overflow-tooltip>
          <template #default="scope">
            <div class="title-box">
              <el-tag
                v-if="scope.row.ntop == 1"
                type="danger"
                size="small"
                class="top-tag"
              >
                置顶
              </el-tag>
              <span class="notice-title" @click="handleDetail(scope.row)">
                {{ scope.row.ntitle }}
              </span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="ntype" label="类型" width="120" />
        <el-table-column prop="ncreatename" label="发布人" width="120" />
        <el-table-column prop="ncreatetime" label="发布时间" width="180" />

        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="handleDetail(scope.row)">
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-box">
        <el-pagination
          background
          layout="total, prev, pager, next, sizes"
          :current-page="pageIndex"
          :page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="totalRecord"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="topVisible"
      title="置顶公告"
      width="900px"
      destroy-on-close
    >
      <div v-if="topNoticeList.length > 0" class="top-list-box">
        <div
          v-for="item in topNoticeList"
          :key="item.nid"
          class="top-item"
        >
          <div class="detail-header">
            <div class="detail-title-row">
              <el-tag type="danger" class="top-detail-tag">
                置顶
              </el-tag>
              <span class="detail-title">{{ item.ntitle }}</span>
            </div>

            <div class="detail-meta">
              <span>类型：{{ item.ntype || "-" }}</span>
              <span>发布人：{{ item.ncreatename || "-" }}</span>
              <span>发布时间：{{ item.ncreatetime || "-" }}</span>
            </div>
          </div>

          <el-divider />

          <div class="detail-content preview-content">
            {{ item.ncontent }}
          </div>

          <div class="top-item-btns">
            <el-button type="primary" link @click="openTopDetail(item)">
              查看详情
            </el-button>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="topVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="detailVisible"
      title="公告详情"
      width="800px"
      destroy-on-close
    >
      <div class="detail-box">
        <div class="detail-header">
          <div class="detail-title-row">
            <el-tag v-if="detailData.ntop == 1" type="danger" class="top-detail-tag">
              置顶
            </el-tag>
            <span class="detail-title">{{ detailData.ntitle }}</span>
          </div>

          <div class="detail-meta">
            <span>类型：{{ detailData.ntype || "-" }}</span>
            <span>发布人：{{ detailData.ncreatename || "-" }}</span>
            <span>发布时间：{{ detailData.ncreatetime || "-" }}</span>
          </div>
        </div>

        <el-divider />

        <div class="detail-content">
          {{ detailData.ncontent }}
        </div>
      </div>

      <template #footer>
        <el-button type="primary" @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue"
import axios from "@/utils/Axios"
import { ElMessage } from "element-plus"

const loading = ref(false)
const tableData = ref([])

const pageIndex = ref(1)
const pageSize = ref(10)
const totalRecord = ref(0)

const detailVisible = ref(false)
const topVisible = ref(false)
const topNoticeList = ref([])

const queryForm = reactive({
  qkey: "",
  ntype: ""
})

const detailData = reactive({
  nid: null,
  ntitle: "",
  ncontent: "",
  ntype: "",
  ntop: 0,
  ncreatename: "",
  ncreatetime: ""
})

const resetDetailData = () => {
  detailData.nid = null
  detailData.ntitle = ""
  detailData.ncontent = ""
  detailData.ntype = ""
  detailData.ntop = 0
  detailData.ncreatename = ""
  detailData.ncreatetime = ""
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/noticeinfo/listquery", {
      params: {
        pageIndex: pageIndex.value,
        pageSize: pageSize.value,
        qkey: queryForm.qkey,
        ntype: queryForm.ntype
      }
    })

    if (res.data && res.data._code === 200) {
      tableData.value = res.data.data || []
      totalRecord.value = res.data.totalRecord || 0
    } else {
      ElMessage.error(res.data?._msg || "获取公告列表失败")
    }
  } catch (err) {
    ElMessage.error("获取公告列表失败")
  } finally {
    loading.value = false
  }
}

const loadTopNotice = async () => {
  const userInfo = JSON.parse(localStorage.getItem("userInfo") || "null")
  const uid = userInfo?.uid || "guest"
  const shownKey = `notice_top_shown_${uid}`

  if (sessionStorage.getItem(shownKey) === "1") {
    return
  }

  try {
    const res = await axios.get("/noticeinfo/listquery", {
      params: {
        pageIndex: 1,
        pageSize: 50,
        qkey: "",
        ntype: ""
      }
    })

    if (res.data && res.data._code === 200) {
      const list = res.data.data || []
      const topList = list.filter(item => item.ntop == 1)

      if (topList.length > 0) {
        topNoticeList.value = topList
        topVisible.value = true
        sessionStorage.setItem(shownKey, "1")
      }
    }
  } catch (err) {
    console.log(err)
  }
}

const handleSearch = () => {
  pageIndex.value = 1
  loadTableData()
}

const handleReset = () => {
  queryForm.qkey = ""
  queryForm.ntype = ""
  pageIndex.value = 1
  loadTableData()
}

const handlePageChange = (val) => {
  pageIndex.value = val
  loadTableData()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  pageIndex.value = 1
  loadTableData()
}

const handleDetail = async (row) => {
  resetDetailData()
  try {
    const res = await axios.get(`/noticeinfo/detail/${row.nid}`)

    if (res.data && res.data.code === 200) {
      const data = res.data.data || {}
      detailData.nid = data.nid
      detailData.ntitle = data.ntitle
      detailData.ncontent = data.ncontent
      detailData.ntype = data.ntype
      detailData.ntop = data.ntop
      detailData.ncreatename = data.ncreatename
      detailData.ncreatetime = data.ncreatetime
      detailVisible.value = true
    } else {
      ElMessage.error(res.data?.msg || "获取公告详情失败")
    }
  } catch (err) {
    ElMessage.error("获取公告详情失败")
  }
}

const openTopDetail = (item) => {
  detailData.nid = item.nid
  detailData.ntitle = item.ntitle
  detailData.ncontent = item.ncontent
  detailData.ntype = item.ntype
  detailData.ntop = item.ntop
  detailData.ncreatename = item.ncreatename
  detailData.ncreatetime = item.ncreatetime
  topVisible.value = false
  detailVisible.value = true
}

onMounted(() => {
  loadTableData()
  loadTopNotice()
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

.title-box {
  display: flex;
  align-items: center;
  gap: 8px;
}

.top-tag {
  flex-shrink: 0;
}

.notice-title {
  color: #409eff;
  cursor: pointer;
}

.notice-title:hover {
  text-decoration: underline;
}

.detail-box {
  min-height: 260px;
}

.detail-header {
  margin-bottom: 12px;
}

.detail-title-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.top-detail-tag {
  flex-shrink: 0;
}

.detail-title {
  font-size: 20px;
  font-weight: 700;
  color: #303133;
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  color: #909399;
  font-size: 14px;
}

.detail-content {
  white-space: pre-wrap;
  line-height: 1.9;
  color: #606266;
  font-size: 15px;
}

.top-list-box {
  max-height: 520px;
  overflow-y: auto;
  padding-right: 4px;
}

.top-item {
  padding: 8px 4px 16px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 12px;
}

.top-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.preview-content {
  max-height: 120px;
  overflow: hidden;
}

.top-item-btns {
  margin-top: 12px;
  text-align: right;
}
</style>
