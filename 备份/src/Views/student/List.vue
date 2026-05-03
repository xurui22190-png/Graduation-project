<template>
  <div class="student-page">
    <div class="search-panel">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="请输入学号或姓名"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="性别">
          <el-select
            v-model="searchForm.ssex"
            placeholder="请选择性别"
            clearable
            style="width: 140px"
          >
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select
            v-model="searchForm.sclassid"
            placeholder="请选择班级"
            clearable
            filterable
            style="width: 220px"
          >
            <el-option
              v-for="item in classList"
              :key="item.cId ?? item.cid"
              :label="item.cName ?? item.cname"
              :value="item.cId ?? item.cid"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增学生</el-button>

          <el-button type="warning" plain @click="uploadDialogVisible = true">
            Excel 批量导入
          </el-button>
          <el-button type="info" link @click="downloadTemplate">下载导入模板</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-panel">
      <el-table
        :data="tableData"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="sid" label="ID" width="70" align="center" />
        <el-table-column prop="sno" label="学号" min-width="120" />
        <el-table-column prop="sname" label="姓名" min-width="100" />
        <el-table-column prop="ssex" label="性别" width="80" align="center" />
        <el-table-column prop="className" label="班级" min-width="140" />
        <el-table-column prop="mname" label="专业" min-width="140" />
        <el-table-column prop="collegeName" label="学院" min-width="140" />
        <el-table-column prop="stel" label="电话" min-width="130" />
        <el-table-column prop="sidcard" label="身份证号" min-width="180" show-overflow-tooltip />
        <el-table-column prop="saddress" label="家庭住址" min-width="200" show-overflow-tooltip />

        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager-box" style="margin-top: 20px; display: flex; justify-content: flex-end;">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="pagination.currentPage"
          :page-size="pagination.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="formData.sid ? '编辑学生' : '新增学生'"
      width="760px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号" prop="sno">
              <el-input v-model="formData.sno" placeholder="请输入学号" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="sname">
              <el-input v-model="formData.sname" placeholder="请输入姓名" clearable />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="ssex">
              <el-select v-model="formData.ssex" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级" prop="sclassid">
              <el-select
                v-model="formData.sclassid"
                placeholder="请选择班级"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="item in classList"
                  :key="item.cId ?? item.cid"
                  :label="item.cName ?? item.cname"
                  :value="item.cId ?? item.cid"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="专业">
              <el-input :model-value="formData.mName" placeholder="根据班级自动带出" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="学院">
              <el-input :model-value="formData.collegeName" placeholder="根据班级自动带出" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年级">
              <el-input :model-value="formData.cGrade" placeholder="根据班级自动带出" readonly />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="电话" prop="stel">
          <el-input v-model="formData.stel" maxlength="11" placeholder="请输入电话" clearable />
        </el-form-item>

        <el-form-item label="身份证号" prop="sidcard">
          <el-input v-model="formData.sidcard" maxlength="18" placeholder="请输入身份证号" clearable />
        </el-form-item>

        <el-form-item label="家庭住址" prop="saddress">
          <el-input v-model="formData.saddress" placeholder="请输入家庭住址" clearable />
        </el-form-item>

        <el-form-item label="账号ID" prop="saccountid">
          <el-input v-model="formData.saccountid" placeholder="没有可填0" clearable />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog
      title="批量导入学生数据"
      v-model="uploadDialogVisible"
      width="400px"
      @close="handleUploadClose"
    >
      <el-upload
        ref="uploadRef"
        class="upload-demo"
        drag
        action="#"
        :http-request="customUpload"
        :before-upload="beforeUpload"
        accept=".xls,.xlsx"
        :auto-upload="true"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或 <em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip" style="color: #E6A23C; margin-top: 10px;">
            请上传符合格式的 Excel 文件。<br/>
            (表头需包含：学生姓名、登录账号、初始密码、班级ID)
          </div>
        </template>
      </el-upload>
    </el-dialog>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted, nextTick, watch } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import request from "@/utils/Axios"
// 引入 Element Plus 的图标（用于批量上传拖拽框）
import { UploadFilled } from '@element-plus/icons-vue'

