<template>
  <div class="page-box">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.qkey"
            placeholder="请输入学号/姓名"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="学期">
          <el-select
            v-model="searchForm.sctermid"
            placeholder="请选择学期"
            clearable
            filterable
            style="width: 180px"
            @change="handleTermChange"
          >
            <el-option
              v-for="item in termList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select
            v-model="searchForm.scclassid"
            placeholder="请选择班级"
            clearable
            filterable
            style="width: 180px"
            @change="handleClassChange"
          >
            <el-option
              v-for="item in classList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="课程">
          <el-select
            v-model="searchForm.sccourseid"
            placeholder="请选择课程"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in courseList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="录入状态">
          <el-select
            v-model="searchForm.scstatus"
            placeholder="请选择状态"
            clearable
            style="width: 150px"
          >
            <el-option label="已录入" :value="1" />
            <el-option label="未录入" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleOpenEntryDialog">成绩录入</el-button>

        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="sno" label="学号" min-width="120" />
        <el-table-column prop="sname" label="姓名" min-width="100" />
        <el-table-column prop="cname" label="班级" min-width="140" />
        <el-table-column prop="crname" label="课程" min-width="140" />
        <el-table-column prop="yall" label="学期" min-width="140" />
        <el-table-column prop="tname" label="教师" min-width="100" />

        <el-table-column label="成绩" min-width="90">
          <template #default="scope">
            <span v-if="hasScore(scope.row.scscore)">
              {{ scope.row.scscore }}
            </span>
            <span v-else class="empty-text">未填写</span>
          </template>
        </el-table-column>

        <el-table-column label="状态" min-width="90">
          <template #default="scope">
            <el-tag v-if="Number(scope.row.scstatus) === 1" type="success">
              已录入
            </el-tag>
            <el-tag v-else type="info">
              未录入
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" min-width="120" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-if="hasScore(scope.row.scscore)"
              type="warning"
              size="small"
              plain
              @click="showRadarDialog(scope.row)"
            >
              学情诊断
            </el-button>
          </template>
        </el-table-column>

        <el-table-column prop="sccreatedate" label="登记时间" min-width="180" />
      </el-table>

      <div class="pager-box">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="pageIndex"
          :page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      title="成绩录入"
      width="980px"
      destroy-on-close
    >
      <el-form :inline="true" :model="entryForm" class="entry-form">
        <el-form-item label="学期">
          <el-select
            v-model="entryForm.sctermid"
            placeholder="请选择学期"
            clearable
            filterable
            style="width: 180px"
            @change="handleEntryTermChange"
          >
            <el-option
              v-for="item in entryTermList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="班级">
          <el-select
            v-model="entryForm.scclassid"
            placeholder="请选择班级"
            clearable
            filterable
            style="width: 180px"
            @change="handleEntryClassChange"
          >
            <el-option
              v-for="item in entryClassList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="课程">
          <el-select
            v-model="entryForm.sccourseid"
            placeholder="请选择课程"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in entryCourseList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="loadStudentScoreList">
            加载学生
          </el-button>
        </el-form-item>
        <el-form-item>
  <el-upload
    action=""
    :show-file-list="false"
    :http-request="handleExcelImport"
    accept=".xlsx, .xls"
  >
    <el-button type="success" :loading="uploadLoading">
      <el-icon style="margin-right: 4px"><Upload /></el-icon> Excel 批量导入
    </el-button>
  </el-upload>
