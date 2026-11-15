<template>
  <div class="front-layout">
    <header class="front-header">
      <div class="header-inner">
        <!-- Logo Section -->
        <div class="logo-wrapper" @click="router.push('/home')">
          <div class="logo-icon">
            <el-icon><Bicycle /></el-icon>
            <div class="logo-glow"></div>
          </div>
          <div class="logo-text">
            <span class="title">体育器材平台</span>
            <span class="sub">SPORTS EQUIPMENT</span>
          </div>
        </div>

        <!-- Navigation Menu -->
        <nav class="top-nav">
          <router-link
            v-for="item in menus"
            :key="item.path"
            :to="item.path"
            class="nav-link"
            :class="{ active: isActive(item.path) }"
          >
            <div class="nav-content">
              <el-icon class="nav-icon"><component :is="item.icon" /></el-icon>
              <span class="nav-text">{{ item.label }}</span>
              <div v-if="item.path === '/notice' && noticeCount > 0" class="nav-badge">
                {{ noticeCount > 99 ? '99+' : noticeCount }}
              </div>
            </div>
            <div class="active-indicator"></div>
          </router-link>
        </nav>

        <!-- User Actions -->
        <div class="user-actions">
          <div class="action-divider"></div>
          
          <el-tooltip content="通知中心" placement="bottom" :show-after="500">
            <div class="icon-btn-wrapper" @click="router.push('/notice')">
              <div class="icon-btn">
                <el-icon><Bell /></el-icon>
                <div v-if="noticeCount > 0" class="dot-indicator"></div>
              </div>
            </div>
          </el-tooltip>

          <el-dropdown trigger="click" popper-class="user-dropdown-popper">
            <div class="user-profile-trigger">
              <div class="avatar-wrapper">
                <el-avatar :src="avatarUrl" :size="36" class="custom-avatar">
                  {{ nickName.slice(0, 1) }}
                </el-avatar>
                <div class="online-status"></div>
              </div>
              <div class="user-info">
                <span class="user-name">{{ nickName }}</span>
                <el-icon class="arrow-icon"><ArrowDown /></el-icon>
              </div>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <div class="dropdown-header">
                  <div class="dropdown-user-info">
                    <strong>{{ nickName }}</strong>
                    <span>欢迎使用平台</span>
                  </div>
                </div>
                <el-dropdown-item @click="router.push('/profile')">
                  <el-icon><User /></el-icon>
                  <span>个人信息</span>
                </el-dropdown-item>
                <el-dropdown-item @click="router.push('/my-borrow')">
                  <el-icon><List /></el-icon>
                  <span>我的借还</span>
                </el-dropdown-item>
                <el-dropdown-item @click="router.push('/my-repair')">
                  <el-icon><Tools /></el-icon>
                  <span>报修记录</span>
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout" class="logout-item">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出系统</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <main class="front-main">
      <div class="main-container">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
    
    <footer class="front-footer">
      <div class="footer-content">
        <p>© 2026 体育器材预约管理系统 · 提供更便捷的运动体验</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  Bell,
  Bicycle,
  ArrowDown,
  User,
  List,
  Tools,
  HomeFilled,
  Message,
  SwitchButton
} from "@element-plus/icons-vue";
import { useUserStore } from "@/store/modules/user";
import { useNoticeStore } from "@/store/modules/notice";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const noticeStore = useNoticeStore();

const menus = [
  { path: "/home", label: "器材预约", icon: HomeFilled },
  { path: "/my-borrow", label: "我的借还", icon: List },
  { path: "/my-repair", label: "报修管理", icon: Tools },
  { path: "/notice", label: "消息中心", icon: Message },
  { path: "/profile", label: "个人中心", icon: User }
];

const nickName = computed(() => userStore.nickName || '未登录');
const noticeCount = computed(() => noticeStore.noticeCount);

const avatarUrl = computed(() => {
  const avatar = userStore.avatar;
  if (!avatar) return "";
  if (avatar.startsWith("http://") || avatar.startsWith("https://")) return avatar;
  return `${import.meta.env.VITE_APP_BASE_API}${avatar}`;
});

const isActive = (path) => {
  if (path === '/home' && route.path === '/') return true;
  return route.path === path;
};

const handleLogout = async () => {
  noticeStore.clear();
  await userStore.logoutAction();
  router.push("/login");
};

let timer = null;

onMounted(async () => {
  await noticeStore.refreshAll();
  timer = setInterval(() => {
    noticeStore.refreshSummary();
  }, 60000);
});

onBeforeUnmount(() => {
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
});

watch(
  () => route.path,
  async (path) => {
    if (path === "/notice") {
      await noticeStore.refreshAll();
    }
  }
);
</script>

<style scoped>
.front-layout {
  min-height: 100vh;
  background-color: #f6f8fa;
  display: flex;
  flex-direction: column;
}