// ==========================================
// 1. 响应式状态定义区域
// ==========================================
const loading = ref(false)
const saveLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref()

// 🔥 新增：批量上传弹窗控制与 DOM 引用
const uploadDialogVisible = ref(false)
const uploadRef = ref(null)

// 🔥 新增：上传时的请求头 (必须带 Token，否则拦截器会拦截 el-upload 的原生请求)
const uploadHeaders = ref({
  Authorization: localStorage.getItem('token') || ''
})

const searchForm = reactive({
  qkey: "",
  ssex: "",
  sclassid: null
})

const tableData = ref([])
const classList = ref([])

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  sid: null,
  sno: "",
  sname: "",
  ssex: "",
  sclassid: null,
  stel: "",
  sidcard: "",
  saddress: "",
  saccountid: 0,
  mName: "",
  collegeName: "",
  cGrade: ""
})

const rules = {
  sno: [
    { required: true, message: "请输入学号", trigger: "blur" }
  ],
  sname: [
    { required: true, message: "请输入姓名", trigger: "blur" }
  ],
  ssex: [
    { required: true, message: "请选择性别", trigger: "change" }
  ],
  sclassid: [
    { required: true, message: "请选择班级", trigger: "change" }
  ]
}

// ==========================================
// 2. 生命周期与监听器
// ==========================================
onMounted(() => {
  loadClassList()
  loadTableData()
})

// 监听班级变化，自动带出专业、学院、年级信息
watch(() => formData.sclassid, (newVal) => {
  if (newVal) {
    const classInfo = classList.value.find(item => (item.cId ?? item.cid) === newVal)
    if (classInfo) {
      formData.mName = classInfo.mName ?? classInfo.mname ?? ''
      formData.collegeName = classInfo.collegeName ?? ''
      formData.cGrade = classInfo.cGrade ?? classInfo.cgrade ?? ''
    }
  } else {
    formData.mName = ""
    formData.collegeName = ""
    formData.cGrade = ""
  }
})

// ==========================================
// 3. 基础业务方法 (查询、新增、编辑、删除)
// ==========================================
const loadClassList = async () => {
  try {
    const res = await request.get("/classinfo/getlist")
    const result = res.data
    if (result.code === 200 || result._code === 200) {
      classList.value = result.data
    }
  } catch (error) {
    console.error("加载班级列表失败：", error)
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.currentPage,
      limit: pagination.pageSize,
      ...searchForm
    }
    const res = await request.get("/studentinfo/getlist", { params })
    const result = res.data
    if (result.code === 200 || result._code === 200) {
      tableData.value = result.data
      pagination.total = Number(result.totalRecord || result.total)
    } else {
      ElMessage.error(result.msg || result._msg || "获取数据失败")
    }
  } catch (error) {
    console.error("加载学生数据失败：", error)
    ElMessage.error("加载学生数据失败")
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
  searchForm.ssex = ""
  searchForm.sclassid = null
  handleSearch()
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  loadTableData()
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
  loadTableData()
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(formData, {
    sid: null,
    sno: "",
    sname: "",
    ssex: "",
    sclassid: null,
    stel: "",
    sidcard: "",
    saddress: "",
    saccountid: 0,
    mName: "",
    collegeName: "",
    cGrade: ""
  })
}

