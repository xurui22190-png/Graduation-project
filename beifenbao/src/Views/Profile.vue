<template>
  <div class="opt-container">
    <el-card class="profile-card" shadow="hover" :body-style="{ padding: '0' }">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="header-icon"><UserFilled /></el-icon>
            <span>头像设置</span>
          </div>
          <el-tag size="small" type="info" effect="plain">个人信息维护</el-tag>
        </div>
      </template>

      <div class="profile-content">
        <el-form
          :model="ruleForm"
          ref="ruleFormRef"
          label-width="0"
          class="profile-form"
        >
          <div class="info-section">
            <div class="section-title">
              <el-icon><InfoFilled /></el-icon>
              <span>基本信息</span>
            </div>

            <div class="info-items">
              <div class="info-item">
                <span class="info-label">账号</span>
                <span class="info-value">{{ ruleForm.uLog }}</span>
              </div>

              <div class="info-item">
                <span class="info-label">姓名</span>
                <span class="info-value">{{ ruleForm.uname }}</span>
              </div>

              <div class="info-item">
                <span class="info-label">性别</span>
                <span class="info-value">{{ ruleForm.usex }}</span>
              </div>
            </div>
          </div>

          <el-divider />

          <div class="avatar-section">
            <div class="section-title">
              <el-icon><Picture /></el-icon>
              <span>头像预览</span>
            </div>

            <div class="avatar-container">
              <el-upload
                class="avatar-uploader"
                :action="uploadURL"
                :headers="headers"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :on-error="handleUploadError"
                :before-upload="beforeAvatarUpload"
              >
                <div class="avatar-wrapper" :class="{ 'has-avatar': ruleForm.uphoto }">
                  <img
                    v-if="ruleForm.uphoto"
                    :src="imgurl + ruleForm.uphoto + '?t=' + Date.now()"
                    class="avatar"
                  />

                  <div v-else class="avatar-placeholder">
                    <el-icon class="placeholder-icon"><Plus /></el-icon>
                    <span class="placeholder-text">上传头像</span>
                  </div>
                </div>
              </el-upload>

              <div class="avatar-tip">
                <el-icon><InfoFilled /></el-icon>
                <span>支持 JPG / PNG ，大小不超过 2MB</span>
              </div>
            </div>
          </div>

          <el-divider />

          <div class="form-footer">
            <el-button
              type="primary"
              @click="handleSave"
              :icon="Upload"
              size="large"
              round
              :loading="loading"
            >
              保存修改
            </el-button>

            <el-button
              @click="resetForm"
              :icon="Refresh"
              size="large"
              round
            >
              重置
            </el-button>
          </div>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from "vue"
import {
  Plus,
  Picture,
  InfoFilled,
  UserFilled,
  Upload,
  Refresh,
} from "@element-plus/icons-vue"
import { ElMessage } from "element-plus"
import { htbaseurl, imgurl } from "@/store/config.js"
import axios from "@/utils/Axios"
import { useUserStore } from "@/store/index"

const ruleFormRef = ref()
const loading = ref(false)
const userStore = useUserStore()

// 上传接口地址
const uploadURL = htbaseurl + "/api/pub/uploadfile"

// 请求头，携带 token
const headers = computed(() => ({
  token: localStorage.getItem("token") || "",
}))

// 页面表单数据
const ruleForm = reactive({
  uLog: "",
  uname: "",
  usex: "",
  uphoto: "",
})

// 保存初始头像，方便重置
const oldPhoto = ref("")

onMounted(() => {
  const currentUser = userStore.userInfo || {}

  ruleForm.uLog = currentUser.ulog || ""
  ruleForm.uname = currentUser.uname || ""
  ruleForm.usex = currentUser.usex || ""
  ruleForm.uphoto = currentUser.uphoto || ""

  oldPhoto.value = currentUser.uphoto || ""
})

// 上传成功回调
const handleAvatarSuccess = (res) => {
  if (res.code === 200 || res.success) {
    ruleForm.uphoto = res.data
    ElMessage.success("头像上传成功")
  } else {
    ElMessage.error(res.msg || "上传失败")
  }
}

// 上传失败回调
const handleUploadError = () => {
  ElMessage.error("头像上传失败")
}

// 上传前校验
const beforeAvatarUpload = (file) => {
  const isImage =
    file.type === "image/jpeg" ||
    file.type === "image/png" ||
    file.type === "image/jpg"

  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error("只能上传 JPG / PNG 图片")
    return false
  }

  if (!isLt2M) {
    ElMessage.error("图片不能超过 2MB")
    return false
  }

  return true
}

// 保存按钮
const handleSave = async () => {
  if (!ruleForm.uphoto) {
    ElMessage.warning("请先上传头像")
    return
  }

  loading.value = true

  try {
    const postData = {
      uphoto: ruleForm.uphoto,
    }

    const response = await axios.put("/main/setphoto", postData)
    const res = response.data

    if (res.code === 200) {
      ElMessage.success(res.msg || "保存成功")

      oldPhoto.value = ruleForm.uphoto

      userStore.updateUserInfo({
        uphoto: ruleForm.uphoto,
      })
    } else {
      ElMessage.error(res.msg || "保存失败")
    }
  } catch (error) {
    console.error("头像保存失败：", error)
    ElMessage.error(
      error.response?.data?.msg ||
      error.response?.data?.message ||
      "保存失败，请稍后再试"
    )
  } finally {
    loading.value = false
  }
}

// 重置按钮
const resetForm = () => {
  ruleForm.uphoto = oldPhoto.value
  ElMessage.info("已重置")
}
</script>

<style scoped>
.opt-container {
  width: 100%;
  min-height: 100%;
  margin: 0;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background: transparent;
}

.profile-card {
  width: 100%;
  max-width: 800px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.12);
}

.profile-content {
  padding: 30px 40px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #fafafa;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
}

.header-icon {
  font-size: 22px;
  color: #764ba2;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 15px;
  color: #666;
  font-weight: 600;
}

.info-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px dashed #eee;
}

.info-label {
  width: 80px;
  color: #999;
}

.info-value {
  color: #333;
  font-weight: 500;
}

.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-uploader {
  display: flex;
  justify-content: center;
}

.avatar-wrapper {
  width: 140px;
  height: 140px;
  border: 2px dashed #d9d9d9;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
  transition: all 0.3s;
}

.avatar-wrapper:hover {
  border-color: #409eff;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #999;
}

.placeholder-icon {
  font-size: 28px;
  margin-bottom: 8px;
}

.placeholder-text {
  font-size: 14px;
}

.avatar-tip {
  margin-top: 15px;
  font-size: 13px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 5px;
}

.form-footer {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 10px;
}
</style>
