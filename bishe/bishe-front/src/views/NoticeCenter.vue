<template>
  <div class="notice-page">
    <el-card shadow="never" class="head-card">
      <div class="head-row">
        <div>
          <h3>消息中心</h3>
          <p>借还审批、逾期、报修进度会实时汇总在这里</p>
        </div>
        <div class="head-actions">
          <span class="time">最近刷新：{{ noticeStore.lastRefreshTime || '刚刚' }}</span>
          <el-button type="primary" :icon="Refresh" :loading="noticeStore.loading" @click="refreshData">刷新</el-button>
        </div>
      </div>
    </el-card>

    <el-row :gutter="16">
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="stat-card warning">
          <div class="label">借用待审批</div>
          <div class="value">{{ noticeStore.summary.pendingBorrow || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="stat-card danger">
          <div class="label">逾期借用</div>
          <div class="value">{{ noticeStore.summary.overdueBorrow || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="stat-card info">
          <div class="label">待受理报修</div>
          <div class="value">{{ noticeStore.summary.pendingRepair || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="stat-card success">
          <div class="label">处理中报修</div>
          <div class="value">{{ noticeStore.summary.processingRepair || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="list-card">
      <template #header>
        <div class="list-head">
          <span>最近通知</span>
          <el-switch v-model="attentionOnly" active-text="仅看重要" inactive-text="全部" />
        </div>
      </template>

      <el-empty v-if="filteredList.length === 0" description="暂无通知" />
      <el-timeline v-else>
        <el-timeline-item
          v-for="item in filteredList"
          :key="item.id"
          :timestamp="item.time"
          :type="timelineType(item.level)"
          placement="top"
        >
          <div class="notice-item">
            <div>
              <div class="item-title">
                {{ item.title }}
                <el-tag size="small" :type="tagType(item.level)">{{ item.statusLabel }}</el-tag>
              </div>
              <p class="item-desc">{{ item.content }}</p>
            </div>
            <el-button v-if="item.route" link type="primary" @click="go(item.route)">{{ item.actionText || "立即处理" }}</el-button>
            <el-button v-else link type="primary" @click="showDetail(item)">查看详情</el-button>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <el-dialog v-model="detailOpen" width="640px" title="消息详情" append-to-body>
      <div class="detail-wrap">
        <h4 class="detail-title">{{ detailItem.title || "系统消息" }}</h4>
        <div class="detail-meta">
          <el-tag size="small" :type="tagType(detailItem.level)">{{ detailItem.statusLabel || "通知" }}</el-tag>
          <span>{{ detailItem.time || "--" }}</span>
        </div>
        <div class="detail-content" v-html="detailHtml"></div>
      </div>
      <template #footer>
        <el-button type="primary" @click="detailOpen = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { Refresh } from "@element-plus/icons-vue";
import { useNoticeStore } from "@/store/modules/notice";

const router = useRouter();
const noticeStore = useNoticeStore();
const attentionOnly = ref(true);
const detailOpen = ref(false);
const detailItem = ref({});

const filteredList = computed(() => {
  const list = noticeStore.recentList || [];
  if (!attentionOnly.value) return list;
  return list.filter((item) => item.level === "warning" || item.level === "danger");
});

const timelineType = (level) => {
  if (level === "danger") return "danger";
  if (level === "warning") return "warning";
  return "primary";
};

const tagType = (level) => {
  if (level === "danger") return "danger";
  if (level === "warning") return "warning";
  return "info";
};

const go = (path) => {
  router.push(path || "/home");
};

const detailHtml = computed(() => {
  const raw = String(detailItem.value.rawContent || "").trim();
  if (raw) {
    return raw;
  }
  return `<p>${detailItem.value.content || "暂无内容"}</p>`;
});

const showDetail = (item) => {
  detailItem.value = item || {};
  detailOpen.value = true;
};

const refreshData = async () => {
  await noticeStore.refreshAll();
};

onMounted(refreshData);
</script>

<style scoped>
.notice-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.head-card,
.list-card,
.stat-card {
  border-radius: 14px;
}

.head-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.head-row h3 {
  margin: 0;
  font-size: 22px;
}

.head-row p {
  margin: 8px 0 0;
  color: #7388a3;
}

.head-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.time {
  color: #7388a3;
  font-size: 12px;
}

.stat-card .label {
  color: #6f8098;
  font-size: 13px;
}

.stat-card .value {
  margin-top: 8px;
  font-size: 30px;
  font-weight: 700;
  color: #274261;
}

.warning .value {
  color: #d9901e;
}

.danger .value {
  color: #d74a4a;
}

.info .value {
  color: #2f83d1;
}

.success .value {
  color: #22a16c;
}

.list-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notice-item {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

.item-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

.item-desc {
  margin: 6px 0 0;
  color: #6f8098;
}

.detail-wrap {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.detail-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #2a3f5c;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: #7f8ea3;
}

.detail-content {
  line-height: 1.75;
  color: #35485f;
  word-break: break-word;
}

.detail-content :deep(p) {
  margin: 0 0 8px;
}
</style>