const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  resetForm()
  nextTick(() => {
    Object.assign(formData, {
      ...row,
      sclassid: row.sclassId ?? row.sclassid,
      saccountid: row.saccountId ?? row.saccountid
    })
    dialogVisible.value = true
  })
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      saveLoading.value = true
      try {
        const url = formData.sid ? "/studentinfo/update" : "/studentinfo/add"
        const saveData = {
          ...formData,
          sclassId: formData.sclassid,
          saccountId: formData.saccountid
        }

        console.log("准备保存的数据：", saveData)
        const res = await request.post(url, saveData)
        const result = res.data
        console.log("保存返回：", result)

        if (result.code === 200 || result._code === 200) {
          ElMessage.success(result.msg || result._msg || "保存成功")
          dialogVisible.value = false
          await loadTableData()
        } else {
          ElMessage.error(result.msg || result._msg || "保存失败")
        }
      } catch (error) {
        console.error("保存学生失败：", error)
        ElMessage.error("保存学生失败")
      } finally {
        saveLoading.value = false
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定删除学生【${row.sname}】吗？`,
      "提示",
      { type: "warning" }
    )

    const res = await request.post("/studentinfo/delete", [row.sid])
    const result = res.data

    if (result.code === 200 || result._code === 200) {
      ElMessage.success(result.msg || result._msg || "删除成功")

      if (tableData.value.length === 1 && pagination.currentPage > 1) {
        pagination.currentPage -= 1
      }
      await loadTableData()
    } else {
      ElMessage.error(result.msg || result._msg || "删除失败")
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除失败：", error)
      ElMessage.error("删除请求失败")
    }
  }
}


// ==========================================
// 4. 🔥 批量导入相关方法 (Excel)
// ==========================================

// 导入前的格式校验
const beforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.ms-excel' ||
                  file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                  file.name.endsWith('.xls') || file.name.endsWith('.xlsx');
  if (!isExcel) {
    ElMessage.error('只能上传 .xls 或 .xlsx 格式的 Excel 文件!');
  }
  return isExcel;
}

// 导入成功后的回调逻辑
// const handleUploadSuccess = (response, uploadFile) => {
//   if (response.code === 200 || response._code === 200) {
//     ElMessage.success(response.msg || response._msg || '导入成功！');
//     uploadDialogVisible.value = false;
//     // 导入成功后刷新表格数据，返回第一页
//     pagination.currentPage = 1;
//     loadTableData();
//   } else {
//     ElMessage.error(response.msg || response._msg || '导入失败，请检查文件内容格式是否规范');
//   }
//   uploadRef.value.clearFiles(); // 清空上传记录
// }

// 导入失败的回调
// const handleUploadError = (err) => {
//   console.error("上传错误信息:", err);
//   ElMessage.error('网络或服务器异常，导入失败 (可能跨域或Token失效)');
//   uploadRef.value.clearFiles();
// }

const customUpload = async (options) => {
  try {
    // 1. 将文件装入 FormData (浏览器专门用来传文件的对象)
    const formData = new FormData()
    formData.append('file', options.file)

    // 2. 使用你的 request 发送 POST 请求 (会自动加上你的 /api 前缀和 token)
    const res = await request.post('/studentinfo/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    const result = res.data
    if (result.code === 200 || result._code === 200) {
      ElMessage.success(result.msg || result._msg || '导入成功！')
      uploadDialogVisible.value = false // 关弹窗
      pagination.currentPage = 1 // 回到第一页
      await loadTableData() // 刷新表格数据
    } else {
      ElMessage.error(result.msg || result._msg || '导入失败，请检查文件格式')
    }
  } catch (error) {
    console.error("上传错误信息:", error)
    ElMessage.error('网络或服务器异常，导入失败')
  } finally {
    // 无论成功失败，都清空上传列表
    if (uploadRef.value) {
      uploadRef.value.clearFiles()
    }
  }
}
// 关闭弹窗时清空文件列表
const handleUploadClose = () => {
  uploadRef.value?.clearFiles()
}

// 下载模板按钮点击事件
const downloadTemplate = () => {
  // 生产环境可以放一个真实的静态文件地址在 public 目录下
  // 比如 window.open('/template/学生导入模板.xlsx')
  ElMessage.info('请确保你的Excel表头依次为：[学生姓名], [登录账号], [初始密码], [班级ID]');
}

</script>
<style scoped>
.student-page {
  padding: 16px;
  background: #f5f7fa;
  min-height: 100%;
  box-sizing: border-box;
}

.search-panel,
.table-panel {
  background: #ffffff;
  border-radius: 8px;
  padding: 16px;
  box-sizing: border-box;
}

.search-panel {
  margin-bottom: 16px;
}

.pager-box {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

:deep(.el-dialog__body) {
  padding-top: 10px;
}
</style>
