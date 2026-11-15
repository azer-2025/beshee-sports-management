<template>
  <div class="dashboard-container">
    <!-- Welcome Banner -->
    <div class="welcome-banner">
      <div class="welcome-content">
        <h1>欢迎回来，{{ userStore.nickName }}</h1>
        <p>今天是 {{ currentDate }}，系统运行状态良好，继续保持运动精神！</p>
      </div>
      <div class="banner-illustration">
        <el-icon :size="80" color="rgba(255,255,255,0.2)"><Bicycle /></el-icon>
      </div>
    </div>

    <!-- Top Statistics Card Row -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-item primary">
          <div class="stat-icon"><el-icon><Box /></el-icon></div>
          <div class="stat-info">
            <div class="label">器材总数</div>
            <div class="count">{{ summary.totalAssets || 0 }}</div>
          </div>
          <div class="stat-trend">分类：{{ summary.totalCategories || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-item success">
          <div class="stat-icon"><el-icon><Connection /></el-icon></div>
          <div class="stat-info">
            <div class="label">借阅记录</div>
            <div class="count">{{ summary.totalBorrowRecords || 0 }}</div>
          </div>
          <div class="stat-trend">当前逾期：{{ summary.overdueBorrows || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-item warning">
          <div class="stat-icon"><el-icon><Stamp /></el-icon></div>
          <div class="stat-info">
            <div class="label">待审批借还</div>
            <div class="count">{{ summary.pendingBorrowApprovals || 0 }}</div>
          </div>
          <div class="stat-trend">点击前往处理</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-item danger">
          <div class="stat-icon"><el-icon><Tools /></el-icon></div>
          <div class="stat-info">
            <div class="label">维修中器材</div>
            <div class="count">{{ summary.repairingAssets || 0 }}</div>
          </div>
          <div class="stat-trend">保障器材完好</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Main Content Area -->
    <el-row :gutter="20" class="main-row">
      <!-- Left: Monthly Trend -->
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>月度借阅业务趋势</span>
              <el-tag size="small">近12个月</el-tag>
            </div>
          </template>
          <div ref="trendRef" class="chart-box main-chart"></div>
        </el-card>
      </el-col>

      <!-- Right: Quick Navigation -->
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="quick-nav-card">
          <template #header>快速导航</template>
          <div class="quick-nav-grid">
            <div class="nav-item" @click="router.push('/sports/asset')">
              <div class="nav-icon blue"><el-icon><Management /></el-icon></div>
              <span>器材台账</span>
            </div>
            <div class="nav-item" @click="router.push('/sports/borrow')">
              <div class="nav-icon green"><el-icon><Check /></el-icon></div>
              <span>借还审批</span>
            </div>
            <div class="nav-item" @click="router.push('/sports/repair')">
              <div class="nav-icon orange"><el-icon><Setting /></el-icon></div>
              <span>报修管理</span>
            </div>
            <div class="nav-item" @click="router.push('/sports/stat')">
              <div class="nav-icon purple"><el-icon><Histogram /></el-icon></div>
              <span>数据分析</span>
            </div>
            <div class="nav-item" @click="router.push('/system/user')">
              <div class="nav-icon cyan"><el-icon><User /></el-icon></div>
              <span>用户管理</span>
            </div>
            <div class="nav-item" @click="router.push('/system/notice')">
              <div class="nav-icon red"><el-icon><Bell /></el-icon></div>
              <span>通知发布</span>
            </div>
          </div>
        </el-card>

        <el-card shadow="hover" class="system-info-card mt20">
          <template #header>系统版本</template>
          <div class="sys-info">
            <div class="info-row">
              <span class="label">当前版本</span>
              <el-tag size="small">v{{ version }}</el-tag>
            </div>
            <div class="info-row">
              <span class="label">核心框架</span>
              <span>Vue 3 / Element Plus</span>
            </div>
            <div class="info-row">
              <span class="label">后端服务</span>
              <span>Spring Boot / RuoYi</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Bottom Charts Area -->
    <el-row :gutter="20" class="bottom-row">
      <el-col :xs="24" :md="12">
        <el-card shadow="hover">
          <template #header>器材使用率排行（Top 10）</template>
          <div ref="usageRef" class="chart-box secondary-chart"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="hover">
          <template #header>器材损耗原因占比</template>
          <div ref="reasonRef" class="chart-box secondary-chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import { onMounted, onBeforeUnmount, ref, nextTick, getCurrentInstance } from 'vue';
import { useRouter } from 'vue-router';
import useUserStore from '@/store/modules/user';
import * as echarts from 'echarts';
import { getStatSummary, getUsageRank, getDamageReason, getMonthlyTrend } from "@/api/sports/stat";

const version = ref('3.9.1');
const router = useRouter();
const userStore = useUserStore();
const { proxy } = getCurrentInstance();

const currentDate = ref(new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' }));
const summary = ref({});
const usageRank = ref([]);
const reasonList = ref([]);
const trendList = ref([]);

const usageRef = ref();
const reasonRef = ref();
const trendRef = ref();
let usageChart;
let reasonChart;
let trendChart;

function loadDashboardData() {
  getStatSummary().then((res) => {
    summary.value = res.data || {};
  });

  getUsageRank().then((res) => {
    usageRank.value = res.data || [];
    renderUsage();
  });

  getDamageReason().then((res) => {
    reasonList.value = res.data || [];
    renderReason();
  });

  getMonthlyTrend().then((res) => {
    trendList.value = res.data || [];
    renderTrend();
  });
}

function renderUsage() {
  nextTick(() => {
    if (!usageRef.value) return;
    usageChart = usageChart || echarts.init(usageRef.value);
    usageChart.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'value' },
      yAxis: { type: 'category', data: usageRank.value.map(item => item.assetName).reverse() },
      series: [
        {
          name: '使用次数',
          type: 'bar',
          data: usageRank.value.map(item => item.useCount).reverse(),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          },
          barWidth: '60%'
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
      tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
      legend: { orient: 'vertical', left: 'left', top: 'center' },
      series: [
        {
          name: '损耗原因',
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
          label: { show: false, position: 'center' },
          emphasis: { label: { show: true, fontSize: '18', fontWeight: 'bold' } },
          labelLine: { show: false },
          data: reasonList.value.map(item => ({
            name: item.reasonLabel || '其他',
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
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', boundaryGap: false, data: trendList.value.map(item => item.month) },
      yAxis: { type: 'value' },
      series: [
        {
          name: '借阅量',
          type: 'line',
          smooth: true,
          showSymbol: false,
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(128, 255, 165)' },
              { offset: 1, color: 'rgba(1, 191, 236)' }
            ])
          },
          emphasis: { focus: 'series' },
          data: trendList.value.map(item => item.borrowCount),
          lineStyle: { width: 0 }
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
  loadDashboardData();
  window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
  usageChart && usageChart.dispose();
  reasonChart && reasonChart.dispose();
  trendChart && trendChart.dispose();
});
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);

  .welcome-banner {
    background: linear-gradient(135deg, #409eff 0%, #007aff 100%);
    padding: 32px 40px;
    border-radius: 12px;
    color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    box-shadow: 0 4px 12px rgba(0, 122, 255, 0.2);

    h1 {
      margin: 0 0 10px 0;
      font-size: 28px;
      font-weight: 700;
    }

    p {
      margin: 0;
      opacity: 0.9;
      font-size: 16px;
    }
  }

  .stat-row {
    margin-bottom: 24px;

    .stat-item {
      height: 120px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      position: relative;
      overflow: hidden;
      border: none;
      
      :deep(.el-card__body) {
        padding: 20px;
        display: grid;
        grid-template-columns: 60px 1fr;
        grid-template-rows: 1fr auto;
        gap: 0 15px;
        height: 100%;
      }

      .stat-icon {
        grid-row: span 2;
        width: 56px;
        height: 56px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28px;
        align-self: center;
      }

      .stat-info {
        .label {
          color: #909399;
          font-size: 14px;
          margin-bottom: 4px;
        }
        .count {
          font-size: 26px;
          font-weight: 700;
          color: #303133;
        }
      }

      .stat-trend {
        grid-column: 2;
        font-size: 12px;
        margin-top: 5px;
        color: #909399;
      }

      &.primary .stat-icon { background-color: rgba(64, 158, 255, 0.1); color: #409eff; }
      &.success .stat-icon { background-color: rgba(103, 194, 58, 0.1); color: #67c23a; }
      &.warning .stat-icon { background-color: rgba(230, 162, 60, 0.1); color: #e6a23c; }
      &.danger .stat-icon { background-color: rgba(245, 108, 108, 0.1); color: #f56c6c; }
    }
  }

  .main-row {
    margin-bottom: 24px;
  }

  .chart-card {
    height: 100%;
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .quick-nav-card {
    .quick-nav-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 15px;
      padding: 10px 0;
    }

    .nav-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      transition: all 0.2s;

      &:hover {
        transform: translateY(-2px);
        .nav-icon {
          box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
      }

      .nav-icon {
        width: 48px;
        height: 48px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 22px;
        color: white;

        &.blue { background-color: #409eff; }
        &.green { background-color: #67c23a; }
        &.orange { background-color: #e6a23c; }
        &.purple { background-color: #722ed1; }
        &.cyan { background-color: #13c2c2; }
        &.red { background-color: #f5222d; }
      }

      span {
        font-size: 13px;
        color: #606266;
      }
    }
  }

  .system-info-card {
    .sys-info {
      .info-row {
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        border-bottom: 1px solid #f0f0f0;
        font-size: 14px;
        &:last-child { border-bottom: none; }
        .label { color: #909399; }
      }
    }
  }

  .chart-box {
    width: 100%;
    &.main-chart { height: 360px; }
    &.secondary-chart { height: 300px; }
  }

  .mt20 { margin-top: 20px; }
}
</style>
