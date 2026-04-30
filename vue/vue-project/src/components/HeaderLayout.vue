<template>
  <div class="part-hed">
    <div class="header-left">
      <button class="system-btn" @click="goHome">
        <i class="fa fa-bandcamp" aria-hidden="true"></i>
        <span>学生成绩分析和弱项诊断</span>
      </button>
    </div>

    <div class="user-info">
      <span class="account">
        欢迎你：{{ userName || "未登录" }} ({{ userAccount || "" }})
      </span>

      <el-dropdown>
        <el-image class="dftphoto" :src="userphoto" fit="cover"></el-image>

        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="goHome">
              系统首页
            </el-dropdown-item>

            <el-dropdown-item @click="showProfile">
              头像设置
            </el-dropdown-item>

            <el-dropdown-item @click="showChangePwd">
              修改密码
            </el-dropdown-item>

            <el-dropdown-item divided @click="setLogout">
              退出系统
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import defaultPhoto from "@/assets/images/photo_1.png"
import { mapState } from "pinia"
import { useUserStore } from "@/store/index.js"
import { imgurl } from "@/store/config.js"

export default {
  name: "HeaderLayout",

  computed: {
    ...mapState(useUserStore, [
      "userAccount",
      "userName",
      "userAvatar",
    ]),

    userphoto() {
      return this.userAvatar
        ? imgurl + this.userAvatar + "?t=" + Date.now()
        : defaultPhoto
    },
  },

  methods: {
    goHome() {
      this.$router.push("/main/home")
    },

    showProfile() {
      this.$router.push("/main/profile")
    },

    showChangePwd() {
      this.$router.push("/main/password")
    },

    setLogout() {
      const userStore = useUserStore()
      userStore.logout()
      this.$router.push("/login")
    },
  },
}
</script>

<style scoped>
.part-hed {
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background: linear-gradient(135deg, #2b5876 0%, #4e4376 100%);
  color: #fff;
}

.header-left {
  display: flex;
  align-items: center;
}

.system-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  border: none;
  background: transparent;
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  padding: 0;
}

.system-btn:hover {
  opacity: 0.9;
}

.system-btn i {
  font-size: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.account {
  font-size: 14px;
  opacity: 0.95;
}

.dftphoto {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  border: 2px solid rgba(255, 255, 255, 0.3);
  cursor: pointer;
}

:deep(.el-dropdown-menu__item) {
  font-size: 13px;
}
</style>
