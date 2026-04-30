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
          />
        </el-form-item>

        <el-form-item label="类型">
          <el-select v-model="queryForm.ntype" placeholder="请选择类型" clearable style="width: 160px">
            <el-option label="普通公告" value="普通公告" />
            <el-option label="系统公告" value="系统公告" />
            <el-option label="考试通知" value="考试通知" />
            <el-option label="课程通知" value="课程通知" />
          </el-select>
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="queryForm.nstate" placeholder="请选择状态" clearable style="width: 140px">
            <el-option label="发布" :value="1" />
            <el-option label="下线" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增公告</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="70" />
        <el-table-column prop="ntitle" label="公告标题" min-width="220" show-overflow-tooltip />
        <el-table-column prop="ntype" label="类型" width="120" />
        <el-table-column label="置顶" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.ntop == 1 ? 'danger' : 'info'">
              {{ scope.row.ntop == 1 ? '置顶' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.nstate == 1 ? 'success' : 'warning'">
              {{ scope.row.nstate == 1 ? '发布' : '下线' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="ncreatename" label="发布人" width="120" />
        <el-table-column prop="ncreatetime" label="发布时间" width="180" />

        <el-table-column label="操作" width="340" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>

            <el-button
              type="warning"
              link
              @click="handleTop(scope.row)"
            >
              {{ scope.row.ntop == 1 ? '取消置顶' : '设为置顶' }}
            </el-button>

            <el-button
              type="success"
              link
              @click="handleState(scope.row)"
            >
              {{ scope.row.nstate == 1 ? '下线' : '发布' }}
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
      v-model="dialogVisible"
      :title="formData.nid ? '编辑公告' : '新增公告'"
      width="700px"
      destroy-on-close
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="公告标题" prop="ntitle">
          <el-input v-model="formData.ntitle" maxlength="200" show-word-limit />
        </el-form-item>

        <el-form-item label="公告类型" prop="ntype">
          <el-select v-model="formData.ntype" placeholder="请选择类型" style="width: 100%">
            <el-option label="普通公告" value="普通公告" />
            <el-option label="系统公告" value="系统公告" />
            <el-option label="考试通知" value="考试通知" />
            <el-option label="课程通知" value="课程通知" />
          </el-select>
        </el-form-item>

        <el-form-item label="公告内容" prop="ncontent">
          <el-input
            v-model="formData.ncontent"
            type="textarea"
            :rows="8"
            maxlength="3000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="是否置顶">
          <el-switch v-model="formData.ntop" :active-value="1" :inactive-value="0" />
        </el-form-item>

        <el-form-item label="发布状态">
          <el-switch v-model="formData.nstate" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from "vue"
import axios from "@/utils/Axios"
import { ElMessage, ElMessageBox } from "element-plus"

const loading = ref(false)
const tableData = ref([])

const pageIndex = ref(1)
const pageSize = ref(10)
const totalRecord = ref(0)

const dialogVisible = ref(false)
const formRef = ref()

const queryForm = reactive({
  qkey: "",
  ntype: "",
  nstate: null
})

const formData = reactive({
  nid: null,
  ntitle: "",
  ncontent: "",
  ntype: "普通公告",
  ntop: 0,
  nstate: 1,
  ncreatename: "管理员",
  ncreateuid: 1
})

const rules = {
  ntitle: [{ required: true, message: "请输入公告标题", trigger: "blur" }],
  ntype: [{ required: true, message: "请选择公告类型", trigger: "change" }],
  ncontent: [{ required: true, message: "请输入公告内容", trigger: "blur" }]
}

const resetFormData = () => {
  formData.nid = null
  formData.ntitle = ""
  formData.ncontent = ""
  formData.ntype = "普通公告"
  formData.ntop = 0
  formData.nstate = 1
  formData.ncreatename = "管理员"
  formData.ncreateuid = 1
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/noticeinfo/getlist", {
      params: {
        pageIndex: pageIndex.value,
        pageSize: pageSize.value,
        qkey: queryForm.qkey,
        ntype: queryForm.ntype,
        nstate: queryForm.nstate
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

const handleSearch = () => {
  pageIndex.value = 1
  loadTableData()
}

const handleReset = () => {
  queryForm.qkey = ""
  queryForm.ntype = ""
  queryForm.nstate = null
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
  formData.nid = row.nid
  formData.ntitle = row.ntitle
  formData.ncontent = row.ncontent
  formData.ntype = row.ntype
  formData.ntop = row.ntop
  formData.nstate = row.nstate
  formData.ncreatename = row.ncreatename
  formData.ncreateuid = row.ncreateuid

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

    try {
      const url = formData.nid ? "/noticeinfo/update" : "/noticeinfo/add"
      const res = await axios.post(url, formData)

      if (res.data && res.data.code === 200) {
        ElMessage.success(res.data.msg || "保存成功")
        dialogVisible.value = false
        loadTableData()
      } else {
        ElMessage.error(res.data?.msg || "保存失败")
      }
    } catch (err) {
      ElMessage.error("保存失败")
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除公告【${row.ntitle}】吗？`, "提示", {
      type: "warning"
    })

    const res = await axios.post(`/noticeinfo/delete/${row.nid}`)
    if (res.data && res.data.code === 200) {
      ElMessage.success(res.data.msg || "删除成功")
      loadTableData()
    } else {
      ElMessage.error(res.data?.msg || "删除失败")
    }
  } catch (err) {
    console.log(err)
  }
}

const handleTop = async (row) => {
  try {
    const res = await axios.post("/noticeinfo/updatetop", {
      nid: row.nid,
      ntop: row.ntop == 1 ? 0 : 1
    })

    if (res.data && res.data.code === 200) {
      ElMessage.success(res.data.msg || "操作成功")
      loadTableData()
    } else {
      ElMessage.error(res.data?.msg || "操作失败")
    }
  } catch (err) {
    ElMessage.error("操作失败")
  }
}

const handleState = async (row) => {
  try {
    const res = await axios.post("/noticeinfo/updatestate", {
      nid: row.nid,
      nstate: row.nstate == 1 ? 0 : 1
    })

    if (res.data && res.data.code === 200) {
      ElMessage.success(res.data.msg || "操作成功")
      loadTableData()
    } else {
      ElMessage.error(res.data?.msg || "操作失败")
    }
  } catch (err) {
    ElMessage.error("操作失败")
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
