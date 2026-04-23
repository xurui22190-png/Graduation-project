import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/store/index.js";

/**
 * 路由配置说明：
 * 1. /login → 登录页
 * 2. /main → 主布局（所有后台页面都在 children 里）
 * 3. meta.requireAuth → 是否需要登录
 * 4. meta.title → 页面标题
 */
const routes = [
  {
    path: "/",
    redirect: "/login", // 默认跳登录页
  },

  // ================= 登录模块 =================
  {
    path: "/login",
    name: "login",
    component: () => import("@/Views/LoginView.vue"),
    meta: {
      title: "登录",
      requireAuth: false, // 不需要登录
    },
  },

  // ================= 主布局 =================
  {
    path: "/main",
    name: "main",
    component: () => import("@/Views/main.vue"),
    redirect: "/main/home", // 默认进入首页
    meta: {
      title: "后台主页",
      requireAuth: true,
    },

    // 所有功能页面都挂在这里
    children: [
      // ================= 系统基础 =================
      {
        path: "home",
        name: "home",
        component: () => import("@/Views/Home.vue"),
        meta: { title: "系统首页", requireAuth: true },
      },
      {
        path: "profile",
        name: "profile",
        component: () => import("@/Views/Profile.vue"),
        meta: { title: "头像设置", requireAuth: true },
      },
      {
        path: "password",
        name: "password",
        component: () => import("@/Views/Password.vue"),
        meta: { title: "修改密码", requireAuth: true },
      },

      // ================= 机构管理 =================
      {
        path: "college/list",
        name: "collegeList",
        component: () => import("@/Views/college/List.vue"),
        meta: { title: "院系管理", requireAuth: true },
      },
      {
        path: "major/list",
        name: "majorList",
        component: () => import("@/Views/major/List.vue"),
        meta: { title: "专业管理", requireAuth: true },
      },
      {
        path: "classinfo/list",
        name: "classinfoList",
        component: () => import("@/Views/classinfo/List.vue"),
        meta: { title: "班级管理", requireAuth: true },
      },

      // ================= 档案管理 =================
      {
        path: "student/list",
        name: "studentList",
        component: () => import("@/Views/student/List.vue"),
        meta: { title: "学生管理", requireAuth: true },
      },
      {
        path: "teacher/list",
        name: "teacherList",
        component: () => import("@/Views/teacher/List.vue"),
        meta: { title: "教师管理", requireAuth: true },
      },

      // ================= 课程管理 =================
      {
        path: "course/list",
        name: "courseList",
        component: () => import("@/Views/courseinfo/List.vue"),
        meta: { title: "课程管理", requireAuth: true },
      },
      {
        path: "teaching/list",
        name: "teachingList",
        component: () => import("@/Views/teaching/List.vue"),
        meta: { title: "授课管理", requireAuth: true },
      },

      // ================= 成绩管理 =================
      {
        path: "scores/listquery",
        name: "scoresListQuery",
        component: () => import("@/Views/score/List.vue"),
        meta: { title: "成绩查询", requireAuth: true },
      },
      {
        path: "scores/statistics",
        name: "scoresStatistics",
        component: () => import("@/Views/score/Statistics.vue"),
        meta: { title: "成绩统计", requireAuth: true },
      },

      // 👇 教师端
      {
        path: "teacher/score",
        name: "teacherScore",
        component: () => import("@/Views/teacher/TeacherScore.vue"),
        meta: { title: "教师成绩管理", requireAuth: true },
      },
      {
        path: "scores/t_listquery",
        name: "scoresTeacherListQuery",
        component: () => import("@/Views/ListDemo/List.vue"),
        meta: { title: "教师成绩查询", requireAuth: true },
      },
      {
        path: "scores/t_statistics",
        name: "scoresTeacherStatistics",
        component: () => import("@/Views/teacher/TeacherScoreStatistics.vue"),
        meta: { title: "教师成绩统计", requireAuth: true },
      },

      // 👇 学生端
      {
        path: "scores/s_listquery",
        name: "scoresStudentListQuery",
        component: () => import("@/Views/student/StudentScore.vue"),
        meta: { title: "学生成绩查询", requireAuth: true },
      },

      // ================= 公告模块 =================
      {
        path: "notice/list",
        name: "noticeList",
        component: () => import("@/Views/notice/List.vue"),
        meta: { title: "公告管理", requireAuth: true },
      },
      {
        path: "notice/listquery",
        name: "noticeListQuery",
        component: () => import("@/Views/notice/Query.vue"),
        meta: { title: "公告查看", requireAuth: true },
      },

      // ================= 用户管理 =================
      {
        path: "userinfo/list",
        name: "userinfoList",
        component: () => import("@/Views/userinfo/List.vue"),
        meta: { title: "用户管理", requireAuth: true },
      },

      // ================= 教师 / 学生功能 =================
      {
        path: "teaching/t_listquery",
        name: "teachingTeacherListQuery",
        component: () => import("@/Views/teacher/Teaching.vue"),
        meta: { title: "教师授课查询", requireAuth: true },
      },
      {
        path: "student/t_listquery",
        name: "studentTeacherListQuery",
        component: () => import("@/Views/teacher/TeacherStudent.vue"),
        meta: { title: "学生查看", requireAuth: true },
      },
      {
        path: "teaching/s_listquery",
        name: "teachingStudentListQuery",
        component: () => import("@/Views/student/Coures.vue"),
        meta: { title: "学生课程查询", requireAuth: true },
      },
    ],
  },
];

// ================= 创建路由 =================
const router = createRouter({
  history: createWebHistory(),
  routes,
});

/**
 * 全局前置守卫
 * 作用：
 * 1. 动态修改页面标题
 * 2. 判断是否登录
 */
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title || "学生成绩管理系统";

  const userStore = useUserStore();

  // 初始化用户状态（从 localStorage 恢复）
  userStore.initializeUserState();

  // 判断是否需要登录
  if (to.meta.requireAuth) {
    if (userStore.isLoggedIn) {
      next();
    } else {
      // 未登录 → 跳转登录页
      next({
        path: "/login",
        query: { redirect: to.fullPath },
      });
    }
  } else {
    next();
  }
});

export default router;
