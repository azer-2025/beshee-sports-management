<template>
  <div class="home-page">
    <!-- Hero/Search Section -->
    <div class="search-section">
      <el-card shadow="never" class="query-card">
        <el-form :inline="true" :model="queryParams" class="custom-form">
          <el-form-item label="关键词">
            <el-input
              v-model="queryParams.keyword"
              placeholder="器材名/编码"
              :prefix-icon="Search"
              clearable
              @keyup.enter="handleQuery"
              class="search-input"
            />
          </el-form-item>
          <el-form-item label="类别">
            <el-select v-model="queryParams.classType" clearable placeholder="全部" class="filter-select">
              <el-option label="球类" value="1" />
              <el-option label="器械类" value="2" />
              <el-option label="场地辅助类" value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="queryParams.categoryId" clearable placeholder="全部" class="filter-select">
              <el-option
                v-for="item in categoryOptions"
                :key="item.categoryId"
                :label="item.categoryName"
                :value="item.categoryId"
              />
            </el-select>
          </el-form-item>
          <el-form-item class="action-items">
            <el-button type="primary" class="btn-search" :icon="Search" @click="handleQuery">搜索</el-button>
            <el-button class="btn-reset" :icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- Content Section -->
    <div class="content-section">
      <div class="list-head">
        <div class="title-wrapper">
          <div class="title-indicator"></div>
          <h3>可预约器材</h3>
        </div>
        <span class="total-count">共 <strong>{{ total }}</strong> 件</span>
      </div>

      <el-row :gutter="24" v-loading="loading" class="card-grid">
        <el-col v-for="item in equipmentList" :key="item.assetId" :xs="24" :sm="12" :md="8" :lg="6" :xl="6" class="card-col">
          <div class="modern-card">
            <div class="card-image-wrapper">
              <el-image :src="toImage(item.coverImage)" fit="cover" class="asset-image" lazy>
                <template #error>
                  <div class="img-empty">
                    <el-icon class="empty-icon"><Picture /></el-icon>
                    <span>暂无图片</span>
                  </div>
                </template>
              </el-image>
              <div class="status-badge" :class="(item.availableQty || 0) > 0 ? 'status-avail' : 'status-empty'">
                {{ (item.availableQty || 0) > 0 ? '可预约' : '库存不足' }}
              </div>
            </div>

            <div class="card-content">
              <h4 class="asset-title" :title="item.assetName">{{ item.assetName }}</h4>
              
              <div class="asset-info">
                <div class="info-row">
                  <span class="info-label">编码</span>
                  <span class="info-value">{{ item.assetCode || '-' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">分类</span>
                  <el-tag size="small" effect="light" class="category-tag">{{ item.categoryName || '-' }}</el-tag>
                </div>
              </div>

              <div class="stock-progress">
                <div class="stock-labels">
                  <span class="stock-title">库存状况</span>
                  <span class="stock-numbers">
                    <strong :class="{'text-danger': (item.availableQty || 0) <= 0}">{{ item.availableQty || 0 }}</strong> / {{ item.stockQty || 0 }}
                  </span>
                </div>
                <el-progress 
                  :percentage="getPercentage(item.availableQty || 0, item.stockQty || 0)" 
                  :show-text="false"
                  :color="(item.availableQty || 0) > 0 ? '#409eff' : '#f56c6c'"
                  :stroke-width="6"
                />
              </div>

              <div class="card-actions">
                <el-button 
                  type="primary" 
                  class="btn-borrow" 
                  :disabled="(item.availableQty || 0) <= 0" 
                  @click="openBorrow(item)"
                >
                  <el-icon><ShoppingCart /></el-icon>
                  <span>立即预约</span>
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-empty v-if="!loading && equipmentList.length === 0" description="没有找到相关的可预约器材" class="modern-empty" />

      <div class="pager-wrap" v-if="total > 0">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[8, 16, 24, 32]"
          :total="total"
          :page-size="queryParams.pageSize"
          :current-page="queryParams.pageNum"
          @size-change="onSizeChange"
          @current-change="onPageChange"
          class="modern-pagination"
        />
      </div>
    </div>

    <!-- Borrow Dialog -->
    <el-dialog v-model="borrowOpen" title="提交借用预约" width="500px" class="modern-dialog" destroy-on-close align-center>
      <el-form ref="borrowRef" :model="borrowForm" :rules="rules" label-width="96px">
        <el-form-item label="器材名称">
          <el-input v-model="borrowForm.assetName" disabled />
        </el-form-item>
        <el-form-item label="借用数量" prop="quantity">
          <el-input-number v-model="borrowForm.quantity" :min="1" :max="borrowForm.maxQty || 1" />
        </el-form-item>
        <el-form-item label="开始时间" prop="reserveStartTime">
          <el-date-picker
            v-model="borrowForm.reserveStartTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="reserveEndTime">
          <el-date-picker
            v-model="borrowForm.reserveEndTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="borrowForm.remark" type="textarea" :rows="3" maxlength="200" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="borrowOpen = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="submitBorrow">确认预约</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { Search, Refresh, ShoppingCart, Picture } from "@element-plus/icons-vue";
import { listFrontCategory, listFrontEquipment } from "@/api/equipment";
import { applyBorrow } from "@/api/borrow";
import { useNoticeStore } from "@/store/modules/notice";

const noticeStore = useNoticeStore();

const loading = ref(false);
const total = ref(0);
const equipmentList = ref([]);
const categoryOptions = ref([]);

const queryParams = reactive({
  pageNum: 1,
  pageSize: 8,
  keyword: "",
  classType: "",
  categoryId: undefined
});

const borrowOpen = ref(false);
const borrowRef = ref();
const submitLoading = ref(false);
const borrowForm = reactive({
  assetId: undefined,
  assetName: "",
  maxQty: 1,
  quantity: 1,
  reserveStartTime: "",
  reserveEndTime: "",
  remark: ""
});

const rules = {
  quantity: [{ required: true, message: "请输入数量", trigger: "change" }],
  reserveStartTime: [{ required: true, message: "请选择开始时间", trigger: "change" }],
  reserveEndTime: [{ required: true, message: "请选择结束时间", trigger: "change" }]
};

const toImage = (src) => {
  if (!src) return "";
  if (src.startsWith("http://") || src.startsWith("https://")) return src;
  return `${import.meta.env.VITE_APP_BASE_API}${src}`;
};

const getPercentage = (avail, total) => {
  if (!total) return 0;
  return Math.min(100, Math.max(0, (avail / total) * 100));
};

const getCategoryList = async () => {
  const res = await listFrontCategory();
  categoryOptions.value = res.data || [];
};

const getList = async () => {
  loading.value = true;
  try {
    const res = await listFrontEquipment(queryParams);
    equipmentList.value = res.rows || [];
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
  queryParams.keyword = "";
  queryParams.classType = "";
  queryParams.categoryId = undefined;
  handleQuery();
};

const onSizeChange = (size) => {
  queryParams.pageSize = size;
  queryParams.pageNum = 1;
  getList();
};

const onPageChange = (page) => {
  queryParams.pageNum = page;
  getList();
};

const openBorrow = (item) => {
  borrowForm.assetId = item.assetId;
  borrowForm.assetName = item.assetName;
  borrowForm.maxQty = item.availableQty || 1;
  borrowForm.quantity = 1;
  borrowForm.reserveStartTime = "";
  borrowForm.reserveEndTime = "";
  borrowForm.remark = "";
  borrowOpen.value = true;
};

const submitBorrow = () => {
  borrowRef.value.validate(async (valid) => {
    if (!valid) return;
    if (borrowForm.reserveEndTime <= borrowForm.reserveStartTime) {
      ElMessage.warning("结束时间必须晚于开始时间");
      return;
    }
    submitLoading.value = true;
    try {
      await applyBorrow({
        assetId: borrowForm.assetId,
        quantity: borrowForm.quantity,
        reserveStartTime: borrowForm.reserveStartTime,
        reserveEndTime: borrowForm.reserveEndTime,
        expectedReturnTime: borrowForm.reserveEndTime,
        remark: borrowForm.remark
      });
      ElMessage.success("预约提交成功，请等待审批");
      borrowOpen.value = false;
      await noticeStore.refreshSummary();
      getList();
    } finally {
      submitLoading.value = false;
    }
  });
};

onMounted(async () => {
  await Promise.all([getCategoryList(), getList()]);
});
</script>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
  min-height: 100%;
}

/* Search Section */
.search-section {
  position: relative;
  z-index: 10;
}

.query-card {
  border: none !important;
  border-radius: 16px;
  background: #ffffff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);
  padding: 12px 8px 0;
}

.custom-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: flex-start;
}

.custom-form .el-form-item {
  margin-right: 0;
}

.search-input {
  width: 240px;
}

.filter-select {
  width: 140px;
}

.action-items {
  margin-left: auto;
}

.btn-search {
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.btn-reset {
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
}

/* Content Section */
.content-section {
  background: transparent;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.list-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 0 8px;
}

.title-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-indicator {
  width: 6px;
  height: 24px;
  background: linear-gradient(135deg, #409eff, #53a8ff);
  border-radius: 4px;
}

.list-head h3 {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #1d2129;
  letter-spacing: 0.5px;
}

.total-count {
  color: #86909c;
  font-size: 14px;
}

.total-count strong {
  color: #409eff;
  font-size: 16px;
  margin: 0 4px;
}

/* Modern Card */
.card-grid {
  margin-bottom: 24px;
}

.card-col {
  margin-bottom: 24px;
}

.modern-card {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  border: 1px solid #f2f3f5;
}

.modern-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.08);
  border-color: #e5e6eb;
}

.card-image-wrapper {
  position: relative;
  height: 200px;
  overflow: hidden;
  background: #f7f8fa;
}

.asset-image {
  width: 100%;
  height: 100%;
  transition: transform 0.5s ease;
}

.modern-card:hover .asset-image {
  transform: scale(1.05);
}

.img-empty {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #c9cdd4;
  background: linear-gradient(135deg, #f2f3f5 0%, #e5e6eb 100%);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 8px;
  opacity: 0.7;
}

.status-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  backdrop-filter: blur(4px);
  z-index: 2;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.status-avail {
  background: rgba(103, 194, 58, 0.9);
  color: #fff;
}

.status-empty {
  background: rgba(245, 108, 108, 0.9);
  color: #fff;
}

.card-content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.asset-title {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 600;
  color: #1d2129;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.asset-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 16px;
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 13px;
}

.info-label {
  color: #86909c;
}

.info-value {
  color: #4e5969;
  font-weight: 500;
}

.category-tag {
  border-radius: 6px;
  border: none;
  background: #f2f3f5;
  color: #4e5969;
}

.stock-progress {
  margin-top: auto;
  margin-bottom: 20px;
  padding-top: 16px;
  border-top: 1px dashed #e5e6eb;
}

.stock-labels {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 13px;
}

.stock-title {
  color: #86909c;
}

.stock-numbers {
  color: #4e5969;
}

.stock-numbers strong {
  font-size: 15px;
  font-weight: 600;
}

.text-danger {
  color: #f56c6c !important;
}

.card-actions {
  margin-top: auto;
}

.btn-borrow {
  width: 100%;
  border-radius: 8px;
  height: 40px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}

.modern-card:hover .btn-borrow:not(.is-disabled) {
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  transform: translateY(-2px);
}

/* Empty State */
.modern-empty {
  padding: 60px 0;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.02);
}

/* Pagination */
.pager-wrap {
  display: flex;
  justify-content: center;
  padding: 20px 0;
  margin-top: auto;
}

.modern-pagination {
  --el-pagination-bg-color: #fff;
  --el-pagination-hover-color: #409eff;
}

/* Dialog overrides */
:deep(.modern-dialog) {
  border-radius: 16px !important;
  overflow: hidden;
}

:deep(.modern-dialog .el-dialog__header) {
  margin: 0;
  padding: 20px 24px;
  border-bottom: 1px solid #f2f3f5;
  background: #fafafa;
}

:deep(.modern-dialog .el-dialog__title) {
  font-weight: 600;
  font-size: 18px;
  color: #1d2129;
}

:deep(.modern-dialog .el-dialog__body) {
  padding: 24px;
}

:deep(.modern-dialog .el-dialog__footer) {
  padding: 16px 24px;
  border-top: 1px solid #f2f3f5;
  background: #fafafa;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
