import { createRouter, createWebHistory } from "vue-router";
import { getToken } from "@/utils/auth";
import { useUserStore } from "@/store/modules/user";

const routes = [
  {
    path: "/login",
    component: () => import("@/views/Login.vue"),
    meta: { title: "登录" }
  },
  {
    path: "/",
    component: () => import("@/layout/FrontLayout.vue"),
    redirect: "/home",
    children: [
      {
        path: "home",
        component: () => import("@/views/Home.vue"),
        meta: { title: "器材预约" }
      },
      {
        path: "my-borrow",
        component: () => import("@/views/MyBorrow.vue"),
        meta: { title: "我的借还" }
      },
      {
        path: "my-repair",
        component: () => import("@/views/MyRepair.vue"),
        meta: { title: "我的报修" }
      },
      {
        path: "notice",
        component: () => import("@/views/NoticeCenter.vue"),
        meta: { title: "消息中心" }
      },
      {
        path: "profile",
        component: () => import("@/views/Profile.vue"),
        meta: { title: "个人中心" }
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach(async (to, from, next) => {
  const hasToken = !!getToken();
  if (!hasToken) {
    if (to.path === "/login") {
      next();
    } else {
      next("/login");
    }
    return;
  }
  const userStore = useUserStore();
  if (to.path === "/login") {
    next("/home");
    return;
  }
  try {
    if (!userStore.info?.userId) {
      await userStore.fetchInfo();
    }
    next();
  } catch (e) {
    userStore.resetAuth();
    next("/login");
  }
});

export default router;
