import axios from "axios";
import { hturl } from "@/store/config.js";
import router from "@/router";

axios.defaults.baseURL = hturl;
axios.defaults.timeout = 18000;

axios.interceptors.request.use(
  (config) => {
    var token = localStorage.getItem("token");
    config.headers = config.headers || {};

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (err) => {
    return Promise.reject(err);
  },
);

axios.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
      router.push("/login");
      return;
    }

    return Promise.reject(error);
  },
);

export default axios;
