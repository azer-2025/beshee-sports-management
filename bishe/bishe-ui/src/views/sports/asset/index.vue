<template>
  <div class="app-container">
    <el-alert
      v-if="!loading && total === 0"
      title="暂无器材台账数据，请先初始化示例数据或新增器材。"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 12px"
    />
    <el-form ref="queryRef" :model="queryParams" :inline="true" v-show="showSearch">
      <el-form-item label="器材名称" prop="assetName">
        <el-input v-model="queryParams.assetName" placeholder="请输入器材名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="器材大类" prop="classType">
        <el-select v-model="queryParams.classType" placeholder="请选择器材大类" clearable style="width: 160px">
          <el-option v-for="dict in sp_equipment_class" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="auditStatus">
        <el-select v-model="queryParams.auditStatus" placeholder="请选择审核状态" clearable style="width: 160px">
          <el-option v-for="dict in sp_asset_audit_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="器材状态" prop="assetStatus">
        <el-select v-model="queryParams.assetStatus" placeholder="请选择器材状态" clearable style="width: 160px">
          <el-option v-for="dict in sp_asset_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['sports:asset:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['sports:asset:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Remove" :disabled="multiple" @click="handleBatchOffline" v-hasPermi="['sports:asset:offline']">批量下架</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['sports:asset:remove']">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="assetList"
      empty-text="暂无器材台账数据"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="图片" width="90">
        <template #default="scope">
          <image-preview :src="scope.row.coverImage" :width="48" :height="48" />
        </template>
      </el-table-column>
      <el-table-column label="名称" prop="assetName" min-width="140" />
      <el-table-column label="编码" prop="assetCode" min-width="120" />
      <el-table-column label="分类" prop="categoryName" min-width="100" />
      <el-table-column label="库存" min-width="100">
        <template #default="scope">
          <span>{{ scope.row.availableQty }}/{{ scope.row.stockQty }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核" width="100">
        <template #default="scope">
          <dict-tag :options="sp_asset_audit_status" :value="scope.row.auditStatus" />
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <dict-tag :options="sp_asset_status" :value="scope.row.assetStatus" />
        </template>
      </el-table-column>
      <el-table-column label="位置" prop="location" min-width="120" show-overflow-tooltip />
      <el-table-column label="采购日期" width="120">
        <template #default="scope">
          <span>{{ parseTime(scope.row.purchaseDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['sports:asset:edit']">修改</el-button>
          <el-button
            v-if="scope.row.auditStatus === '0'"
            link
            type="success"
            icon="Check"
            @click="handleAudit(scope.row, '1')"
            v-hasPermi="['sports:asset:audit']"
          >通过</el-button>
          <el-button
            v-if="scope.row.auditStatus === '0'"
            link
            type="warning"
            icon="Close"
            @click="handleAudit(scope.row, '2')"
            v-hasPermi="['sports:asset:audit']"
          >驳回</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['sports:asset:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" v-model="open" width="780px" append-to-body>
      <el-form ref="assetRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="器材名称" prop="assetName">
              <el-input v-model="form.assetName" placeholder="请输入器材名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="器材编码" prop="assetCode">
              <el-input v-model="form.assetCode" placeholder="请输入器材编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类">
                <el-option v-for="item in categoryOptions" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="条码" prop="barcode">
              <el-input v-model="form.barcode" placeholder="请输入条码（支持条码绑定）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-input v-model="form.brand" placeholder="请输入品牌" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购日期" prop="purchaseDate">
              <el-date-picker v-model="form.purchaseDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择采购日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购价" prop="purchasePrice">
              <el-input-number v-model="form.purchasePrice" :precision="2" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年折旧率(%)" prop="depreciationRate">
              <el-input-number v-model="form.depreciationRate" :precision="2" :min="0" :max="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总库存" prop="stockQty">
              <el-input-number v-model="form.stockQty" :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="可用库存" prop="availableQty">
              <el-input-number v-model="form.availableQty" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用年限" prop="serviceLifeYear">
              <el-input-number v-model="form.serviceLifeYear" :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="存放位置" prop="location">
              <el-input v-model="form.location" placeholder="请输入存放位置" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="器材图片" prop="coverImage">
              <image-upload v-model="form.coverImage" :limit="1" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="3" maxlength="200" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SportsAsset">
import { listAsset, getAsset, addAsset, updateAsset, delAsset, auditAsset, offlineAsset } from "@/api/sports/asset";
import { enabledCategoryList } from "@/api/sports/category";

const { proxy } = getCurrentInstance();
const { sp_equipment_class, sp_asset_status, sp_asset_audit_status } = proxy.useDict("sp_equipment_class", "sp_asset_status", "sp_asset_audit_status");

const loading = ref(false);
const showSearch = ref(true);
const total = ref(0);
const assetList = ref([]);
const categoryOptions = ref([]);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const open = ref(false);
const title = ref("");

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    assetName: undefined,
    classType: undefined,
    assetStatus: undefined,
    auditStatus: undefined
  },
  form: {},
  rules: {
    assetName: [{ required: true, message: "器材名称不能为空", trigger: "blur" }],
    assetCode: [{ required: true, message: "器材编码不能为空", trigger: "blur" }],
    categoryId: [{ required: true, message: "分类不能为空", trigger: "change" }],
    stockQty: [{ required: true, message: "总库存不能为空", trigger: "blur" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

function getCategoryOptions() {
  enabledCategoryList().then(res => {
    categoryOptions.value = res.data || [];
  });
}

function getList() {
  loading.value = true;
  listAsset(queryParams.value).then(res => {
    assetList.value = res.rows;
    total.value = res.total;
  }).catch(() => {
    assetList.value = [];
    total.value = 0;
  }).finally(() => {
    loading.value = false;
  });
}

function reset() {
  form.value = {
    assetId: undefined,
    categoryId: undefined,
    assetName: undefined,
    assetCode: undefined,
    barcode: undefined,
    brand: undefined,
    purchaseDate: undefined,
    purchasePrice: 0,
    depreciationRate: 10,
    serviceLifeYear: 5,
    assetStatus: "0",
    auditStatus: "0",
    location: undefined,
    stockQty: 1,
    availableQty: 1,
    coverImage: undefined,
    remark: undefined
  };
  proxy.resetForm("assetRef");
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.assetId);
  single.value = selection.length !== 1;
  multiple.value = selection.length === 0;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增器材台账";
}

function handleUpdate(row) {
  reset();
  const assetId = row?.assetId || ids.value[0];
  getAsset(assetId).then(res => {
    form.value = res.data;
    open.value = true;
    title.value = "修改器材台账";
  });
}

function cancel() {
  open.value = false;
}

function submitForm() {
  proxy.$refs.assetRef.validate(valid => {
    if (!valid) return;
    if (form.value.availableQty > form.value.stockQty) {
      proxy.$modal.msgError("可用库存不能大于总库存");
      return;
    }
    const req = form.value.assetId ? updateAsset(form.value) : addAsset(form.value);
    req.then(() => {
      proxy.$modal.msgSuccess(form.value.assetId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row) {
  const assetIds = row?.assetId || ids.value;
  proxy.$modal.confirm("确认删除器材编号为 " + assetIds + " 的数据项？").then(() => {
    return delAsset(assetIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

function handleBatchOffline() {
  proxy.$modal.confirm("确认将选中器材批量下架？").then(() => {
    return offlineAsset(ids.value.join(","));
  }).then(() => {
    proxy.$modal.msgSuccess("下架成功");
    getList();
  }).catch(() => {});
}

function handleAudit(row, status) {
  const text = status === "1" ? "审核通过" : "审核驳回";
  proxy.$modal.confirm("确认执行“" + text + "”？").then(() => {
    return auditAsset({ assetId: row.assetId, auditStatus: status });
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功");
    getList();
  }).catch(() => {});
}

getCategoryOptions();
getList();
</script>