</el-form-item>
      </el-form>

      <el-table
        v-loading="studentLoading"
        :data="studentTableData"
        border
        stripe
        max-height="420"
      >
        <el-table-column prop="sno" label="学号" min-width="120" />
        <el-table-column prop="sname" label="姓名" min-width="100" />
        <el-table-column prop="cname" label="班级" min-width="140" />

        <el-table-column label="成绩" min-width="140">
          <template #default="scope">
            <el-input-number
              v-model="scope.row.scscore"
              :min="0"
              :max="100"
              :precision="2"
              controls-position="right"
              style="width: 120px"
            />
          </template>
        </el-table-column>

        <el-table-column label="状态" min-width="120">
          <template #default="scope">
            <el-select v-model="scope.row.scstatus" style="width: 100px">
              <el-option label="未录入" :value="0" />
              <el-option label="已录入" :value="1" />
            </el-select>
          </template>
        </el-table-column>
      </el-table>

      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleSaveScore">保存成绩</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="radarDialogVisible"
      title="智能学情弱项诊断"
      width="700px"
      @opened="initEcharts"
    >
      <div v-loading="radarLoading">
        <div id="radarChart" style="width: 100%; height: 350px;"></div>
        <el-card shadow="hover" style="margin-top: 15px; background-color: #f8fafd; border: 1px solid #e1eaf4;">
          <template #header>
            <div style="font-weight: bold; color: #409EFF; font-size: 15px;">本地大模型专家诊断建议</div>
          </template>
          <div style="font-size: 14px; line-height: 1.8; color: #303133; white-space: pre-wrap; min-height: 60px;">
            {{ aiReportText || '点击下方按钮，召唤 AI 为您生成专属诊断报告...' }}
          </div>
        </el-card>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" :loading="isGeneratingAi" @click="generateAiReport">
            {{ isGeneratingAi ? 'AI 深度思考中...' : '生成 AI 诊断报告' }}
          </el-button>
          <el-button @click="radarDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
      v-model="classAiDialogVisible"
      title="班级成绩大盘与走势预测"
      width="650px"
    >
      <div v-loading="isGeneratingClassAi" style="min-height: 150px;">
        <el-card shadow="never" style="background-color: #f4f4f5; border: none; font-size: 15px; line-height: 1.8; color: #303133; white-space: pre-wrap;">
          <div style="margin-bottom: 10px; font-weight: bold; color: #626aef;">本地大模型 Qwen 教研分析：</div>
          {{ classAiReportText }}
        </el-card>
      </div>
      <template #footer>
        <el-button @click="classAiDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
// 完全保留你原有的导入
import { ref, reactive, computed, onMounted, nextTick } from "vue"
import axios from "@/utils/Axios"
import { ElMessage } from "element-plus"
import * as echarts from 'echarts' // 仅增加 echarts 绘图库
import { Upload } from '@element-plus/icons-vue'
const uploadLoading = ref(false)
const loading = ref(false)
const studentLoading = ref(false)
const dialogVisible = ref(false)

// 【新增的 AI 相关变量，绝不干扰原有变量】
const radarDialogVisible = ref(false)
const radarLoading = ref(false)
const currentStudentId = ref(null)
const currentCourseId = ref(null)
let radarInstance = null
const isGeneratingAi = ref(false)
const aiReportText = ref("")

const classAiDialogVisible = ref(false)
const isGeneratingClassAi = ref(false)
const classAiReportText = ref("")

const tableData = ref([])
const total = ref(0)
const pageIndex = ref(1)
const pageSize = ref(10)

const taskList = ref([])
const studentTableData = ref([])

const searchForm = reactive({
  qkey: "",
  sctermid: null,
  scclassid: null,
  sccourseid: null,
  scstatus: null
})

const entryForm = reactive({
  sctermid: null,
  scclassid: null,
  sccourseid: null
})

// =================== 完全保留你原版的核心业务代码 ===================

const hasScore = (score) => {
  return score !== null && score !== undefined && score !== ""
}

const formatScoreValue = (score) => {
  if (score === null || score === undefined || score === "") {
    return null
  }
  const num = Number(score)
  if (Number.isNaN(num)) {
    return null
  }
  return num
}

const formatStatusValue = (status) => {
  if (status === 1 || status === "1") {
    return 1
  }
  return 0
}

const buildUniqueOptions = (list, valueKey, labelKey) => {
  const map = new Map()
  ;(list || []).forEach((item) => {
    const value = item[valueKey]
    const label = item[labelKey]
    if (value !== null && value !== undefined && value !== "" && !map.has(value)) {
      map.set(value, {
        value,
        label
      })
    }
  })
  return Array.from(map.values())
}

const filteredTaskList = computed(() => {
  let arr = [...taskList.value]
  if (searchForm.sctermid) {
    arr = arr.filter((item) => item.tctermid === searchForm.sctermid)
  }
  if (searchForm.scclassid) {
    arr = arr.filter((item) => item.tcclassid === searchForm.scclassid)
  }
  return arr
})

