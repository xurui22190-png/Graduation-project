<template>
  <div class="page-box">
    <div class="search-box">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="输入工号/姓名/电话/毕业院校"
            clearable
            @keyup.enter="loadTableData"
          />
        </el-form-item>

        <el-form-item label="性别">
          <el-select
            v-model="searchForm.tsex"
            placeholder="请选择"
            clearable
            style="width: 120px;"
          >
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>

        <el-form-item label="学历">
          <el-select
            v-model="searchForm.tedulevel"
            placeholder="请选择"
            clearable
            style="width: 140px;"
          >
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
            <el-option label="博士" value="博士" />
          </el-select>
        </el-form-item>

        <el-form-item label="院系">
          <el-select
            v-model="searchForm.tcollegeid"
            placeholder="请选择院系"
            clearable
            style="width: 180px;"
          >
            <el-option label="全部" :value="0" />
            <el-option
              v-for="item in collegeList"
              :key="item.cid"
              :label="item.cname"
              :value="item.cid"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="loadTableData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="openAdd">新增教师</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-box">
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="tno" label="工号" min-width="120" />
        <el-table-column prop="tname" label="姓名" min-width="100" />
        <el-table-column prop="tsex" label="性别" width="80" />
        <el-table-column prop="ttel" label="电话" min-width="130" />
        <el-table-column prop="tedulevel" label="学历" min-width="100" />
        <el-table-column prop="tschool" label="毕业院校" min-width="160" />
        <el-table-column prop="collegename" label="所属院系" min-width="140" />
        <el-table-column prop="cnamet" label="上级院系" min-width="140" />
        <el-table-column prop="taddress" label="现居地址" min-width="180" />
        <el-table-column prop="taccountid" label="账号ID" min-width="100" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="openEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="removeData(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="pager-box">
      <el-pagination
        background
        layout="total, prev, pager, next, sizes"
        :current-page="pagination.currentPage"
        :page-size="pagination.pageSize"
        :page-sizes="[10, 20, 30, 50]"
        :total="pagination.total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      destroy-on-close
      @closed="handleDialogClosed"
    >
      <el-form :model="editForm" :rules="rules" ref="editFormRef" label-width="90px">
        <el-form-item label="工号" prop="tno">
          <el-input v-model="editForm.tno" />
        </el-form-item>

        <el-form-item label="姓名" prop="tname">
          <el-input v-model="editForm.tname" />
        </el-form-item>

        <el-form-item label="性别" prop="tsex">
          <el-select v-model="editForm.tsex" placeholder="请选择" style="width: 100%;">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>

        <el-form-item label="电话" prop="ttel">
          <el-input v-model="editForm.ttel" />
        </el-form-item>

        <el-form-item label="学历" prop="tedulevel">
          <el-select v-model="editForm.tedulevel" placeholder="请选择" style="width: 100%;">
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
            <el-option label="博士" value="博士" />
          </el-select>
        </el-form-item>

        <el-form-item label="毕业院校" prop="tschool">
          <el-input v-model="editForm.tschool" />
        </el-form-item>

        <el-form-item label="所属院系" prop="tcollegeid">
          <el-select v-model="editForm.tcollegeid" placeholder="请选择院系" style="width: 100%;">
            <el-option
              v-for="item in collegeList"
              :key="item.cid"
              :label="item.cname"
              :value="item.cid"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="现居地址" prop="taddress">
          <el-input v-model="editForm.taddress" />
        </el-form-item>

        <el-form-item label="账号ID" prop="taccountid">
          <el-input v-model="editForm.taccountid" disabled />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveData">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from "vue"
import axios from "@/utils/Axios"
import { ElMessage, ElMessageBox } from "element-plus"

const loading = ref(false)
const tableData = ref([])
const collegeList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref("新增教师")
const editFormRef = ref()

const searchForm = reactive({
  qkey: "",
  tsex: "",
  tedulevel: "",
  tcollegeid: 0
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const editForm = reactive({
  tid: null,
  tno: "",
  tname: "",
  tsex: "",
  ttel: "",
  tedulevel: "",
  tschool: "",
  taddress: "",
  taccountid: null,
  tcollegeid: null
})

const rules = {
  tno: [{ required: true, message: "请输入工号", trigger: "blur" }],
  tname: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  tsex: [{ required: true, message: "请选择性别", trigger: "change" }],
  tcollegeid: [{ required: true, message: "请选择院系", trigger: "change" }]
}

const getResCode = (res) => {
  return res?.data?.code ?? res?.data?._code
}

const getResMsg = (res, defaultMsg = "") => {
  return res?.data?.msg || res?.data?._msg || defaultMsg
}

const resetEditForm = () => {
  editForm.tid = null
  editForm.tno = ""
  editForm.tname = ""
  editForm.tsex = ""
  editForm.ttel = ""
  editForm.tedulevel = ""
  editForm.tschool = ""
  editForm.taddress = ""
  editForm.taccountid = ""
  editForm.tcollegeid = null
}

const handleDialogClosed = () => {
  if (editFormRef.value) {
    editFormRef.value.clearValidate()
  }
}

const loadCollegeList = async () => {
  try {
    const res = await axios.get("/collegeinfo/getall")
    const code = getResCode(res)

    if (code === 200) {
      collegeList.value = res.data.data || []
    } else {
      collegeList.value = []
      ElMessage.error(getResMsg(res, "获取院系列表失败"))
    }
  } catch (error) {
    console.error("获取院系列表失败：", error)
    collegeList.value = []
    ElMessage.error("获取院系列表失败")
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/teacherinfo/getlist", {
      params: {
        pageIndex: pagination.currentPage,
        pageSize: pagination.pageSize,
        qkey: searchForm.qkey,
        tsex: searchForm.tsex,
        tedulevel: searchForm.tedulevel,
        tcollegeid: searchForm.tcollegeid
      }
    })

    const code = getResCode(res)
    if (code === 200) {
      tableData.value = res.data.data || []
      pagination.total = res.data.totalRecord || 0
    } else {
      tableData.value = []
      pagination.total = 0
      ElMessage.error(getResMsg(res, "获取教师列表失败"))
    }
  } catch (error) {
    console.error("获取教师列表失败：", error)
    tableData.value = []
    pagination.total = 0
    ElMessage.error("获取教师列表失败")
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.qkey = ""
  searchForm.tsex = ""
  searchForm.tedulevel = ""
  searchForm.tcollegeid = 0
  pagination.currentPage = 1
  loadTableData()
}

const handleCurrentChange = (page) => {
  pagination.currentPage = page
  loadTableData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  loadTableData()
}

const openAdd = async () => {
  dialogTitle.value = "新增教师"
  resetEditForm()
  dialogVisible.value = true
  await nextTick()
  if (editFormRef.value) {
    editFormRef.value.clearValidate()
  }
}

const openEdit = async (row) => {
  dialogTitle.value = "编辑教师"
  resetEditForm()

  editForm.tid = row.tid
  editForm.tno = row.tno
  editForm.tname = row.tname
  editForm.tsex = row.tsex
  editForm.ttel = row.ttel
  editForm.tedulevel = row.tedulevel
  editForm.tschool = row.tschool
  editForm.taddress = row.taddress
  editForm.taccountid = row.taccountid
  editForm.tcollegeid = row.tcollegeid

  dialogVisible.value = true
  await nextTick()
  if (editFormRef.value) {
    editFormRef.value.clearValidate()
  }
}

const saveData = async () => {
  if (!editFormRef.value) return

  try {
    await editFormRef.value.validate()

    let res
    if (editForm.tid) {
      res = await axios.put("/teacherinfo/update", editForm)
    } else {
      res = await axios.post("/teacherinfo/save", editForm)
    }

    const code = getResCode(res)
    if (code === 200) {
      ElMessage.success(getResMsg(res, "保存成功"))
      dialogVisible.value = false
      loadTableData()
    } else {
      ElMessage.error(getResMsg(res, "保存失败"))
    }
  } catch (error) {
    console.error("保存教师失败：", error)
    ElMessage.error("保存教师失败")
  }
}

const removeData = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除教师【${row.tname}】吗？`, "提示", {
      type: "warning"
    })

    const res = await axios.delete("/teacherinfo/delete", {
      params: { id: row.tid }
    })

    const code = getResCode(res)
    if (code === 200) {
      ElMessage.success(getResMsg(res, "删除成功"))
      loadTableData()
    } else {
      ElMessage.error(getResMsg(res, "删除失败"))
    }
  } catch (error) {
    if (error !== "cancel" && error !== "close") {
      console.error("删除教师失败：", error)
      ElMessage.error("删除教师失败")
    }
  }
}

onMounted(() => {
  loadCollegeList()
  loadTableData()
})
</script>

<style scoped>
.page-box {
  padding: 15px;
}

.search-box {
  background: #fff;
  padding: 15px 15px 0;
  margin-bottom: 15px;
  border-radius: 6px;
}

.table-box {
  background: #fff;
  padding: 15px;
  border-radius: 6px;
}

.pager-box {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>
