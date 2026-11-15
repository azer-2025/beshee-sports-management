<template>
  <div class="login-page">
    <div class="login-card">
      <section class="brand-panel">
        <div class="brand-icon">
          <el-icon><Bicycle /></el-icon>
        </div>
        <h1>体育器材管理平台</h1>
        <p>学生/教练在线预约借还、异常报修、消息提醒一体化</p>
      </section>

      <section class="form-panel">
        <h2>账号登录</h2>
        <p class="sub">请输入账号信息继续使用系统</p>

        <el-form ref="loginRef" :model="form" :rules="rules" label-position="top">
          <el-form-item prop="username" label="账号">
            <el-input
              v-model="form.username"
              placeholder="请输入账号"
              :prefix-icon="User"
              @keyup.enter="submitLogin"
            />
          </el-form-item>

          <el-form-item prop="password" label="密码">
            <el-input
              v-model="form.password"
              type="password"
              show-password
              placeholder="请输入密码"
              :prefix-icon="Lock"
              @keyup.enter="submitLogin"
            />
          </el-form-item>

          <el-form-item v-if="captchaEnabled" prop="code" label="验证码">
            <div class="captcha-row">
              <el-input
                v-model="form.code"
                placeholder="请输入验证码"
                :prefix-icon="Key"
                @keyup.enter="submitLogin"
              />
              <div class="captcha-box" @click="getCode">
                <img v-if="codeUrl" :src="codeUrl" alt="captcha" />
                <el-icon v-else class="is-loading"><Loading /></el-icon>
              </div>
            </div>
          </el-form-item>

          <div class="action-row">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          </div>

          <el-button type="primary" :loading="loading" class="submit-btn" @click="submitLogin">
            登录
          </el-button>
        </el-form>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { User, Lock, Key, Loading, Bicycle } from "@element-plus/icons-vue";
import { getCodeImg } from "@/api/auth";
import { useUserStore } from "@/store/modules/user";

const router = useRouter();
const userStore = useUserStore();

const loginRef = ref();
const loading = ref(false);
const captchaEnabled = ref(true);
const codeUrl = ref("");
const rememberMe = ref(false);

const form = reactive({
  username: "",
  password: "",
  code: "",
  uuid: ""
});

const rules = {
  username: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  code: [
    {
      validator: (_rule, value, callback) => {
        if (!captchaEnabled.value || value) {
          callback();
          return;
        }
        callback(new Error("请输入验证码"));
      },
      trigger: "blur"
    }
  ]
};

const getCode = async () => {
  try {
    const res = await getCodeImg();
    captchaEnabled.value = res.captchaEnabled !== false;
    if (captchaEnabled.value) {
      codeUrl.value = `data:image/gif;base64,${res.img}`;
      form.uuid = res.uuid;
      form.code = "";
    }
  } catch (error) {
    codeUrl.value = "";
  }
};

const submitLogin = () => {
  loginRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      await userStore.loginAction(form);
      await userStore.fetchInfo();
      ElMessage.success("登录成功");
      router.push("/home");
    } catch (error) {
      ElMessage.error(error?.message || "登录失败");
      await getCode();
    } finally {
      loading.value = false;
    }
  });
};

getCode();
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: radial-gradient(circle at top left, #d9efff 0%, #f4f8fc 45%, #ffffff 100%);
}

.login-card {
  width: 100%;
  max-width: 980px;
  min-height: 580px;
  border-radius: 20px;
  overflow: hidden;
  display: grid;
  grid-template-columns: 1.1fr 1fr;
  background: #fff;
  box-shadow: 0 22px 56px rgba(22, 69, 132, 0.18);
}

.brand-panel {
  padding: 56px 46px;
  color: #fff;
  background: linear-gradient(145deg, #0f7af0 0%, #1b9cff 50%, #0f6ee0 100%);
}

.brand-icon {
  width: 62px;
  height: 62px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  font-size: 30px;
}

.brand-panel h1 {
  margin: 18px 0 12px;
  font-size: 34px;
  line-height: 1.2;
}

.brand-panel p {
  margin: 0;
  line-height: 1.8;
  opacity: 0.92;
}

.form-panel {
  padding: 56px 44px;
}

.form-panel h2 {
  margin: 0;
  font-size: 28px;
  color: #1f2f44;
}

.sub {
  margin: 8px 0 24px;
  color: #7f8da0;
}

.captcha-row {
  display: grid;
  grid-template-columns: 1fr 120px;
  gap: 10px;
}

.captcha-box {
  height: 40px;
  border: 1px solid #dbe4ef;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  cursor: pointer;
  background: #f8fbff;
}

.captcha-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.action-row {
  margin-bottom: 16px;
}

.submit-btn {
  width: 100%;
  height: 44px;
  font-weight: 600;
}

@media (max-width: 900px) {
  .login-card {
    grid-template-columns: 1fr;
  }

  .brand-panel {
    min-height: 200px;
    padding: 34px 28px;
  }

  .form-panel {
    padding: 32px 24px;
  }
}
</style>