const termList = computed(() => {
  return buildUniqueOptions(taskList.value, "tctermid", "yall")
})

const classList = computed(() => {
  let arr = [...taskList.value]
  if (searchForm.sctermid) {
    arr = arr.filter((item) => item.tctermid === searchForm.sctermid)
  }
  return buildUniqueOptions(arr, "tcclassid", "cname")
})

const courseList = computed(() => {
  return buildUniqueOptions(filteredTaskList.value, "tccourseid", "crname")
})

const entryTermList = computed(() => {
  return buildUniqueOptions(taskList.value, "tctermid", "yall")
})

const entryClassList = computed(() => {
  let arr = [...taskList.value]
  if (entryForm.sctermid) {
    arr = arr.filter((item) => item.tctermid === entryForm.sctermid)
  }
  return buildUniqueOptions(arr, "tcclassid", "cname")
})

const entryCourseList = computed(() => {
  let arr = [...taskList.value]
  if (entryForm.sctermid) {
    arr = arr.filter((item) => item.tctermid === entryForm.sctermid)
  }
  if (entryForm.scclassid) {
    arr = arr.filter((item) => item.tcclassid === entryForm.scclassid)
  }
  return buildUniqueOptions(arr, "tccourseid", "crname")
})

const loadTaskList = async () => {
  try {
    const res = await axios.get("/teacherscore/gettasklist")
    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    if (code === 200) {
      taskList.value = resp.data || []
    } else {
      ElMessage.error(resp.msg || resp._msg || "获取授课任务失败")
    }
  } catch (error) {
    console.error("获取授课任务失败：", error)
    ElMessage.error("获取授课任务失败")
  }
}

const loadTableData = async () => {
  loading.value = true
  try {
    const res = await axios.get("/teacherstudent/getlist", {
      params: {
        pageIndex: pageIndex.value,
        pageSize: pageSize.value,
        qkey: searchForm.qkey,
        sctermid: searchForm.sctermid ?? "",
        scclassid: searchForm.scclassid ?? "",
        sccourseid: searchForm.sccourseid ?? "",
        scstatus: searchForm.scstatus ?? ""
      }
    })
    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    if (code === 200) {
      tableData.value = resp.data || []
      total.value = resp.totalRecord || 0
    } else {
      ElMessage.error(resp.msg || resp._msg || "获取成绩列表失败")
    }
  } catch (error) {
    console.error("获取成绩列表失败：", error)
    ElMessage.error("获取成绩列表失败")
  } finally {
    loading.value = false
  }
}

const loadStudentScoreList = async () => {
  if (!entryForm.sctermid || !entryForm.scclassid || !entryForm.sccourseid) {
    ElMessage.warning("请先选择学期、班级和课程")
    return
  }
  studentLoading.value = true
  try {
    const res = await axios.get("/teacherscore/getstudents", {
      params: {
        sctermid: entryForm.sctermid,
        scclassid: entryForm.scclassid,
        sccourseid: entryForm.sccourseid
      }
    })
    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    const msg = resp.msg ?? resp._msg ?? "获取学生成绩列表失败"
    const list = Array.isArray(resp.data) ? resp.data : []
    if (code === 200) {
      studentTableData.value = list
        .filter((item) => item && typeof item === "object")
        .map((item) => ({
          sid: item.sid ?? null,
          sno: item.sno ?? "",
          sname: item.sname ?? "",
          sclassid: item.sclassid ?? null,
          cname: item.cname ?? "",
          scid: item.scid ?? null,
          scscore: formatScoreValue(item.scscore),
          scstatus: formatStatusValue(item.scstatus)
        }))
    } else {
      ElMessage.error(msg)
    }
  } catch (error) {
    console.error("获取学生成绩列表失败：", error)
    ElMessage.error("获取学生成绩列表失败")
  } finally {
    studentLoading.value = false
  }
}

const handleSearch = () => {
  pageIndex.value = 1
  loadTableData()
}

