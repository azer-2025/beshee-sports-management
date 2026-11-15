<template>
  <div class="app-container">
    <el-alert
      v-if="!loading && total === 0"
      title="暂无借还记录。学生端提交预约后会在此展示审批记录。"
      type="info"
      :closable="false"
      show-icon
      class="mb12"
    />
    <el-form ref="queryRef" :model="queryParams" :inline="true" v-show="showSearch">
      <el-form-item label="申请用户" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入用户名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 160px">
          <el-option v-for="dict in sp_borrow_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="12" class="mb12">
      <el-col :xs="12" :md="4">
        <el-card shadow="hover" class="dashboard-card">
          <div class="label">逾期总数</div>
          <div class="value danger">{{ dashboard.totalOverdue || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="4">
        <el-card shadow="hover" class="dashboard-card">
          <div class="label">严重逾期(≥7天)</div>
          <div class="value warning">{{ dashboard.severeOverdue || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="4">
        <el-card shadow="hover" class="dashboard-card">
          <div class="label">今日新增逾期</div>
          <div class="value">{{ dashboard.todayNewOverdue || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="4">
        <el-card shadow="hover" class="dashboard-card">
          <div class="label">待回填原因</div>
          <div class="value">{{ dashboard.pendingReasonFill || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="4">
        <el-card shadow="hover" class="dashboard-card">
          <div class="label">平均逾期天数</div>
          <div class="value">{{ dashboard.avgOverdueDays || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="4">
        <el-card shadow="hover" class="dashboard-card">
          <div class="label">原因回填率</div>
          <div class="value">{{ fillRate }}%</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row class="mb12">
      <el-col :span="24">
        <el-card shadow="never">
          <template #header>逾期原因分布 Top</template>
          <el-space wrap>
            <el-tag v-for="item in reasonStats" :key="item.reasonLabel">{{ item.reasonLabel }}: {{ item.total }}</el-tag>
          </el-space>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Bell" @click="loadOverdue">逾期预警</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="borrowList" empty-text="暂无借还记录">
      <el-table-column label="记录ID" prop="borrowId" width="88" />
      <el-table-column label="用户" prop="userName" width="120" />
      <el-table-column label="器材" prop="assetName" min-width="140" />
      <el-table-column label="编码" prop="assetCode" min-width="120" />
      <el-table-column label="数量" prop="quantity" width="80" />
      <el-table-column label="预约开始" min-width="160">
        <template #default="scope">{{ parseTime(scope.row.reserveStartTime, "{y}-{m}-{d} {h}:{i}") }}</template>
      </el-table-column>
      <el-table-column label="预计归还" min-width="160">
        <template #default="scope">{{ parseTime(scope.row.expectedReturnTime, "{y}-{m}-{d} {h}:{i}") }}</template>
      </el-table-column>
      <el-table-column label="状态" width="110">
        <template #default="scope">
          <dict-tag :options="sp_borrow_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="逾期天数" prop="overdueDays" width="90" />
      <el-table-column label="逾期原因" prop="overdueReason" min-width="160" show-overflow-tooltip />
      <el-table-column label="驳回原因" prop="rejectReason" min-width="150" show-overflow-tooltip />
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="scope">
          <el-button
            v-if="scope.row.status === '0'"
            link
            type="success"
            icon="Check"
            @click="openApprove(scope.row, '1')"
            v-hasPermi="['sports:borrow:approve']"
          >通过</el-button>
          <el-button
            v-if="scope.row.status === '0'"
            link
            type="warning"
            icon="Close"
            @click="openApprove(scope.row, '2')"
            v-hasPermi="['sports:borrow:approve']"
          >驳回</el-button>
          <el-button
            v-if="scope.row.status === '3' || scope.row.status === '5'"
            link
            type="primary"
            icon="RefreshRight"
            @click="handleReturn(scope.row)"
            v-hasPermi="['sports:borrow:return']"
          >登记归还</el-button>
          <el-button
            v-if="scope.row.overdueDays > 0"
            link
            type="info"
            icon="EditPen"
            @click="openReasonDialog(scope.row)"
            v-hasPermi="['sports:borrow:approve']"
          >回填原因</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="借还审批" v-model="approveOpen" width="540px" append-to-body>
      <el-form ref="approveRef" :model="approveForm" :rules="approveRules" label-width="100px">
        <el-form-item label="审批结果" prop="status">
          <el-radio-group v-model="approveForm.status">
            <el-radio value="1">通过</el-radio>
            <el-radio value="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="approveForm.status === '1'" label="预计归还" prop="expectedReturnTime">
          <el-date-picker
            v-model="approveForm.expectedReturnTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item v-if="approveForm.status === '2'" label="驳回原因" prop="rejectReason">
          <el-input v-model="approveForm.rejectReason" type="textarea" :rows="3" maxlength="200" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitApprove">确定</el-button>
          <el-button @click="approveOpen = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="逾期预警列表" v-model="overdueOpen" width="860px" append-to-body>
      <el-row class="mb12">
        <el-col :span="24">
          <el-switch
            v-model="overdueQuery.pendingReasonOnly"
            active-value="1"
            inactive-value="0"
            active-text="仅看未回填原因"
            @change="loadOverdue"
          />
        </el-col>
      </el-row>
      <el-table :data="overdueList" empty-text="暂无逾期记录">
        <el-table-column label="用户" prop="userName" width="120" />
        <el-table-column label="器材" prop="assetName" min-width="140" />
        <el-table-column label="预计归还" min-width="160">
          <template #default="scope">{{ parseTime(scope.row.expectedReturnTime, "{y}-{m}-{d} {h}:{i}") }}</template>
        </el-table-column>
        <el-table-column label="逾期天数" prop="overdueDays" width="100" />
        <el-table-column label="逾期原因" prop="overdueReason" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="110">
          <template #default="scope">
            <el-button
              v-if="scope.row.overdueDays > 0"
              link
              type="primary"
              icon="EditPen"
              @click="openReasonDialog(scope.row)"
              v-hasPermi="['sports:borrow:approve']"
            >回填</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog title="回填逾期原因" v-model="reasonOpen" width="520px" append-to-body>
      <el-form ref="reasonRef" :model="reasonForm" :rules="reasonRules" label-width="90px">
        <el-form-item label="逾期原因" prop="overdueReason">
          <el-input v-model="reasonForm.overdueReason" type="textarea" :rows="4" maxlength="255" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitReason">保存</el-button>
          <el-button @click="reasonOpen = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SportsBorrow">
import {
  listBorrow,
  approveBorrow,
  returnBorrow,
  listOverdueBorrow,
  getOverdueDashboard,
  getOverdueReasonStats,
  fillOverdueReason
} from "@/api/sports/borrow";

const { proxy } = getCurrentInstance();
const { sp_borrow_status } = proxy.useDict("sp_borrow_status");

const loading = ref(false);
const showSearch = ref(true);
const total = ref(0);
const borrowList = ref([]);
const dashboard = ref({});
const approveOpen = ref(false);
const overdueOpen = ref(false);
const reasonOpen = ref(false);
const overdueList = ref([]);
const reasonStats = ref([]);
const reminderSnapshot = ref({
  pendingBorrow: 0,
  pendingOverdueReason: 0
});

let reminderTimer = null;
const REMINDER_INTERVAL = 15000;

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userName: undefined,
    status: undefined
  },
  approveForm: {
    borrowId: undefined,
    status: "1",
    expectedReturnTime: undefined,
    rejectReason: undefined
  },
  reasonForm: {
    borrowId: undefined,
    overdueReason: undefined
  },
  overdueQuery: {
    pageNum: 1,
    pageSize: 100,
    pendingReasonOnly: "0"
  },
  approveRules: {
    status: [{ required: true, message: "请选择审批结果", trigger: "change" }],
    expectedReturnTime: [{ required: true, message: "请填写预计归还时间", trigger: "change" }],
    rejectReason: [{ required: true, message: "请填写驳回原因", trigger: "blur" }]
  },
  reasonRules: {
    overdueReason: [{ required: true, message: "请填写逾期原因", trigger: "blur" }]
  }
});

const { queryParams, approveForm, reasonForm, overdueQuery, approveRules, reasonRules } = toRefs(data);

const fillRate = computed(() => {
  const total = Number(dashboard.value.totalOverdue || 0);
  const pending = Number(dashboard.value.pendingReasonFill || 0);
  if (total <= 0) return 100;
  return ((total - pending) * 100 / total).toFixed(1);
});

function getList() {
  loading.value = true;
  listBorrow(queryParams.value).then(res => {
    borrowList.value = res.rows || [];
    total.value = res.total || 0;
  }).finally(() => {
    loading.value = false;
  });
  loadDashboard();
  pollReminders(true);
}

function loadDashboard() {
  getOverdueDashboard().then(res => {
    dashboard.value = res.data || {};
  }).catch(() => {
    dashboard.value = {};
  });
  loadReasonStats();
}

function loadReasonStats() {
  getOverdueReasonStats().then(res => {
    reasonStats.value = res.data || [];
  }).catch(() => {
    reasonStats.value = [];
  });
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

function openApprove(row, status) {
  approveForm.value = {
    borrowId: row.borrowId,
    status: status,
    expectedReturnTime: row.expectedReturnTime,
    rejectReason: undefined
  };
  approveOpen.value = true;
}

function submitApprove() {
  proxy.$refs.approveRef.validate(valid => {
    if (!valid) return;
    if (approveForm.value.status === "2" && !approveForm.value.rejectReason) {
      proxy.$modal.msgError("请填写驳回原因");
      return;
    }
    approveBorrow(approveForm.value).then(() => {
      proxy.$modal.msgSuccess("审批完成");
      approveOpen.value = false;
      getList();
    });
  });
}

function handleReturn(row) {
  proxy.$modal.confirm("确认该记录已归还？").then(() => {
    return returnBorrow(row.borrowId);
  }).then(() => {
    proxy.$modal.msgSuccess("归还登记成功");
    getList();
  }).catch(() => {});
}

function loadOverdue() {
  listOverdueBorrow(overdueQuery.value).then(res => {
    overdueList.value = res.rows || [];
    overdueOpen.value = true;
  }).catch(() => {
    overdueList.value = [];
    overdueOpen.value = true;
  });
}

function openReasonDialog(row) {
  reasonForm.value = {
    borrowId: row.borrowId,
    overdueReason: row.overdueReason
  };
  reasonOpen.value = true;
}

function submitReason() {
  proxy.$refs.reasonRef.validate(valid => {
    if (!valid) return;
    fillOverdueReason(reasonForm.value).then(() => {
      proxy.$modal.msgSuccess("逾期原因已回填");
      reasonOpen.value = false;
      getList();
      if (overdueOpen.value) {
        loadOverdue();
      }
    });
  });
}

function pollReminders(silent = false) {
  Promise.all([
    listBorrow({ pageNum: 1, pageSize: 1, status: "0" }),
    listOverdueBorrow({ pageNum: 1, pageSize: 1, pendingReasonOnly: "1" })
  ]).then(([pendingBorrowRes, pendingReasonRes]) => {
    const pendingBorrow = Number(pendingBorrowRes.total || 0);
    const pendingOverdueReason = Number(pendingReasonRes.total || 0);
    const prev = reminderSnapshot.value;
    if (!silent) {
      if (pendingBorrow > prev.pendingBorrow) {
        proxy.$modal.msgWarning(`New pending borrow approvals: ${pendingBorrow - prev.pendingBorrow}`);
      }
      if (pendingOverdueReason > prev.pendingOverdueReason) {
        proxy.$modal.msgWarning(`New overdue reason follow-ups: ${pendingOverdueReason - prev.pendingOverdueReason}`);
      }
    }
    reminderSnapshot.value = { pendingBorrow, pendingOverdueReason };
  }).catch(() => {});
}

onMounted(() => {
  getList();
  pollReminders(true);
  reminderTimer = setInterval(() => {
    pollReminders(false);
  }, REMINDER_INTERVAL);
});

onBeforeUnmount(() => {
  if (reminderTimer) {
    clearInterval(reminderTimer);
    reminderTimer = null;
  }
});
</script>

<style scoped>
.mb12 {
  margin-bottom: 12px;
}
.dashboard-card .label {
  color: #909399;
  font-size: 13px;
}
.dashboard-card .value {
  margin-top: 8px;
  font-size: 24px;
  font-weight: 700;
  color: #303133;
}
.dashboard-card .value.warning {
  color: #e6a23c;
}
.dashboard-card .value.danger {
  color: #f56c6c;
}
</style>
