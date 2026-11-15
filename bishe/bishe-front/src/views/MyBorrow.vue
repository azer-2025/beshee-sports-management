<template>
  <div class="borrow-page">
    <el-row :gutter="16" class="summary-row">
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="sum-card">
          <div class="sum-title">全部</div>
          <div class="sum-value">{{ summary.total }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="sum-card warning">
          <div class="sum-title">待审批</div>
          <div class="sum-value">{{ summary.pending }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="sum-card success">
          <div class="sum-title">借出中</div>
          <div class="sum-value">{{ summary.borrowing }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="sum-card danger">
          <div class="sum-title">已逾期</div>
          <div class="sum-value">{{ summary.overdue }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="table-card">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" clearable placeholder="全部" style="width: 180px">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="borrowList" v-loading="loading" border>
        <el-table-column prop="assetName" label="器材名称" min-width="160" />
        <el-table-column prop="assetCode" label="器材编码" width="140" />
        <el-table-column prop="quantity" label="数量" width="80" align="center" />
        <el-table-column label="预约时间" min-width="160">
          <template #default="scope">{{ formatDate(scope.row.reserveStartTime) }}</template>
        </el-table-column>
        <el-table-column label="应还时间" min-width="160">
          <template #default="scope">{{ formatDate(scope.row.expectedReturnTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="120" align="center">
          <template #default="scope">
            <el-tag :type="statusType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="反馈" min-width="180" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.rejectReason || scope.row.overdueReason || scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="评价" min-width="220">
          <template #default="scope">
            <div v-if="hasReviewed(scope.row)" class="review-cell">
              <el-rate
                :model-value="avgScore(scope.row)"
                disabled
                allow-half
                show-score
                score-template="{value} 分"
                text-color="#f59e0b"
              />
              <div class="review-extra" v-if="scope.row.reviewContent">{{ scope.row.reviewContent }}</div>
            </div>
            <span v-else class="review-empty">待评价</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="170" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.status === '0'" type="danger" link @click="handleCancel(scope.row)">取消预约</el-button>
            <el-button v-if="scope.row.status === '3' || scope.row.status === '5'" type="primary" link @click="handleReturn(scope.row)">
              提交归还
            </el-button>
            <el-button
              v-if="scope.row.status === '4' && !hasReviewed(scope.row)"
              type="success"
              link
              @click="openReview(scope.row)"
            >
              去评价
            </el-button>
          </template>
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

    <el-dialog v-model="reviewOpen" title="借还评价" width="520px" append-to-body>
      <el-form ref="reviewRef" :model="reviewForm" :rules="reviewRules" label-width="92px">
        <el-form-item label="质量评分" prop="reviewQualityScore">
          <el-rate v-model="reviewForm.reviewQualityScore" />
        </el-form-item>
        <el-form-item label="包装评分" prop="reviewPackageScore">
          <el-rate v-model="reviewForm.reviewPackageScore" />
        </el-form-item>
        <el-form-item label="服务评分" prop="reviewServiceScore">
          <el-rate v-model="reviewForm.reviewServiceScore" />
        </el-form-item>
        <el-form-item label="文字评价" prop="reviewContent">
          <el-input
            v-model="reviewForm.reviewContent"
            type="textarea"
            :rows="4"
            maxlength="500"
            show-word-limit
            placeholder="请描述你的使用体验"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewOpen = false">取消</el-button>
        <el-button type="primary" :loading="reviewSubmitting" @click="submitReview">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Search, Refresh } from "@element-plus/icons-vue";
import { cancelBorrow, myBorrowList, returnBorrow, reviewBorrow } from "@/api/borrow";
import { useNoticeStore } from "@/store/modules/notice";

const noticeStore = useNoticeStore();

const loading = ref(false);
const total = ref(0);
const borrowList = ref([]);
const reviewOpen = ref(false);
const reviewSubmitting = ref(false);
const reviewRef = ref();

const reviewForm = reactive({
  borrowId: undefined,
  reviewQualityScore: 5,
  reviewPackageScore: 5,
  reviewServiceScore: 5,
  reviewContent: ""
});

const reviewRules = {
  reviewQualityScore: [{ required: true, message: "请评分", trigger: "change" }],
  reviewPackageScore: [{ required: true, message: "请评分", trigger: "change" }],
  reviewServiceScore: [{ required: true, message: "请评分", trigger: "change" }]
};

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  status: ""
});

const statusOptions = [
  { label: "待审批", value: "0" },
  { label: "已审批", value: "1" },
  { label: "已驳回", value: "2" },
  { label: "借出中", value: "3" },
  { label: "已归还", value: "4" },
  { label: "已逾期", value: "5" }
];

const statusMap = {
  "0": { label: "待审批", type: "warning" },
  "1": { label: "已审批", type: "info" },
  "2": { label: "已驳回", type: "danger" },
  "3": { label: "借出中", type: "success" },
  "4": { label: "已归还", type: "info" },
  "5": { label: "已逾期", type: "danger" }
};

const statusLabel = (status) => statusMap[status]?.label || "未知";
const statusType = (status) => statusMap[status]?.type || "info";

const summary = computed(() => ({
  total: total.value,
  pending: noticeStore.summary.pendingBorrow || 0,
  borrowing: borrowList.value.filter((i) => i.status === "3").length,
  overdue: noticeStore.summary.overdueBorrow || 0
}));

const formatDate = (value) => {
  if (!value) return "-";
  return String(value).replace("T", " ").slice(0, 16);
};

const hasReviewed = (row) => {
  return row.reviewQualityScore != null && row.reviewPackageScore != null && row.reviewServiceScore != null;
};

const avgScore = (row) => {
  if (!hasReviewed(row)) return 0;
  return Number(((Number(row.reviewQualityScore) + Number(row.reviewPackageScore) + Number(row.reviewServiceScore)) / 3).toFixed(1));
};

const getList = async () => {
  loading.value = true;
  try {
    const params = { ...queryParams, status: queryParams.status || undefined };
    const res = await myBorrowList(params);
    borrowList.value = res.rows || [];
    total.value = Number(res.total || 0);
  } finally {
    loading.value = false;
  }
};

const handleQuery = () => {
  queryParams.pageNum = 1;
  getList();
};

const resetQuery = () => {
  queryParams.status = "";
  handleQuery();
};

const onPageChange = (page) => {
  queryParams.pageNum = page;
  getList();
};

const handleCancel = (row) => {
  ElMessageBox.confirm("确认取消该预约吗？", "提示", { type: "warning" })
    .then(async () => {
      await cancelBorrow(row.borrowId);
      ElMessage.success("取消成功");
      await noticeStore.refreshSummary();
      getList();
    })
    .catch(() => {});
};

const handleReturn = (row) => {
  ElMessageBox.confirm("确认提交归还申请吗？", "提示", { type: "info" })
    .then(async () => {
      await returnBorrow(row.borrowId);
      ElMessage.success("归还申请已提交");
      await noticeStore.refreshSummary();
      getList();
    })
    .catch(() => {});
};

const openReview = (row) => {
  reviewForm.borrowId = row.borrowId;
  reviewForm.reviewQualityScore = 5;
  reviewForm.reviewPackageScore = 5;
  reviewForm.reviewServiceScore = 5;
  reviewForm.reviewContent = "";
  reviewOpen.value = true;
};

const submitReview = () => {
  reviewRef.value.validate(async (valid) => {
    if (!valid) return;
    reviewSubmitting.value = true;
    try {
      await reviewBorrow({ ...reviewForm });
      ElMessage.success("评价提交成功");
      reviewOpen.value = false;
      await noticeStore.refreshSummary();
      getList();
    } finally {
      reviewSubmitting.value = false;
    }
  });
};

getList();
</script>

<style scoped>
.borrow-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.summary-row {
  margin-bottom: 0;
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

.sum-card.success .sum-value {
  color: #22a16c;
}

.sum-card.danger .sum-value {
  color: #d74a4a;
}

.table-card {
  border-radius: 14px;
}

.filter-form {
  margin-bottom: 10px;
}

.pager-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 14px;
}

.review-cell {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.review-extra {
  color: #6b7280;
  font-size: 12px;
  line-height: 1.4;
}

.review-empty {
  color: #9ca3af;
}
</style>
