import { defineStore } from "pinia";
import { getToken, setToken, removeToken } from "@/utils/auth";
import { login, getInfo, logout } from "@/api/auth";

export const useUserStore = defineStore("front-user", {
  state: () => ({
    token: getToken() || "",
    info: {}
  }),
  getters: {
    isLogin: state => !!state.token,
    avatar: state => state.info?.avatar || "",
    nickName: state => state.info?.nickName || state.info?.userName || "用户"
  },
  actions: {
    async loginAction(payload) {
      const res = await login(payload);
      this.token = res.token;
      setToken(res.token);
    },
    async fetchInfo() {
      const res = await getInfo();
      this.info = res.user || {};
      return res;
    },
    async logoutAction() {
      try {
        await logout();
      } finally {
        this.resetAuth();
      }
    },
    resetAuth() {
      this.token = "";
      this.info = {};
      removeToken();
    }
  }
});
