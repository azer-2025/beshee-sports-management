<template>
  <div class="repair-page">
    <el-row :gutter="16" class="summary-row">
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="sum-card">
          <div class="sum-title">全部报修</div>
          <div class="sum-value">{{ total }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="sum-card warning">
          <div class="sum-title">待受理</div>
          <div class="sum-value">{{ noticeStore.summary.pendingRepair || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="sum-card primary">
          <div class="sum-title">处理中</div>
          <div class="sum-value">{{ noticeStore.summary.processingRepair || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="sum-card success">
          <div class="sum-title">已完成</div>
          <div class="sum-value">{{ fixedCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="openReport">提交报修</el-button>
      </div>

      <el-table :data="repairList" v-loading="loading" border>
        <el-table-column prop="repairId" label="报修ID" width="90" align="center" />
        <el-table-column prop="assetName" label="器材名称" min-width="160" />
        <el-table-column prop="faultDesc" label="故障描述" min-width="220" show-overflow-tooltip />
        <el-table-column label="图片" width="90" align="center">
          <template #default="scope">
            <el-image v-if="scope.row.faultImage" :src="toImage(scope.row.faultImage)" style="width: 40px; height: 40px" fit="cover" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120" align="center">
          <template #default="scope">
            <el-tag :type="statusType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="反馈" min-width="180" show-overflow-tooltip>
          <template #default="scope">{{ scope.row.processNote || '-' }}</template>
        </el-table-column>
        <el-table-column label="时间" min-width="160">
          <template #default="scope">{{ formatDate(scope.row.createTime) }}</template>
        </el-table-column>
      </el-table>

      <div class="pager-wrap">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :total="total"
          :page-size="queryParams.pageSize"
          :current-page="queryParams.pageNum"
          @current-change="onPageChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="open" title="提交报修" width="620px" destroy-on-close>
      <el-form ref="repairRef" :model="repairForm" :rules="rules" label-width="92px">
        <el-form-item label="器材" prop="assetId">
          <el-select v-model="repairForm.assetId" filterable placeholder="请选择器材" style="width: 100%">
            <el-option
              v-for="item in assetOptions"
              :key="item.assetId"
              :label="`${item.assetName} [${item.assetCode}]`"
              :value="item.assetId"
            />
          </el-select>
        </el-form-item>

        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="损耗原因" prop="faultReason">
              <el-select v-model="repairForm.faultReason" style="width: 100%">
                <el-option label="自然损耗" value="0" />
                <el-option label="使用不当" value="1" />
                <el-option label="外部损坏" value="2" />
                <el-option label="其他" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急程度" prop="urgency">
              <el-radio-group v-model="repairForm.urgency">
                <el-radio-button label="普通" value="0" />
                <el-radio-button label="紧急" value="1" />
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="故障描述" prop="faultDesc">
          <el-input v-model="repairForm.faultDesc" type="textarea" :rows="4" maxlength="300" show-word-limit />
        </el-form-item>

        <el-form-item label="故障图片">
          <el-upload :show-file-list="false" :http-request="customUpload" :before-upload="beforeUpload">
            <div class="upload-box">
              <img v-if="repairForm.faultImage" :src="toImage(repairForm.faultImage)" class="upload-img" />
              <template v-else>
                <el-icon><Camera /></el-icon>
                <span>上传图片</span>
              </template>
            </div>
          </el-upload>
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="repairForm.remark" maxlength="100" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitRepair">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { Camera, Plus } from "@element-plus/icons-vue";
import { myRepairList, reportRepair } from "@/api/repair";
import { listFrontEquipment } from "@/api/equipment";
import { uploadFile } from "@/api/common";
import { useNoticeStore } from "@/store/modules/notice";

const noticeStore = useNoticeStore();

const loading = ref(false);
const submitLoading = ref(false);
const total = ref(0);
const repairList = ref([]);
const open = ref(false);
const repairRef = ref();
const assetOptions = ref([]);

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10
});

const repairForm = reactive({
  assetId: undefined,
  faultDesc: "",
  faultImage: "",
  faultReason: "0",
  urgency: "0",
  remark: ""
});

const rules = {
  assetId: [{ required: true, message: "请选择器材", trigger: "change" }],
  faultDesc: [{ required: true, message: "请填写故障描述", trigger: "blur" }]
};

const statusMap = {
  "0": { label: "待受理", type: "warning" },
  "1": { label: "已受理", type: "info" },
  "2": { label: "维修中", type: "primary" },
  "3": { label: "已修复", type: "success" },
  "4": { label: "已驳回", type: "danger" }
};

const fixedCount = computed(() => repairList.value.filter((i) => i.status === "3").length);

const statusLabel = (status) => statusMap[status]?.label || "未知";
const statusType = (status) => statusMap[status]?.type || "info";

const formatDate = (value) => {
  if (!value) return "-";
  return String(value).replace("T", " ").slice(0, 16);
};

const toImage = (src) => {
  if (!src) return "";
  if (src.startsWith("http://") || src.startsWith("https://")) return src;
  return `${import.meta.env.VITE_APP_BASE_API}${src}`;
};

const getList = async () => {
  loading.value = true;
  try {
    const res = await myRepairList(queryParams);
    repairList.value = res.rows || [];
    total.value = Number(res.total || 0);
  } finally {
    loading.value = false;
  }
};

const loadAssets = async () => {
  const res = await listFrontEquipment({ pageNum: 1, pageSize: 500 });
  assetOptions.value = res.rows || [];
};

const onPageChange = (page) => {
  queryParams.pageNum = page;
  getList();
};

const openReport = async () => {
  await loadAssets();
  repairForm.assetId = undefined;
  repairForm.faultDesc = "";
  repairForm.faultImage = "";
  repairForm.faultReason = "0";
  repairForm.urgency = "0";
  repairForm.remark = "";
  open.value = true;
};

const beforeUpload = (file) => {
  const isImage = file.type.startsWith("image/");
  const isLt5M = file.size / 1024 / 1024 < 5;
  if (!isImage) ElMessage.error("仅支持图片格式");
  if (!isLt5M) ElMessage.error("图片大小不能超过 5MB");
  return isImage && isLt5M;
};

const customUpload = async (params) => {
  try {
    const res = await uploadFile(params.file);
    repairForm.faultImage = res.url || res.fileName || "";
    params.onSuccess?.(res);
    ElMessage.success("上传成功");
  } catch (error) {
    params.onError?.(error);
    ElMessage.error("上传失败");
  }
};

const submitRepair = () => {
  repairRef.value.validate(async (valid) => {
    if (!valid) return;
    submitLoading.value = true;
    try {
      await reportRepair(repairForm);
      ElMessage.success("报修已提交");
      open.value = false;
      await noticeStore.refreshSummary();
      getList();
    } finally {
      submitLoading.value = false;
    }
  });
};

getList();
</script>

<style scoped>
.repair-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.sum-card {
  border-radius: 14px;
}

.sum-title {
  color: #6f8098;
  font-size: 13px;
}

.sum-value {
  margin-top: 8px;
  font-size: 28px;
  font-weight: 700;
  color: #274261;
}

.sum-card.warning .sum-value {
  color: #d9901e;
}

.sum-card.primary .sum-value {
  color: #2f83d1;
}

.sum-card.success .sum-value {
  color: #22a16c;
}

.table-card {
  border-radius: 14px;
}

.toolbar {
  margin-bottom: 12px;
}

.upload-box {
  width: 120px;
  height: 120px;
  border-radius: 10px;
  border: 1px dashed #d4deea;
  background: #f7faff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #7691af;
  cursor: pointer;
}

.upload-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 10px;
}

.pager-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 14px;
}
</style>
