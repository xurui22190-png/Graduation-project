<template>
  <div class="opt-container">
    <el-card class="profile-card" shadow="hover" :body-style="{ padding: '0' }">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="header-icon"><Lock /></el-icon>
            <span>修改登录密码</span>
          </div>
        </div>
      </template>

      <div class="profile-content">
        <el-form
          :model="pwdForm"
          :rules="rules"
          ref="pwdFormRef"
          label-position="top"
          class="profile-form"
        >
          <div class="info-section">
            <div class="section-title">
              <el-icon><InfoFilled /></el-icon>
              <span>安全提示</span>
            </div>
            <p class="security-tip">
              为了您的账号安全，建议定期更换密码，并使用字母、数字及特殊符号的组合。
            </p>
          </div>

          <el-divider />

          <div class="pwd-section">
            <el-form-item label="当前密码" prop="oldPassword">
              <el-input
                v-model="pwdForm.oldPassword"
                type="password"
                placeholder="请输入当前正在使用的密码"
                show-password
                :prefix-icon="Unlock"
              />
            </el-form-item>

            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="pwdForm.newPassword"
                type="password"
                placeholder="请输入 6-16 位新密码"
                show-password
                :prefix-icon="Key"
              />
            </el-form-item>

            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input
                v-model="pwdForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
                :prefix-icon="CircleCheck"
              />
            </el-form-item>
          </div>

          <el-divider />

          <div class="form-footer">
            <el-button
              type="primary"
              @click="handleUpdate"
              :icon="Check"
              size="large"
              round
              :loading="loading"
            >
              提交修改
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
import { ref, reactive } from "vue"
import { useRouter } from "vue-router"
import {
  Lock,
  Unlock,
  Key,
  CircleCheck,
  InfoFilled,
  Check,
  Refresh,
} from "@element-plus/icons-vue"
import { ElMessage } from "element-plus"
import axios from "@/utils/Axios"
import { useUserStore } from "@/store/index"

const router = useRouter()
const userStore = useUserStore()

const pwdFormRef = ref()
const loading = ref(false)

const pwdForm = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请再次输入密码"))
  } else if (value !== pwdForm.newPassword) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const rules = {
  oldPassword: [{ required: true, message: "请输入当前密码", trigger: "blur" }],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, max: 16, message: "长度在 6 到 16 个字符", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: "blur" },
  ],
}

const handleUpdate = async () => {
  if (!pwdFormRef.value) return

  try {
    await pwdFormRef.value.validate()
  } catch (error) {
    ElMessage.warning("请完善表单信息")
    return
  }

  if (pwdForm.oldPassword === pwdForm.newPassword) {
    ElMessage.warning("新密码不能和原密码一样")
    return
  }

  loading.value = true

  try {
    const postData = {
      uoldpwd: pwdForm.oldPassword,
      upwd: pwdForm.newPassword,
    }

    const response = await axios.put("/main/setpwd", postData)
    const res = response.data

    if (res.code === 200) {
      ElMessage.success(res.msg || "密码修改成功，请重新登录")
      resetForm()
      userStore.logout()
      router.push("/")
    } else {
      ElMessage.error(res.msg || "密码修改失败")
    }
  } catch (error) {
    console.error("修改密码失败：", error)
    ElMessage.error(
      error.response?.data?.msg ||
      error.response?.data?.message ||
      "修改失败，请稍后再试"
    )
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  if (pwdFormRef.value) {
    pwdFormRef.value.resetFields()
  }
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
  max-width: 500px;
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
  margin-bottom: 10px;
  color: #666;
  font-weight: 600;
}

.security-tip {
  font-size: 13px;
  color: #999;
  line-height: 1.6;
}

.pwd-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  padding-bottom: 4px;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  padding: 4px 12px;
}

.form-footer {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 10px;
}
</style>
