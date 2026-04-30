<template>
  <el-menu
    style="text-align: left; border-right: none;"
    :default-active="$route.path"
    class="aside-menu"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    router
  >
    <!-- 管理员菜单 -->
    <!-- 机构管理 -->
    <el-sub-menu index="1" v-if="userRole == 3">
      <template #title>
        <i class="el-icon-menu"></i>
        <span>机构管理</span>
      </template>

      <el-menu-item index="/main/college/list">
        <i class="el-icon-school"></i>
        <span>院系管理</span>
      </el-menu-item>
      <el-menu-item index="/main/major/list">
        <i class="el-icon-notebook-2"></i>
        <span>专业管理</span>
      </el-menu-item>
      <el-menu-item index="/main/classinfo/list">
        <i class="el-icon-office-building"></i>
        <span>班级管理</span>
      </el-menu-item>
    </el-sub-menu>

    <!-- 档案管理 -->
    <el-sub-menu index="3_2" v-if="userRole == 3">
      <template #title>
        <i class="el-icon-files"></i>
        <span>档案管理</span>
      </template>

      <el-menu-item index="/main/student/list">
        <i class="el-icon-user"></i>
        <span>学生管理</span>
      </el-menu-item>
      <el-menu-item index="/main/teacher/list">
        <i class="el-icon-user-solid"></i>
        <span>教师管理</span>
      </el-menu-item>
    </el-sub-menu>

    <!-- 课程管理 -->
    <el-sub-menu index="3_3" v-if="userRole == 3">
      <template #title>
        <i class="el-icon-notebook-1"></i>
        <span>课程管理</span>
      </template>

      <el-menu-item index="/main/course/list">
        <i class="el-icon-reading"></i>
        <span>课程管理</span>
      </el-menu-item>
      <el-menu-item index="/main/teaching/list">
        <i class="el-icon-present"></i>
        <span>授课管理</span>
      </el-menu-item>
    </el-sub-menu>

    <!-- 成绩管理 -->
    <el-sub-menu index="3_4" v-if="userRole == 3">
      <template #title>
        <i class="el-icon-trophy"></i>
        <span>成绩管理</span>
      </template>

      <el-menu-item index="/main/scores/listquery">
        <i class="el-icon-search"></i>
        <span>成绩管理</span>
      </el-menu-item>
      <el-menu-item index="/main/scores/statistics">
        <i class="el-icon-data-analysis"></i>
        <span>成绩统计</span>
      </el-menu-item>
      <el-menu-item index="/academic-warning">
        <i class="el-icon-data-analysis"></i>
        <span>成绩预警大盘</span>
      </el-menu-item>
    </el-sub-menu>

    <!-- 公告管理 -->
    <el-menu-item index="/main/notice/list" v-if="userRole == 3">
      <i class="el-icon-message"></i>
      <span>公告管理</span>
    </el-menu-item>

    <!-- 用户管理 -->
    <el-menu-item index="/main/userinfo/list" v-if="userRole == 3">
      <i class="el-icon-user-group"></i>
      <span>用户管理</span>
    </el-menu-item>

    <!-- 教师端菜单 -->
    <el-sub-menu index="2_1" v-if="userRole == 2">
      <template #title>
        <i class="el-icon-trophy"></i>
        <span>成绩管理</span>
      </template>

      <el-menu-item index="/main/teacher/score">
        <i class="el-icon-edit"></i>
        <span>成绩录入与查看</span>
      </el-menu-item>

      <el-menu-item index="/main/scores/t_statistics">
        <i class="el-icon-data-analysis"></i>
        <span>成绩统计</span>
      </el-menu-item>
    </el-sub-menu>

    <el-menu-item index="/main/teaching/t_listquery" v-if="userRole == 2">
      <i class="el-icon-view"></i>
      <span>授课查看</span>
    </el-menu-item>

    <el-menu-item index="/main/student/t_listquery" v-if="userRole == 2">
      <i class="el-icon-user"></i>
      <span>学生查看</span>
    </el-menu-item>

    <el-menu-item index="/main/notice/listquery" v-if="userRole == 2">
      <i class="el-icon-message"></i>
      <span>公告查看</span>
    </el-menu-item>

    <!-- 学生端菜单 -->
    <el-menu-item index="/main/teaching/s_listquery" v-if="userRole == 1">
      <i class="el-icon-reading"></i>
      <span>课程查看</span>
    </el-menu-item>

    <el-menu-item index="/main/scores/s_listquery" v-if="userRole == 1">
      <i class="el-icon-search"></i>
      <span>成绩查询</span>
    </el-menu-item>

    <el-menu-item index="/main/notice/listquery" v-if="userRole == 1">
      <i class="el-icon-message"></i>
      <span>公告查看</span>
    </el-menu-item>

    <!-- 个人中心 -->
    <el-sub-menu index="9">
      <template #title>
        <i class="el-icon-setting"></i>
        <span>个人中心</span>
      </template>

      <el-menu-item index="/main/profile">
        <i class="el-icon-picture"></i>
        <span>头像设置</span>
      </el-menu-item>

      <el-menu-item index="/main/password">
        <i class="el-icon-lock"></i>
        <span>修改密码</span>
      </el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<script>
import { useUserStore } from "@/store/index"

export default {
  name: "Asider",
  data() {
    return {
      userRole: 0, // 1:学生 2:教师 3:管理员
    }
  },
  mounted() {
    const userStore = useUserStore()
    this.userRole = userStore.userInfo?.urole || 0
  },
}
</script>

<style scoped>
.aside-menu {
  height: 100%;
  border-right: none;
}

.aside-menu:not(.el-menu--collapse) {
  width: 200px;
}

:deep(.el-sub-menu__title) {
  font-size: 14px;
}

:deep(.el-menu-item) {
  font-size: 13px;
}

:deep(.el-menu-item i) {
  margin-right: 8px;
  width: 18px;
  text-align: center;
}

:deep(.el-menu-item-group__title) {
  display: none;
}
</style>
