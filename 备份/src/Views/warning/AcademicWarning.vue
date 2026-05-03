<template>
  <div class="warning-container">
    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar-flex">
        <div class="title-box">
          <el-icon color="#F56C6C" :size="24" class="mr-2"><Warning /></el-icon>
          <span class="title-text">AI 学业风险预警大盘</span>
        </div>
        <div class="action-box">

          <span class="label-text">学期：</span>
          <el-select
            v-model="scanSemesterId"
            placeholder="选择学期"
            filterable
            clearable
            style="width: 160px; margin-right: 16px;"
          >
            <el-option
              v-for="item in semesterList"
              :key="item.yId || item.yid"
              :label="item.yAll || item.yall"
              :value="item.yId || item.yid"
            />
          </el-select>

          <span class="label-text">课程：</span>
          <el-select
            v-model="scanCourseId"
            placeholder="请选择要扫描的课程"
            filterable
            clearable
            style="width: 220px; margin-right: 16px;"
          >
            <el-option
              v-for="item in courseList"
              :key="item.crid || item.id"
              :label="item.crname || item.name"
              :value="item.crid || item.id"
            />
          </el-select>

          <el-button type="danger" @click="handleScan" :loading="scanning">
            <el-icon class="mr-1"><Aim /></el-icon> 触发 AI 扫描
          </el-button>
          <el-button type="primary" plain @click="fetchWarningList">
            <el-icon class="mr-1"><Refresh /></el-icon> 刷新数据
          </el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="warningList"
        border
        stripe
        style="width: 100%"
        :row-class-name="tableRowClassName"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="100">
          <template #default="scope">
            <b style="color: #303133;">{{ scope.row.studentName }}</b>
          </template>
        </el-table-column>

        <el-table-column prop="courseId" label="关联课程ID" width="100" align="center" />

        <el-table-column label="风险等级" width="120" align="center">
          <template #default="scope">
            <el-tag
              :type="scope.row.riskLevel === '高风险' ? 'danger' : 'warning'"
              effect="dark"
            >
              {{ scope.row.riskLevel }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="综合学业风险指数" width="180" align="center">
          <template #default="scope">
            <el-progress
              :percentage="Number((scope.row.probability * 100).toFixed(1))"
              :color="getCustomColor"
              :stroke-width="10"
            />
          </template>
        </el-table-column>

        <el-table-column prop="reason" label="AI 诊断原因" min-width="250" show-overflow-tooltip />

        <el-table-column label="处理状态" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="info">未处理</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已谈话</el-tag>
            <el-tag v-else type="success" plain>已解除</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createDate" label="预警时间" width="160" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.createDate) }}
          </template>
        </el-table-column>

        <el-table-column label="操作干预" width="220" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-if="scope.row.status === 0"
              size="small"
              type="primary"
              @click="sendNotice(scope.row)"
            >
              一键通知
            </el-button>
            <el-button
              v-if="scope.row.status === 0"
              size="small"
              type="success"
              plain
              @click="markHandled(scope.row)"
            >
              标记已面谈
            </el-button>
            <span v-if="scope.row.status !== 0" style="color: #67C23A; font-size: 12px;">
              <el-icon><CircleCheckFilled /></el-icon> 流程已闭环
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="noticeDialogVisible" title="下发学业预警通知" width="500px">
      <el-form :model="noticeForm" label-width="80px">
        <el-form-item label="接收人">
          <el-input v-model="noticeForm.studentName" disabled />
        </el-form-item>
        <el-form-item label="通知标题">
          <el-input v-model="noticeForm.title" />
        </el-form-item>
        <el-form-item label="通知内容">
          <el-input type="textarea" v-model="noticeForm.content" :rows="5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="noticeDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitNotice" :loading="sendingNotice">确认发送</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from '@/utils/Axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Warning, Aim, Refresh, CircleCheckFilled } from '@element-plus/icons-vue';

// --- 状态定义 ---
const loading = ref(false);
const scanning = ref(false);
const warningList = ref([]);

const courseList = ref([]); // 课程下拉列表
const scanCourseId = ref(''); // 选中的课程ID

const semesterList = ref([]); // 学期下拉列表
const scanSemesterId = ref(''); // 选中的学期ID

// --- 1. 获取学期列表 (新增) ---
const fetchSemesterList = async () => {
  try {
    // ⚠️ 注意：这里假设你的获取学期接口是 /yearterm/getall，如果报错请改成你实际的接口路径
    const res = await axios.get('/yearterm/getall');
    if (res.data?.code === 200 || res.data?._code === 200) {
      semesterList.value = res.data.data || [];
    }
  } catch (error) {
    console.error("加载学期列表失败:", error);
  }
};

// --- 2. 获取课程列表 ---
const fetchCourseList = async () => {
  try {
    const res = await axios.get('/courseinfo/getall');
    if (res.data?.code === 200 || res.data?._code === 200) {
      courseList.value = res.data.data || [];
    }
  } catch (error) {
    console.error("加载课程列表失败:", error);
  }
};

// --- 3. 获取已有的预警记录列表 ---
const fetchWarningList = async () => {
  loading.value = true;
  try {
    // 这里为了演示，暂时还是获取全部预警。如果你后端写了按学期筛选的接口，可以带上参数
    // 例如: axios.get('/warning/list', { params: { termId: scanSemesterId.value } })
    const res = await axios.get('/warning/list');
    if (res.data?.code === 200 || res.data?._code === 200) {
      warningList.value = res.data.data || [];
    }
  } catch (error) {
    ElMessage.error('获取预警列表失败');
  } finally {
    loading.value = false;
  }
};

// --- 4. 触发 AI 扫描 ---
const handleScan = async () => {
  if (!scanCourseId.value) {
    ElMessage.warning('请先从下拉框中选择一门课程');
    return;
  }
  // 提示语变得更聪明：连带学期一起提示
  let msg = '即将开始扫描分析';
  if (scanSemesterId.value) {
    const term = semesterList.value.find(s => s.yId == scanSemesterId.value || s.yid == scanSemesterId.value);
    if(term) msg = `即将开始对【${term.yAll || term.yall}】学期的该课程进行扫描`;
  }

  scanning.value = true;
  try {
    const res = await axios.post(`/warning/scan/${scanCourseId.value}`);
    if (res.data?.code === 200 || res.data?._code === 200) {
      ElMessage.success('AI扫描任务完成！');
      fetchWarningList();
    }
  } catch (error) {
    ElMessage.error('扫描请求失败，请检查后端和Python服务');
  } finally {
    scanning.value = false;
  }
};

// --- 其他交互逻辑 ---
// --- 新增：发送通知相关的状态 ---
const noticeDialogVisible = ref(false);
const sendingNotice = ref(false);
const noticeForm = ref({
  studentId: null,
  studentName: '',
  title: '',
  content: ''
});

// --- 修改：点击一键通知按钮，打开弹窗并自动生成话术 ---
const sendNotice = (row) => {
  // 注意：后端返回的预警表里，学生ID字段通常是 wstudentid
  noticeForm.value = {
    studentId: row.wstudentid || row.studentId,
    studentName: row.studentName,
    title: '【学业预警】请关注您的近期学习状态',
    content: `亲爱的 ${row.studentName} 同学：\n\n系统检测到您在近期学习中存在一定风险。\nAI 诊断分析如下：\n${row.reason}\n\n请您务必引起重视，并尽快与任课老师或辅导员取得联系，及时调整学习计划！`
  };
  noticeDialogVisible.value = true;
};

// --- 新增：提交通知给后端 ---
const submitNotice = async () => {
  sendingNotice.value = true;
  try {
    // 调用后端的发公告接口
    const res = await axios.post('/warning/sendNotice', noticeForm.value);
    if (res.data?.code === 200 || res.data?._code === 200) {
      ElMessage.success('预警通知已成功下发到该学生的公告板！');
      noticeDialogVisible.value = false;
    } else {
      ElMessage.error(res.data?.msg || '发送失败');
    }
  } catch (error) {
    ElMessage.error('网络异常，发送失败');
  } finally {
    sendingNotice.value = false;
  }
};

const markHandled = async (row) => {
  try {
    const res = await axios.put(`/warning/handle/${row.wid}/1`);
    if (res.data?.code === 200) {
      ElMessage.success('已标记处理');
      fetchWarningList();
    }
  } catch (e) { ElMessage.error('操作失败'); }
};

const getCustomColor = (p) => p < 40 ? '#67C23A' : (p < 70 ? '#E6A23C' : '#F56C6C');

const tableRowClassName = ({ row }) => (row.riskLevel === '高风险' && row.status === 0) ? 'danger-row' : '';

const formatDate = (str) => {
  if (!str) return '-';
  const d = new Date(str);
  return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`;
};

onMounted(() => {
  fetchSemesterList(); // 👈 初始化加载学期
  fetchCourseList();   // 初始化加载课程
  fetchWarningList();  // 初始化加载预警记录
});
</script>

<style scoped>
.warning-container { padding: 20px; background-color: #f8fafc; min-height: 100vh; }
.toolbar-card { margin-bottom: 20px; border-radius: 12px; border: none; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); }
.toolbar-flex { display: flex; justify-content: space-between; align-items: center; }
.title-box { display: flex; align-items: center; }
.title-text { font-size: 20px; font-weight: 700; color: #1e293b; }
.action-box { display: flex; align-items: center; }
.label-text { font-size: 14px; color: #64748b; margin-right: 4px; font-weight: 500;}
.table-card { border-radius: 12px; border: none; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); }
.mr-1 { margin-right: 4px; }
.mr-2 { margin-right: 8px; }
:deep(.el-table .danger-row) { --el-table-tr-bg-color: #fff1f0; }
</style>