/* Header Styling */
.front-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-bottom: 1px solid rgba(235, 238, 245, 0.6);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.02);
}

.header-inner {
  height: 72px;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* Logo Styling */
.logo-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  user-select: none;
}

.logo-icon {
  position: relative;
  width: 42px;
  height: 42px;
  background: linear-gradient(135deg, #409eff 0%, #007aff 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 24px;
  box-shadow: 0 4px 12px rgba(0, 122, 255, 0.3);
}

.logo-glow {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: inherit;
  filter: blur(10px);
  opacity: 0.4;
  z-index: -1;
  border-radius: inherit;
}

.logo-text {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.logo-text .title {
  font-size: 19px;
  font-weight: 800;
  color: #1d2129;
  letter-spacing: -0.5px;
}

.logo-text .sub {
  font-size: 10px;
  font-weight: 600;
  color: #86909c;
  letter-spacing: 1px;
}

/* Nav Styling */
.top-nav {
  display: flex;
  align-items: center;
  gap: 4px;
  margin: 0 40px;
}

.nav-link {
  position: relative;
  height: 72px;
  padding: 0 20px;
  text-decoration: none;
  display: flex;
  align-items: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  color: #4e5969;
}

.nav-content {
  display: flex;
  align-items: center;
  gap: 8px;
  z-index: 2;
}

.nav-icon {
  font-size: 18px;
  transition: transform 0.3s ease;
}

.nav-text {
  font-size: 15px;
  font-weight: 500;
}

.nav-link:hover {
  color: #409eff;
}

.nav-link:hover .nav-icon {
  transform: translateY(-2px);
}

.nav-link.active {
  color: #409eff;
}

.nav-link.active .nav-text {
  font-weight: 600;
}

.active-indicator {
  position: absolute;
  bottom: 0;
  left: 20px;
  right: 20px;
  height: 3px;
  background: #409eff;
  border-radius: 3px 3px 0 0;
  opacity: 0;
  transform: scaleX(0.5);
  transition: all 0.3s ease;
}

.nav-link.active .active-indicator {
  opacity: 1;
  transform: scaleX(1);
}

.nav-badge {
  background: #f53f3f;
  color: white;
  font-size: 10px;
  padding: 1px 5px;
  border-radius: 10px;
  line-height: 1;
  margin-left: -2px;
  transform: translateY(-8px);
}

/* User Actions Styling */
.user-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.action-divider {
  width: 1px;
  height: 24px;
  background: #e5e6eb;
  margin-right: 4px;
}

.icon-btn-wrapper {
  cursor: pointer;
}

.icon-btn {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #4e5969;
  font-size: 20px;
  transition: all 0.2s;
  position: relative;
}

.icon-btn:hover {
  background: #f2f3f5;
  color: #1d2129;
}

.dot-indicator {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 8px;
  height: 8px;
  background: #f53f3f;
  border-radius: 50%;
  border: 2px solid #fff;
}

.user-profile-trigger {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 4px 8px;
  padding-right: 4px;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.2s;
}

.user-profile-trigger:hover {
  background: #f2f3f5;
}

.avatar-wrapper {
  position: relative;
}

.custom-avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.online-status {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 10px;
  height: 10px;
  background: #00b42a;
  border-radius: 50%;
  border: 2px solid #fff;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 4px;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: #1d2129;
}

.arrow-icon {
  font-size: 12px;
  color: #86909c;
}

/* Dropdown styling */
:deep(.user-dropdown-popper) {
  border-radius: 12px !important;
  padding: 4px !important;
  border: 1px solid #f2f3f5 !important;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1) !important;
}

.dropdown-header {
  padding: 12px 16px;
  border-bottom: 1px solid #f2f3f5;
  margin-bottom: 4px;
}

.dropdown-user-info {
  display: flex;
  flex-direction: column;
}

.dropdown-user-info strong {
  font-size: 15px;
  color: #1d2129;
}

.dropdown-user-info span {
  font-size: 12px;
  color: #86909c;
}

.logout-item {
  color: #f53f3f !important;
}

/* Main Content Area */
.front-main {
  flex: 1;
  padding-bottom: 40px;
}

.main-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
}

/* Footer Styling */
.front-footer {
  padding: 30px 0;
  text-align: center;
  color: #86909c;
  font-size: 13px;
  border-top: 1px solid #e5e6eb;
  background: #fff;
}

/* Transition */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s ease;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Mobile Responsive */
@media (max-width: 1024px) {
  .logo-text {
    display: none;
  }
  
  .top-nav {
    margin: 0 10px;
  }
  
  .nav-link {
    padding: 0 12px;
  }
  
  .nav-text {
    display: none;
  }
  
  .user-name {
    display: none;
  }
}
</style>