const handleReset = () => {
  searchForm.qkey = ""
  searchForm.sctermid = null
  searchForm.scclassid = null
  searchForm.sccourseid = null
  searchForm.scstatus = null
  pageIndex.value = 1
  loadTableData()
}

const handleTermChange = () => {
  searchForm.scclassid = null
  searchForm.sccourseid = null
}

const handleClassChange = () => {
  searchForm.sccourseid = null
}

const handleCurrentChange = (val) => {
  pageIndex.value = val
  loadTableData()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  pageIndex.value = 1
  loadTableData()
}

const handleOpenEntryDialog = () => {
  entryForm.sctermid = searchForm.sctermid || null
  entryForm.scclassid = searchForm.scclassid || null
  entryForm.sccourseid = searchForm.sccourseid || null
  studentTableData.value = []
  dialogVisible.value = true
}

const handleEntryTermChange = () => {
  entryForm.scclassid = null
  entryForm.sccourseid = null
  studentTableData.value = []
}

const handleEntryClassChange = () => {
  entryForm.sccourseid = null
  studentTableData.value = []
}

const handleSaveScore = async () => {
  // 1. 基础校验：确保已选择上下文并加载了数据
  if (!entryForm.sctermid || !entryForm.scclassid || !entryForm.sccourseid) {
    ElMessage.warning("请先选择学期、班级和课程");
    return;
  }
  if (!studentTableData.value || studentTableData.value.length === 0) {
    ElMessage.warning("请先加载学生数据");
    return;
  }

  // 2. 组装后端实体类需要的标准数据列表
  const scoreList = [];
  for (const item of studentTableData.value) {
    if (!item || !item.sid) {
      continue;
    }

    // 格式化当前行的分数和状态
    const scoreValue = formatScoreValue(item.scscore);
    const statusValue = formatStatusValue(item.scstatus);

    // 校验总分范围
    if (scoreValue !== null && (scoreValue < 0 || scoreValue > 100)) {
      ElMessage.warning(`${item.sname || item.sno} 的成绩必须在 0 到 100 之间`);
      return;
    }

    // 💡 关键修改：这里的字段必须严格对应你后端 Scoreinfo 实体类！
    // 因为你后端的保存接口直接接收 List<Scoreinfo>
    scoreList.push({
      scid: item.scid ?? null,          // 成绩主键（如果有的话，用于更新）
      scstudentid: item.sid,            // 学生ID
      sctermid: entryForm.sctermid,     // 学期ID
      scclassid: entryForm.scclassid,   // 班级ID
      sccourseid: entryForm.sccourseid, // 课程ID
      scscore: scoreValue,              // 总分
      scstatus: statusValue             // 状态
    });
  }

  // 3. 发送网络请求
  try {
    studentLoading.value = true;

    // 💡 关键修改：直接把这个 scoreList 发给咱们新写的带魔法算法的接口
    // 注意检查你的接口前缀，如果是 /api，记得加上
    const res = await axios.post("/scoreinfo/saveScoreList", scoreList);

    const resp = res?.data || {};
    const code = resp.code ?? resp._code;
    const msg = resp.msg ?? resp._msg ?? "保存失败";

    // 4. 处理响应结果
    if (code === 200) {
      ElMessage.success(msg || "成绩保存并智能拆解成功！");
      // 重新刷新背后的看板数据
      await loadStudentScoreList();
      await loadTableData();
      // 如果是在弹窗里录入，这一步可以关掉弹窗
      // dialogVisible.value = false;
    } else {
      ElMessage.error(msg);
    }
  } catch (error) {
    console.error("保存成绩失败：", error);
    ElMessage.error("网络请求异常或后端服务未启动");
  } finally {
    studentLoading.value = false;
  }
};

// =================== 【下方为完全独立新增的 AI 逻辑，不干扰原逻辑】 ===================

const showRadarDialog = (row) => {
  currentStudentId.value = row.sid
  currentCourseId.value = row.crid || searchForm.sccourseid || 4 // 兼容取值
  aiReportText.value = ""
  radarDialogVisible.value = true
}

