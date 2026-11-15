<template>
  <div class="app-container">
    <el-alert
      v-if="!loading && total === 0"
      title="暂无报修数据。用户提交报修后可在此处理。"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 12px"
    />
    <el-form ref="queryRef" :model="queryParams" :inline="true" v-show="showSearch">
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 160px">
          <el-option v-for="dict in sp_repair_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="损耗原因" prop="faultReason">
        <el-select v-model="queryParams.faultReason" placeholder="请选择损耗原因" clearable style="width: 160px">
          <el-option v-for="dict in sp_repair_reason" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>

    <el-table v-loading="loading" :data="repairList" empty-text="暂无报修数据">
      <el-table-column label="报修ID" prop="repairId" width="88" />
      <el-table-column label="用户" prop="userName" width="120" />
      <el-table-column label="器材" prop="assetName" min-width="140" />
      <el-table-column label="故障描述" prop="faultDesc" min-width="180" show-overflow-tooltip />
      <el-table-column label="故障图片" width="90">
        <template #default="scope">
          <image-preview :src="scope.row.faultImage" :width="48" :height="48" />
        </template>
      </el-table-column>
      <el-table-column label="损耗原因" width="120">
        <template #default="scope">
          <dict-tag :options="sp_repair_reason" :value="scope.row.faultReason" />
        </template>
      </el-table-column>
      <el-table-column label="紧急程度" width="100">
        <template #default="scope">
          <dict-tag :options="sp_repair_urgency" :value="scope.row.urgency" />
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <dict-tag :options="sp_repair_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="处理反馈" prop="processNote" min-width="160" show-overflow-tooltip />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleProcess(scope.row)" v-hasPermi="['sports:repair:edit']">处理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog title="报修处理" v-model="open" width="620px" append-to-body>
      <el-form ref="repairRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="器材">{{ form.assetName }}</el-form-item>
        <el-form-item label="报修描述">{{ form.faultDesc }}</el-form-item>
        <el-form-item label="处理状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择处理状态">
            <el-option v-for="dict in sp_repair_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理反馈" prop="processNote">
          <el-input v-model="form.processNote" type="textarea" :rows="4" maxlength="300" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SportsRepair">
import { listRepair, getRepair, updateRepair } from "@/api/sports/repair";

const { proxy } = getCurrentInstance();
const { sp_repair_status, sp_repair_reason, sp_repair_urgency } = proxy.useDict("sp_repair_status", "sp_repair_reason", "sp_repair_urgency");

const loading = ref(false);
const showSearch = ref(true);
const total = ref(0);
const repairList = ref([]);
const open = ref(false);

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    status: undefined,
    faultReason: undefined
  },
  form: {},
  rules: {
    status: [{ required: true, message: "请选择处理状态", trigger: "change" }],
    processNote: [{ required: true, message: "请输入处理反馈", trigger: "blur" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listRepair(queryParams.value).then(res => {
    repairList.value = res.rows;
    total.value = res.total;
  }).catch(() => {
    repairList.value = [];
    total.value = 0;
  }).finally(() => {
    loading.value = false;
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

function handleProcess(row) {
  getRepair(row.repairId).then(res => {
    form.value = res.data;
    open.value = true;
  });
}

function submitForm() {
  proxy.$refs.repairRef.validate(valid => {
    if (!valid) return;
    updateRepair(form.value).then(() => {
      proxy.$modal.msgSuccess("处理成功");
      open.value = false;
      getList();
    });
  });
}

getList();
</script>
