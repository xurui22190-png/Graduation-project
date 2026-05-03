// 引入 Pinia 的 defineStore，用来定义一个状态仓库
import { defineStore } from "pinia";

// 引入 axios，用于发送登录请求
import axios from "@/utils/Axios";

// 定义并导出用户状态仓库
// 'user' 是当前 store 的唯一标识名
export const useUserStore = defineStore("user", {
  // =========================
  // state：定义全局状态数据
  // =========================
  state: () => ({
    // token：登录凭证
    // 优先从 localStorage 中读取，页面刷新后也能保留登录状态
    token: localStorage.getItem("token") || "",

    // userInfo：当前登录用户信息对象
    // 从 localStorage 中读取并转成对象
    userInfo: JSON.parse(localStorage.getItem("userInfo") || "null"),
  }),

  // =========================
  // getters：类似于计算属性
  // 用来从 state 中派生出常用数据
  // =========================
  getters: {
    // 获取当前完整用户对象
    currentUser: (state) => state.userInfo,

    // 获取用户 ID
    userId: (state) => state.userInfo?.uid || "",

    // 获取用户账号
    userAccount: (state) => state.userInfo?.ulog || "",

    // 获取用户角色
    userRole: (state) => state.userInfo?.urole || "",

    // 获取用户头像
    userAvatar: (state) => state.userInfo?.uphoto || "",

    // 获取用户姓名
    userName: (state) => state.userInfo?.uname || "",

    // 获取用户性别
    userSex: (state) => state.userInfo?.usex || "",

    // 判断当前是否已登录
    // 一般要求 token 和 userInfo 都存在才算登录
    isLoggedIn: (state) => !!state.token && !!state.userInfo,
  },

  // =========================
  // actions：定义业务方法
  // 可以写同步/异步逻辑
  // =========================
  actions: {
    // 设置 token
    // 登录成功后保存 token，同时写入本地存储
    setToken(token) {
      // 更新 Pinia 中的状态
      this.token = token;

      // 如果 token 有值，则写入 localStorage
      if (token) {
        localStorage.setItem("token", token);
      } else {
        // 如果没有值，则删除本地 token
        localStorage.removeItem("token");
      }
    },

    // 设置用户信息
    // 登录成功、修改资料后都可以调用这个方法
    setUserInfo(userInfo) {
      // 更新 Pinia 中的状态
      this.userInfo = userInfo;

      // 如果 userInfo 存在，则保存到 localStorage
      if (userInfo) {
        localStorage.setItem("userInfo", JSON.stringify(userInfo));
      } else {
        // 如果 userInfo 为空，则删除本地存储
        localStorage.removeItem("userInfo");
      }
    },

    // 清空用户信息
    // 一般用于退出登录
    clearUserInfo() {
      // 清空状态中的 token 和用户信息
      this.token = "";
      this.userInfo = null;

      // 同时清空本地存储
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
    },

    // 用户登录方法
    // loginData 是登录表单提交的数据
    async login(loginData) {
      try {
        const response = await axios.post("/pub/login", loginData);

        if (response.data.code === 200) {
          const { token, userInfo } = response.data.data;

          this.setToken(token);
          this.setUserInfo(userInfo);

          return {
            success: true,
            userInfo,
          };
        } else {
          return {
            success: false,
            message: response.data.message || response.data.msg || "登录失败",
          };
        }
      } catch (error) {
        console.log("登录错误信息：", error);
        console.log("登录错误响应：", error.response);

        return {
          success: false,
          message:
            error.response?.data?.message ||
            error.response?.data?.msg ||
            error.message ||
            "网络错误",
        };
      }
    },

    // 退出登录
    // 清除当前登录信息
    logout() {
      this.clearUserInfo();
    },

    // 更新用户信息
    // updatedInfo 是要更新的那部分字段
    // 比如更新头像、姓名等
    updateUserInfo(updatedInfo) {
      // 将旧的 userInfo 和新的 updatedInfo 合并成一个新对象
      const newUserInfo = {
        ...this.userInfo,
        ...updatedInfo,
      };

      // 保存新的用户信息
      this.setUserInfo(newUserInfo);

      // 返回更新后的用户信息，方便调用处继续使用
      return newUserInfo;
    },

    // 初始化用户状态
    // 一般在页面刷新、项目启动时调用
    // 用于从 localStorage 恢复登录状态
    initializeUserState() {
      // 获取本地存储中的用户信息
      const storedUserInfo = localStorage.getItem("userInfo");

      // 获取本地存储中的 token
      const storedToken = localStorage.getItem("token");

      // 如果 token 存在，就恢复到 state 中
      if (storedToken) {
        this.token = storedToken;
      }

      // 如果用户信息存在，就解析并恢复到 state 中
      if (storedUserInfo) {
        try {
          // 解析 JSON 字符串
          this.userInfo = JSON.parse(storedUserInfo);
        } catch (error) {
          // 如果解析失败，打印错误并清空 userInfo
          console.error("解析用户信息失败：", error);
          this.userInfo = null;
        }
      }
    },
  },
});
