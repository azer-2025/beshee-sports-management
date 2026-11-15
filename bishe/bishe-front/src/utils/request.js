import axios from "axios";
import { ElMessage } from "element-plus";
import { getToken, removeToken } from "@/utils/auth";

const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 12000,
  headers: { "Content-Type": "application/json;charset=utf-8" }
});

service.interceptors.request.use(config => {
  if (getToken() && (config.headers || {}).isToken !== false) {
    config.headers.Authorization = "Bearer " + getToken();
  }
  return config;
});

service.interceptors.response.use(
  res => {
    const code = res.data.code || 200;
    if (code === 200) {
      return res.data;
    }
    if (code === 401) {
      removeToken();
      ElMessage.error("登录状态已过期，请重新登录");
      window.location.href = "/login";
      return Promise.reject(new Error("Unauthorized"));
    }
    ElMessage.error(res.data.msg || "请求失败");
    return Promise.reject(new Error(res.data.msg || "Error"));
  },
  error => {
    const message = error?.message || "网络异常";
    ElMessage.error(message);
    return Promise.reject(error);
  }
);

export default service;
