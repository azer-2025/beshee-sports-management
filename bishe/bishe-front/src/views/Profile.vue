<template>
  <div class="profile-page">
    <el-row :gutter="16">
      <el-col :xs="24" :md="8">
        <el-card shadow="never" class="user-card">
          <div class="avatar-wrap">
            <el-avatar :src="avatarUrl" :size="110">{{ (profileInfo.nickName || 'U').slice(0, 1) }}</el-avatar>
            <el-upload :show-file-list="false" :http-request="handleAvatarUpload" :before-upload="beforeAvatarUpload">
              <el-button size="small" plain>更换头像</el-button>
            </el-upload>
          </div>

          <h3>{{ profileInfo.nickName || '-' }}</h3>
          <p class="account">{{ profileInfo.userName || '-' }}</p>

          <div class="tags-row">
            <el-tag size="small" type="success">{{ profileInfo.status === '0' ? '账号正常' : '账号停用' }}</el-tag>
            <el-tag size="small" type="info">{{ roleGroup || '未分配角色' }}</el-tag>
          </div>

          <el-divider />

          <div class="line"><span>用户ID</span><span>{{ profileInfo.userId || '-' }}</span></div>
          <div class="line"><span>学号/教练号</span><span>{{ extForm.studentNo || extForm.coachNo || '-' }}</span></div>
          <div class="line"><span>注册时间</span><span>{{ formatDate(profileInfo.createTime) }}</span></div>
        </el-card>

        <el-card shadow="never" class="stats-card">
          <template #header>
            <span>使用概览</span>
          </template>
          <div class="stats-grid">
            <div class="stat-item">
              <div class="label">借还总数</div>
              <div class="value">{{ stats.borrowTotal }}</div>
            </div>
            <div class="stat-item">
              <div class="label">借出中</div>
              <div class="value">{{ stats.borrowing }}</div>
            </div>
            <div class="stat-item">
              <div class="label">逾期数</div>
              <div class="value danger">{{ stats.overdue }}</div>
            </div>
            <div class="stat-item">
              <div class="label">报修总数</div>
              <div class="value">{{ stats.repairTotal }}</div>
            </div>
            <div class="stat-item">
              <div class="label">待受理</div>
              <div class="value warning">{{ stats.pendingRepair }}</div>
            </div>
            <div class="stat-item">
              <div class="label">处理中</div>
              <div class="value primary">{{ stats.processingRepair }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="16">
        <el-card shadow="never" class="form-card">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本信息" name="base">
              <el-form ref="profileRef" :model="editForm" :rules="rules" label-width="92px">
                <el-row :gutter="12">
                  <el-col :span="12">
                    <el-form-item label="昵称" prop="nickName">
                      <el-input v-model="editForm.nickName" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="邮箱" prop="email">
                      <el-input v-model="editForm.email" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="12">
                  <el-col :span="12">
                    <el-form-item label="手机号" prop="phonenumber">
                      <el-input v-model="editForm.phonenumber" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="性别" prop="sex">
                      <el-radio-group v-model="editForm.sex">
                        <el-radio-button value="0">男</el-radio-button>
                        <el-radio-button value="1">女</el-radio-button>
                        <el-radio-button value="2">保密</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-divider>扩展档案</el-divider>

                <el-row :gutter="12">
                  <el-col :span="12">
                    <el-form-item label="学号">
                      <el-input v-model="extForm.studentNo" placeholder="学生填写，如 2026123456" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="教练编号">
                      <el-input v-model="extForm.coachNo" placeholder="教练填写，如 C-2026-01" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="12">
                  <el-col :span="12">
                    <el-form-item label="校区">
                      <el-input v-model="extForm.campus" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="学院">
                      <el-select v-model="extForm.college" placeholder="请选择学院" clearable style="width: 100%">
                        <el-option v-for="item in sp_college_type" :key="item.value" :label="item.label" :value="item.label" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="12">
                  <el-col :span="12">
                    <el-form-item label="专业">
                      <el-select v-model="extForm.majorName" placeholder="请选择专业" clearable style="width: 100%">
                        <el-option v-for="item in sp_major_type" :key="item.value" :label="item.label" :value="item.label" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="班级">
                      <el-input v-model="extForm.className" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="12">
                  <el-col :span="12">
                    <el-form-item label="紧急联系人">
                      <el-input v-model="extForm.emergencyContact" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="紧急电话">
                      <el-input v-model="extForm.emergencyPhone" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="12">
                  <el-col :span="12">
                    <el-form-item label="身份证号">
                      <el-input v-model="extForm.idNo" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="备注">
                      <el-input v-model="extForm.remark" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-form-item label="个人简介">
                  <el-input v-model="extForm.bio" type="textarea" :rows="3" maxlength="255" show-word-limit />
                </el-form-item>

                <el-form-item>
                  <el-button type="primary" :loading="saving" @click="submitProfile">保存资料</el-button>
                </el-form-item>
              </el-form>

              <el-divider>账号档案信息</el-divider>

              <el-descriptions :column="2" border>
                <el-descriptions-item label="登录账号">{{ profileInfo.userName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="角色组">{{ roleGroup || '-' }}</el-descriptions-item>
                <el-descriptions-item label="用户ID">{{ profileInfo.userId || '-' }}</el-descriptions-item>
                <el-descriptions-item label="性别文本">{{ sexLabel(editForm.sex) }}</el-descriptions-item>
                <el-descriptions-item label="学号">{{ extForm.studentNo || '-' }}</el-descriptions-item>
                <el-descriptions-item label="教练编号">{{ extForm.coachNo || '-' }}</el-descriptions-item>
                <el-descriptions-item label="校区">{{ extForm.campus || '-' }}</el-descriptions-item>
                <el-descriptions-item label="学院">{{ extForm.college || '-' }}</el-descriptions-item>
                <el-descriptions-item label="专业">{{ extForm.majorName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="班级">{{ extForm.className || '-' }}</el-descriptions-item>
                <el-descriptions-item label="紧急联系人">{{ extForm.emergencyContact || '-' }}</el-descriptions-item>
                <el-descriptions-item label="紧急电话">{{ extForm.emergencyPhone || '-' }}</el-descriptions-item>
              </el-descriptions>
            </el-tab-pane>

            <el-tab-pane label="修改密码" name="pwd">
              <el-form ref="pwdRef" :model="pwdForm" :rules="pwdRules" label-width="96px" class="pwd-form">
                <el-form-item label="当前密码" prop="oldPassword">
                  <el-input v-model="pwdForm.oldPassword" type="password" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="pwdForm.newPassword" type="password" show-password />
                </el-form-item>
                <el-form-item label="确认新密码" prop="confirmPassword">
                  <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" :loading="savingPwd" @click="submitPwd">更新密码</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, getCurrentInstance, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { getProfile, updatePassword, updateProfile } from "@/api/profile";
import { getProfileExt, updateProfileExt } from "@/api/profileExt";
import { uploadAvatar } from "@/api/common";
import { myBorrowList } from "@/api/borrow";
import { myRepairList } from "@/api/repair";
import { useUserStore } from "@/store/modules/user";

const router = useRouter();
const userStore = useUserStore();
const { proxy } = getCurrentInstance();
const { sp_college_type, sp_major_type } = proxy.useDict("sp_college_type", "sp_major_type");

const activeTab = ref("base");
const profileRef = ref();
const pwdRef = ref();
const saving = ref(false);
const savingPwd = ref(false);
const extApiReady = ref(true);

const roleGroup = ref("");
const postGroup = ref("");

const profileInfo = reactive({
  userId: undefined,
  userName: "",
  nickName: "",
  email: "",
  phonenumber: "",
  sex: "2",
  avatar: "",
  status: "0",
  dept: null,
  createTime: "",
  loginIp: "",
  loginDate: "",
  pwdUpdateDate: ""
});

const editForm = reactive({
  nickName: "",
  email: "",
  phonenumber: "",
  sex: "2"
});

const extForm = reactive({
  studentNo: "",
  coachNo: "",
  campus: "",
  college: "",
  majorName: "",
  className: "",
  idNo: "",
  emergencyContact: "",
  emergencyPhone: "",
  bio: "",
  remark: ""
});

const stats = reactive({
  borrowTotal: 0,
  borrowing: 0,
  overdue: 0,
  repairTotal: 0,
  pendingRepair: 0,
  processingRepair: 0
});

const pwdForm = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: ""
});

const rules = {
  nickName: [{ required: true, message: "请输入昵称", trigger: "blur" }],
  email: [{ type: "email", message: "邮箱格式不正确", trigger: "blur" }],
  phonenumber: [{ pattern: /^1[3-9]\d{9}$/, message: "手机号格式不正确", trigger: "blur" }]
};

const pwdRules = {
  oldPassword: [{ required: true, message: "请输入当前密码", trigger: "blur" }],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, max: 20, message: "长度 6-20 位", trigger: "blur" }
  ],
  confirmPassword: [
    { required: true, message: "请确认新密码", trigger: "blur" },
    {
      validator: (_rule, value, callback) => {
        if (value !== pwdForm.newPassword) {
          callback(new Error("两次输入密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur"
    }
  ]
};

const avatarUrl = computed(() => {
  const avatar = profileInfo.avatar;
  if (!avatar) return "";
  if (avatar.startsWith("http://") || avatar.startsWith("https://")) return avatar;
  return `${import.meta.env.VITE_APP_BASE_API}${avatar}`;
});

const sexLabel = (sex) => {
  if (sex === "0") return "男";
  if (sex === "1") return "女";
  return "保密";
};

const formatDate = (value) => {
  if (!value) return "-";
  return String(value).replace("T", " ").slice(0, 19);
};

const safeQuery = async (fn) => {
  try {
    return await fn();
  } catch (error) {
    return { total: 0, rows: [] };
  }
};

const loadStats = async () => {
  const [borrowTotalRes, borrowingRes, overdueRes, repairTotalRes, pendingRes, acceptedRes, processingRes] = await Promise.all([
    safeQuery(() => myBorrowList({ pageNum: 1, pageSize: 1 })),
    safeQuery(() => myBorrowList({ pageNum: 1, pageSize: 1, status: "3" })),
    safeQuery(() => myBorrowList({ pageNum: 1, pageSize: 1, status: "5" })),
    safeQuery(() => myRepairList({ pageNum: 1, pageSize: 1 })),
    safeQuery(() => myRepairList({ pageNum: 1, pageSize: 1, status: "0" })),
    safeQuery(() => myRepairList({ pageNum: 1, pageSize: 1, status: "1" })),
    safeQuery(() => myRepairList({ pageNum: 1, pageSize: 1, status: "2" }))
  ]);

  stats.borrowTotal = Number(borrowTotalRes.total || 0);
  stats.borrowing = Number(borrowingRes.total || 0);
  stats.overdue = Number(overdueRes.total || 0);
  stats.repairTotal = Number(repairTotalRes.total || 0);
  stats.pendingRepair = Number(pendingRes.total || 0);
  stats.processingRepair = Number(acceptedRes.total || 0) + Number(processingRes.total || 0);
};

const loadProfile = async () => {
  const res = await getProfile();
  const data = res.data || {};

  Object.assign(profileInfo, data);
  editForm.nickName = data.nickName || "";
  editForm.email = data.email || "";
  editForm.phonenumber = data.phonenumber || "";
  editForm.sex = data.sex || "2";

  roleGroup.value = res.roleGroup || "";
  postGroup.value = res.postGroup || "";
};

const loadProfileExt = async () => {
  try {
    const res = await getProfileExt();
    const data = res.data || {};
    extForm.studentNo = data.studentNo || "";
    extForm.coachNo = data.coachNo || "";
    extForm.campus = data.campus || "";
    extForm.college = data.college || "";
    extForm.majorName = data.majorName || "";
    extForm.className = data.className || "";
    extForm.idNo = data.idNo || "";
    extForm.emergencyContact = data.emergencyContact || "";
    extForm.emergencyPhone = data.emergencyPhone || "";
    extForm.bio = data.bio || "";
    extForm.remark = data.remark || "";
    extApiReady.value = true;
  } catch (error) {
    extApiReady.value = false;
  }
};

const submitProfile = () => {
  profileRef.value.validate(async (valid) => {
    if (!valid) return;
    if (extForm.emergencyPhone && !/^1[3-9]\d{9}$/.test(extForm.emergencyPhone)) {
      ElMessage.warning("紧急电话格式不正确");
      return;
    }
    saving.value = true;
    try {
      const tasks = [
        updateProfile({
          nickName: editForm.nickName,
          email: editForm.email,
          phonenumber: editForm.phonenumber,
          sex: editForm.sex
        })
      ];
      if (extApiReady.value) {
        tasks.push(updateProfileExt({
          studentNo: extForm.studentNo,
          coachNo: extForm.coachNo,
          campus: extForm.campus,
          college: extForm.college,
          majorName: extForm.majorName,
          className: extForm.className,
          idNo: extForm.idNo,
          emergencyContact: extForm.emergencyContact,
          emergencyPhone: extForm.emergencyPhone,
          bio: extForm.bio,
          remark: extForm.remark
        }));
      }
      await Promise.all(tasks);
      ElMessage.success("资料更新成功");
      if (!extApiReady.value) {
        ElMessage.warning("扩展档案未初始化，请先执行 sql/sports_profile_ext_increment.sql");
      }
      await userStore.fetchInfo();
      await Promise.all([loadProfile(), loadProfileExt()]);
    } finally {
      saving.value = false;
    }
  });
};

const beforeAvatarUpload = (file) => {
  const isImg = file.type.startsWith("image/");
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isImg) ElMessage.error("仅支持图片文件");
  if (!isLt2M) ElMessage.error("头像大小不能超过 2MB");
  return isImg && isLt2M;
};

const handleAvatarUpload = async (params) => {
  try {
    const res = await uploadAvatar(params.file);
    profileInfo.avatar = res.imgUrl || "";
    await userStore.fetchInfo();
    ElMessage.success("头像上传成功");
    params.onSuccess?.(res);
  } catch (error) {
    params.onError?.(error);
    ElMessage.error("头像上传失败");
  }
};

const submitPwd = () => {
  pwdRef.value.validate(async (valid) => {
    if (!valid) return;
    savingPwd.value = true;
    try {
      await updatePassword({
        oldPassword: pwdForm.oldPassword,
        newPassword: pwdForm.newPassword
      });
      ElMessage.success("密码修改成功，请重新登录");
      setTimeout(async () => {
        await userStore.logoutAction();
        router.push("/login");
      }, 800);
    } catch (error) {
      ElMessage.error(error?.message || "密码修改失败");
    } finally {
      savingPwd.value = false;
    }
  });
};
Promise.all([loadProfile(), loadProfileExt(), loadStats()]);
</script>

<style scoped>
.profile-page {
  padding-bottom: 20px;
}

.user-card,
.form-card,
.stats-card {
  border-radius: 14px;
}

.avatar-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.user-card h3 {
  margin: 14px 0 4px;
  text-align: center;
  font-size: 24px;
}

.account {
  margin: 0;
  text-align: center;
  color: #7488a1;
}

.tags-row {
  margin-top: 12px;
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.line {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 0;
  color: #4f647e;
}

.stats-card {
  margin-top: 16px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.stat-item {
  padding: 10px;
  border-radius: 10px;
  background: #f5f8fc;
}

.stat-item .label {
  color: #6f8098;
  font-size: 12px;
}

.stat-item .value {
  margin-top: 6px;
  font-size: 22px;
  font-weight: 700;
  color: #2e4968;
}

.stat-item .value.danger {
  color: #d44949;
}

.stat-item .value.warning {
  color: #d9901e;
}

.stat-item .value.primary {
  color: #2f83d1;
}

.pwd-form {
  max-width: 560px;
}

@media (max-width: 992px) {
  .stats-card {
    margin-top: 12px;
  }
}
</style>