const initEcharts = async () => {
  radarLoading.value = true
  try {
    const res = await axios.get('/diagnosis/getRadarData', {
      params: { studentId: currentStudentId.value, courseId: currentCourseId.value }
    })
    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    if (code === 200 && resp.data) {
      await nextTick()
      const chartDom = document.getElementById('radarChart')
      if (radarInstance) radarInstance.dispose()
      radarInstance = echarts.init(chartDom)
      const option = {
        title: { text: '个人知识点能力画像', left: 'center' },
        tooltip: { trigger: 'item' },
        radar: {
          indicator: resp.data.indicator,
          shape: 'circle',
          axisName: { color: 'rgb(38, 38, 38)' }
        },
        series: [{
          type: 'radar',
          data: [{
            value: resp.data.data,
            name: '得分率',
            areaStyle: { color: 'rgba(64, 158, 255, 0.3)' },
            itemStyle: { color: '#409EFF' }
          }]
        }]
      }
      radarInstance.setOption(option)
    }
  } catch (error) {
    console.error("雷达图获取失败", error)
  } finally {
    radarLoading.value = false
  }
}

const generateAiReport = async () => {
  isGeneratingAi.value = true
  aiReportText.value = "AI 教授正在飞速思考，为您深度剖析该生学情，请稍候..."
  try {
    const res = await axios.get('/diagnosis/getAiReport', {
      params: { studentId: currentStudentId.value, courseId: currentCourseId.value }
    })
    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    if (code === 200) {
      aiReportText.value = resp.data
      ElMessage.success("AI 诊断报告已生成！")
    } else {
      aiReportText.value = "生成失败，请重试。"
    }
  } catch (error) {
    aiReportText.value = "网络请求失败，无法连接到 AI 引擎。"
  } finally {
    isGeneratingAi.value = false
  }
}

const handleClassDiagnosis = async () => {
  if (!searchForm.scclassid || !searchForm.sccourseid) {
    ElMessage.warning("请先在上方筛选框中选择具体的【班级】和【课程】！")
    return
  }
  classAiDialogVisible.value = true
  isGeneratingClassAi.value = true
  classAiReportText.value = "AI 教研室主任正在扫描全班成绩，计算标准差与及格率，预测后续走势，请稍候..."
  try {
    const res = await axios.get('/diagnosis/getClassAiReport', {
      params: { classId: searchForm.scclassid, courseId: searchForm.sccourseid }
    })
    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    if (code === 200) {
      classAiReportText.value = resp.data
      ElMessage.success("班级走势预测生成成功！")
    } else {
      classAiReportText.value = "分析失败：" + (resp.msg || resp._msg || "未知错误")
    }
  } catch (e) {
    classAiReportText.value = "连接大模型失败，请检查后端和 Python 微服务状态。"
  } finally {
    isGeneratingClassAi.value = false
  }
}

// Excel 自定义上传逻辑
const handleExcelImport = async (options) => {
  // 必须先选择好学期、班级、课程
  if (!entryForm.sctermid || !entryForm.scclassid || !entryForm.sccourseid) {
    ElMessage.warning("请先在左侧选择要导入的学期、班级和课程！")
    return
  }

  const formData = new FormData()
  formData.append('file', options.file)
  formData.append('sctermid', entryForm.sctermid)
  formData.append('scclassid', entryForm.scclassid)
  formData.append('sccourseid', entryForm.sccourseid)

  try {
    uploadLoading.value = true
    // 💡 请求刚刚写好的后端接口
    const res = await axios.post("/scoreinfo/importScores", formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    const resp = res?.data || {}
    const code = resp.code ?? resp._code
    if (code === 200) {
      ElMessage.success(resp.msg || resp._msg || "批量导入并智能拆解成功！")
      // 刷新列表数据
      await loadStudentScoreList()
      await loadTableData()
    } else {
      ElMessage.error(resp.msg || resp._msg || "导入失败")
    }
  } catch (error) {
    console.error(error)
    ElMessage.error("网络异常或文件格式不正确")
  } finally {
    uploadLoading.value = false
  }
}

onMounted(async () => {
  await loadTaskList()
  await loadTableData()
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
  margin-bottom: 12px;
}

.pager-box {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.entry-form {
  margin-bottom: 16px;
}

.empty-text {
  color: #999;
}
</style>
