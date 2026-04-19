<template>
  <div class="login-container">
    <div class="login-box">
      <div class="logo">
        <div class="logo-icon"></div>
      </div>

      <h2 class="title">学生成绩管理系统</h2>

      <el-form
        :model="loginForm"
        :rules="rules"
        ref="loginFormRef"
        label-width="0"
      >
        <el-form-item prop="ulog">
          <el-input
            v-model="loginForm.ulog"
            placeholder="请输入账号/工号/学号"
            :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="upwd">
          <el-input
            v-model="loginForm.upwd"
            type="password"
            placeholder="请输入登录密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <div class="role">
            <span>身份：</span>
            <el-radio-group v-model="loginForm.urole">
              <el-radio value="1">学生</el-radio>
              <el-radio value="2">教师</el-radio>
              <el-radio value="3">管理员</el-radio>
            </el-radio-group>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>

        <el-form-item>
          <el-button
            class="temp-login-btn"
            :loading="loading"
            @click="handleTempLogin"
          >
            暂时登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue"
import { useRouter, useRoute } from "vue-router"
import { ElMessage } from "element-plus"
import { User, Lock } from "@element-plus/icons-vue"
import { useUserStore } from "@/store/index.js"

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  ulog: "",
  upwd: "",
  urole: "3",
})

const rules = {
  ulog: [{ required: true, message: "请输入账号", trigger: "blur" }],
  upwd: [{ required: true, message: "请输入密码", trigger: "blur" }],
}

// 正常登录
const handleLogin = async () => {
  if (!loginFormRef.value) return

  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true

      try {
        const res = await userStore.login(loginForm)

        if (res.success) {
          ElMessage.success("登录成功")
          const redirect = route.query.redirect || "/main/home"
          router.push(redirect)
        } else {
          ElMessage.error(res.message || "账号或密码有误")
        }
      } catch (error) {
        ElMessage.error("登录失败，请重试")
      } finally {
        loading.value = false
      }
    } else {
      ElMessage.warning("请完整填写信息")
      return false
    }
  })
}

// 临时登录
const handleTempLogin = async () => {
  loading.value = true

  try {
    userStore.setToken("temp-token")
    userStore.setUserInfo({
      uid: 1,
      ulog: "admin",
      uname: "管理员",
      urole: "3",
      usex: "男",
      uphoto: ""
    })

    ElMessage.success("已暂时登录")

    const redirect = route.query.redirect || "/main/home"
    router.push(redirect)
  } catch (error) {
    ElMessage.error("临时登录失败")
  } finally {
    loading.value = false
  }

}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 420px;
  max-width: 90%;
  padding: 48px 40px;
  background: rgba(255, 255, 255, 0.96);
  border-radius: 24px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.logo {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.logo-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(145deg, #409eff, #36d1dc);
  border-radius: 50%;
}

.title {
  text-align: center;
  margin-bottom: 40px;
  color: #2c3e50;
  font-size: 28px;
  font-weight: 600;
}

.role {
  display: flex;
  align-items: center;
  background: #f5f7fa;
  border-radius: 30px;
  padding: 8px 20px;
  width: 100%;
}

.role span {
  margin-right: 20px;
  color: #606266;
  font-weight: 500;
  font-size: 15px;
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
  border-radius: 30px;
}

.temp-login-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  border-radius: 30px;
}
</style>
