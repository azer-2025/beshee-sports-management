<template>
  <div class="app-container">
    <el-alert
      v-if="Number(summary.totalAssets || 0) === 0"
      title="当前无统计样本数据，请先新增器材并产生借还/报修记录。"
      type="info"
      :closable="false"
      show-icon
      class="mb12"
    />

    <el-row :gutter="12" class="mb12">
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card">
          <div class="label">器材总量</div>
          <div class="value">{{ summary.totalAssets || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card">
          <div class="label">分类数量</div>
          <div class="value">{{ summary.totalCategories || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card">
          <div class="label">借阅记录</div>
          <div class="value">{{ summary.totalBorrowRecords || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card">
          <div class="label">待审批</div>
          <div class="value warning">{{ summary.pendingBorrowApprovals || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card">
          <div class="label">逾期中</div>
          <div class="value danger">{{ summary.overdueBorrows || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card">
          <div class="label">维修中</div>
          <div class="value">{{ summary.repairingAssets || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12">
      <el-col :xs="24" :md="12">
        <el-card>
          <template #header>器材使用率排行（Top10）</template>
          <div ref="usageRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card>
          <template #header>损耗原因分析</template>
          <div ref="reasonRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12" class="mt12">
      <el-col :span="24">
        <el-card>
          <template #header>月度借阅走势（近12个月）</template>
          <div ref="trendRef" class="chart-box wide"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12" class="mt12">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>采购/报废建议报告</span>
              <div class="header-actions">
                <el-date-picker
                  v-model="statMonth"
                  type="month"
                  value-format="YYYY-MM"
                  placeholder="选择统计月份"
                  style="width: 140px"
                />
                <el-select
                  v-model="suggestionQuery.suggestionType"
                  placeholder="建议类型"
                  clearable
                  style="width: 120px"
                >
                  <el-option label="建议报废" value="建议报废" />
                  <el-option label="建议采购" value="建议采购" />
                  <el-option label="持续观察" value="持续观察" />
                </el-select>
                <el-input
                  v-model="suggestionQuery.keyword"
                  placeholder="器材名/编码/分类"
                  clearable
                  style="width: 170px"
                  @keyup.enter="loadSuggestion"
                />
                <el-button type="primary" icon="Search" @click="loadSuggestion">查询</el-button>
                <el-button icon="Refresh" @click="resetSuggestionQuery">重置</el-button>
                <el-button
                  type="success"
                  icon="Download"
                  @click="handleExport"
                  v-hasPermi="['sports:stat:export']"
                >下载报告</el-button>
              </div>
            </div>
          </template>

          <div class="suggestion-summary">
            <el-tag type="danger">建议报废 {{ suggestionSummary.scrap }}</el-tag>
            <el-tag type="warning">建议采购 {{ suggestionSummary.purchase }}</el-tag>
            <el-tag>持续观察 {{ suggestionSummary.observe }}</el-tag>
            <el-tag type="info">总计 {{ suggestionSummary.total }}</el-tag>
          </div>

          <el-table :data="suggestionList" max-height="420" empty-text="暂无建议报告数据">
            <el-table-column label="统计月份" prop="statMonth" width="96" />
            <el-table-column label="器材ID" prop="assetId" width="88" />
            <el-table-column label="器材名称" prop="assetName" min-width="150" />
            <el-table-column label="分类" prop="categoryName" min-width="120" />
            <el-table-column label="总库存" prop="stockQty" width="88" />
            <el-table-column label="可用库存" prop="availableQty" width="88" />
            <el-table-column label="月借阅量" prop="monthBorrowCount" width="90" />
            <el-table-column label="月报修量" prop="monthRepairCount" width="90" />
            <el-table-column label="累计报修" prop="totalRepairCount" width="90" />
            <el-table-column label="已用年限" prop="usedYears" width="90" />
            <el-table-column label="累计折旧率(%)" prop="accumulatedDepRate" width="110" />
            <el-table-column label="建议" prop="suggestion" width="100">
              <template #default="scope">
                <el-tag v-if="scope.row.suggestion === '建议报废'" type="danger">{{ scope.row.suggestion }}</el-tag>
                <el-tag v-else-if="scope.row.suggestion === '建议采购'" type="warning">{{ scope.row.suggestion }}</el-tag>
                <el-tag v-else>{{ scope.row.suggestion }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="建议依据" prop="suggestionReason" min-width="180" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="SportsStat">
import { computed, getCurrentInstance, nextTick, onBeforeUnmount, onMounted, reactive, ref } from "vue";
import * as echarts from "echarts";
import { getStatSummary, getUsageRank, getDamageReason, getMonthlyTrend, getSuggestionReport } from "@/api/sports/stat";

const { proxy } = getCurrentInstance();

const summary = ref({});
const usageRank = ref([]);
const reasonList = ref([]);
const trendList = ref([]);
const suggestionList = ref([]);
const statMonth = ref(formatMonth(new Date()));
const suggestionQuery = reactive({
  suggestionType: undefined,
  keyword: undefined
});

const usageRef = ref();
const reasonRef = ref();
const trendRef = ref();
let usageChart;
let reasonChart;
let trendChart;

function formatMonth(date) {
  const year = date.getFullYear();
  const month = `${date.getMonth() + 1}`.padStart(2, "0");
  return `${year}-${month}`;
}

function loadBaseData() {
  getStatSummary().then((res) => {
    summary.value = res.data || {};
  }).catch(() => {
    summary.value = {};
  });

  getUsageRank().then((res) => {
    usageRank.value = res.data || [];
    renderUsage();
  }).catch(() => {
    usageRank.value = [];
    renderUsage();
  });

  getDamageReason().then((res) => {
    reasonList.value = res.data || [];
    renderReason();
  }).catch(() => {
    reasonList.value = [];
    renderReason();
  });

  getMonthlyTrend().then((res) => {
    trendList.value = res.data || [];
    renderTrend();
  }).catch(() => {
    trendList.value = [];
    renderTrend();
  });
}

function loadSuggestion() {
  getSuggestionReport({
    statMonth: statMonth.value,
    suggestionType: suggestionQuery.suggestionType,
    keyword: suggestionQuery.keyword
  }).then((res) => {
    suggestionList.value = res.data || [];
  }).catch(() => {
    suggestionList.value = [];
  });
}

function handleExport() {
  proxy.download(
    "sports/stat/suggestion/export",
    {
      statMonth: statMonth.value,
      suggestionType: suggestionQuery.suggestionType,
      keyword: suggestionQuery.keyword
    },
    `采购报废建议_${statMonth.value}.xlsx`
  );
}

const suggestionSummary = computed(() => {
  const result = { scrap: 0, purchase: 0, observe: 0, total: suggestionList.value.length };
  suggestionList.value.forEach((item) => {
    if (item.suggestion === "建议报废") {
      result.scrap += 1;
    } else if (item.suggestion === "建议采购") {
      result.purchase += 1;
    } else {
      result.observe += 1;
    }
  });
  return result;
});

function resetSuggestionQuery() {
  suggestionQuery.suggestionType = undefined;
  suggestionQuery.keyword = undefined;
  loadSuggestion();
}

function renderUsage() {
  nextTick(() => {
    if (!usageRef.value) return;
    usageChart = usageChart || echarts.init(usageRef.value);
    usageChart.setOption({
      tooltip: { trigger: "axis" },
      grid: { left: 36, right: 16, top: 20, bottom: 36, containLabel: true },
      xAxis: { type: "category", data: usageRank.value.map((item) => item.assetName) },
      yAxis: { type: "value", name: "次数" },
      series: [
        {
          type: "bar",
          data: usageRank.value.map((item) => item.useCount),
          itemStyle: { color: "#409EFF" }
        }
      ]
    });
  });
}

function renderReason() {
  nextTick(() => {
    if (!reasonRef.value) return;
    reasonChart = reasonChart || echarts.init(reasonRef.value);
    reasonChart.setOption({
      tooltip: { trigger: "item" },
      legend: { bottom: 0 },
      series: [
        {
          type: "pie",
          radius: ["40%", "70%"],
          data: reasonList.value.map((item) => ({
            name: item.reasonLabel || "未标注",
            value: item.total
          }))
        }
      ]
    });
  });
}

function renderTrend() {
  nextTick(() => {
    if (!trendRef.value) return;
    trendChart = trendChart || echarts.init(trendRef.value);
    trendChart.setOption({
      tooltip: { trigger: "axis" },
      grid: { left: 36, right: 16, top: 24, bottom: 36, containLabel: true },
      xAxis: { type: "category", data: trendList.value.map((item) => item.month) },
      yAxis: { type: "value", name: "借阅量" },
      series: [
        {
          type: "line",
          smooth: true,
          areaStyle: {},
          data: trendList.value.map((item) => item.borrowCount),
          itemStyle: { color: "#67C23A" }
        }
      ]
    });
  });
}

function handleResize() {
  usageChart && usageChart.resize();
  reasonChart && reasonChart.resize();
  trendChart && trendChart.resize();
}

onMounted(() => {
  loadBaseData();
  loadSuggestion();
  window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize);
  usageChart && usageChart.dispose();
  reasonChart && reasonChart.dispose();
  trendChart && trendChart.dispose();
});
</script>

<style scoped>
.stat-card .label {
  color: #909399;
  font-size: 13px;
}

.stat-card .value {
  margin-top: 8px;
  font-size: 26px;
  font-weight: 700;
  color: #303133;
}

.stat-card .value.warning {
  color: #e6a23c;
}

.stat-card .value.danger {
  color: #f56c6c;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.suggestion-summary {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.chart-box {
  width: 100%;
  height: 320px;
}

.chart-box.wide {
  height: 340px;
}

.mt12 {
  margin-top: 12px;
}

.mb12 {
  margin-bottom: 12px;
}
</style>
