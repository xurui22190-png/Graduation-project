import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/store/index.js";

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "login",
    component: () => import("@/Views/LoginView.vue"),
    meta: {
      title: "登录",
      requireAuth: false,
    },
  },
  {
    path: "/main",
    name: "main",
    component: () => import("@/Views/main.vue"),
    redirect: "/main/home",
    meta: {
      title: "后台主页",
      requireAuth: true,
    },
    children: [
      {
        path: "home",
        name: "home",
        component: () => import("@/Views/Home.vue"),
        meta: {
          title: "系统首页",
          requireAuth: true,
        },
      },
      {
        path: "profile",
        name: "profile",
        component: () => import("@/Views/Profile.vue"),
        meta: {
          title: "头像设置",
          requireAuth: true,
        },
      },
      {
        path: "password",
        name: "password",
        component: () => import("@/Views/Password.vue"),
        meta: {
          title: "修改密码",
          requireAuth: true,
        },
      },

      // 机构管理
      {
        path: "college/list",
        name: "collegeList",
        component: () => import("@/Views/college/List.vue"),
        meta: {
          title: "院系管理",
          requireAuth: true,
        },
      },
      {
        path: "major/list",
        name: "majorList",
        component: () => import("@/Views/major/List.vue"),
        meta: {
          title: "专业管理",
          requireAuth: true,
        },
      },
      {
        path: "classinfo/list",
        name: "classinfoList",
        component: () => import("@/Views/classinfo/List.vue"),
        meta: {
          title: "班级管理",
          requireAuth: true,
        },
      },

      // 档案管理
      {
        path: "student/list",
        name: "studentList",
        component: () => import("@/Views/student/List.vue"),
        meta: {
          title: "学生管理",
          requireAuth: true,
        },
      },
      {
        path: "teacher/list",
        name: "teacherList",
        component: () => import("@/Views/teacher/List.vue"),
        meta: {
          title: "教师管理",
          requireAuth: true,
        },
      },

      // 课程管理
      {
        path: "course/list",
        name: "courseList",
        component: () => import("@/Views/courseinfo/List.vue"),
        meta: {
          title: "课程管理",
          requireAuth: true,
        },
      },
      {
        path: "teaching/list",
        name: "teachingList",
        component: () => import("@/Views/teaching/List.vue"),
        meta: {
          title: "授课管理",
          requireAuth: true,
        },
      },

      // 成绩管理
      {
        path: "scores/listquery",
        name: "scoresListQuery",
        component: () => import("@/Views/score/List.vue"),
        meta: {
          title: "成绩查询",
          requireAuth: true,
        },
      },
      {
        path: "scores/statistics",
        name: "scoresStatistics",
        component: () => import("@/Views/score/Statistics.vue"),
        meta: {
          title: "成绩统计",
          requireAuth: true,
        },
      },
      {
        path: "teacher/score",
        name: "teacherScore",
        component: () => import("@/Views/teacher/TeacherScore.vue"),
        meta: {
          title: "教师成绩管理",
          requireAuth: true,
        },
      },
      {
        path: "scores/t_listquery",
        name: "scoresTeacherListQuery",
        component: () => import("@/Views/ListDemo/List.vue"),
        meta: {
          title: "教师成绩查询",
          requireAuth: true,
        },
      },
      {
        path: "scores/t_statistics",
        name: "scoresTeacherStatistics",
        component: () => import("@/Views/teacher/TeacherScoreStatistics.vue"),
        meta: {
          title: "教师成绩统计",
          requireAuth: true,
        },
      },
      {
        path: "scores/s_listquery",
        name: "scoresStudentListQuery",
        component: () => import("@/Views/student/StudentScore.vue"),
        meta: {
          title: "学生成绩查询",
          requireAuth: true,
        },
      },

      // 公告管理
      {
        path: "notice/list",
        name: "noticeList",
        component: () => import("@/Views/notice/List.vue"),
        meta: {
          title: "公告管理",
          requireAuth: true,
        },
      },
      {
        path: "notice/listquery",
        name: "noticeListQuery",
        component: () => import("@/Views/notice/Query.vue"),
        meta: {
          title: "公告查看",
          requireAuth: true,
        },
      },

      // 用户管理
      {
        path: "userinfo/list",
        name: "userinfoList",
        component: () => import("@/Views/userinfo/List.vue"),
        meta: {
          title: "用户管理",
          requireAuth: true,
        },
      },

      // 教师/学生查询
      {
        path: "teaching/t_listquery",
        name: "teachingTeacherListQuery",
        component: () => import("@/Views/teacher/Teaching.vue"),
        meta: {
          title: "教师授课查询",
          requireAuth: true,
        },
      },

      {
        path: "student/t_listquery",
        name: "studentTeacherListQuery",
        component: () => import("@/Views/teacher/TeacherStudent.vue"),
        meta: {
          title: "学生查看",
          requireAuth: true,
        },
      },
      {
        path: "teaching/s_listquery",
        name: "teachingStudentListQuery",
        component: () => import("@/Views/student/Coures.vue"),
        meta: {
          title: "学生课程查询",
          requireAuth: true,
        },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  } else {
    document.title = "学生成绩管理系统";
  }

  const userStore = useUserStore();

  // 先恢复本地登录状态
  userStore.initializeUserState();

  if (to.meta.requireAuth) {
    if (userStore.isLoggedIn) {
      next();
    } else {
      next({
        path: "/login",
        query: {
          redirect: to.fullPath,
        },
      });
    }
  } else {
    next();
  }
});

export default router;
