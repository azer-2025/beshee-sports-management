<template>
  <div class="app-container">
    <el-alert
      v-if="!loading && total === 0"
      title="暂无分类数据，请先执行 sql/sports_increment.sql 初始化或手动新增分类。"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 12px"
    />
    <el-form ref="queryRef" :model="queryParams" :inline="true" v-show="showSearch">
      <el-form-item label="分类名称" prop="categoryName">
        <el-input v-model="queryParams.categoryName" placeholder="请输入分类名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="器材大类" prop="classType">
        <el-select v-model="queryParams.classType" placeholder="请选择器材大类" clearable style="width: 180px">
          <el-option v-for="dict in sp_equipment_class" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 160px">
          <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['sports:category:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['sports:category:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['sports:category:remove']">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="categoryList"
      empty-text="暂无分类数据"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="分类ID" prop="categoryId" width="90" />
      <el-table-column label="分类名称" prop="categoryName" min-width="140" />
      <el-table-column label="器材大类" prop="classType" width="120">
        <template #default="scope">
          <dict-tag :options="sp_equipment_class" :value="scope.row.classType" />
        </template>
      </el-table-column>
      <el-table-column label="分类编码" prop="categoryCode" min-width="180" show-overflow-tooltip />
      <el-table-column label="排序" prop="orderNum" width="90" />
      <el-table-column label="状态" prop="status" width="110">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['sports:category:edit']">修改</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['sports:category:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" v-model="open" width="560px" append-to-body>
      <el-form ref="categoryRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="器材大类" prop="classType">
          <el-select v-model="form.classType" placeholder="请选择器材大类">
            <el-option v-for="dict in sp_equipment_class" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类编码" prop="categoryCode">
          <el-input v-model="form.categoryCode" placeholder="例如 BALL_BASKETBALL" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" maxlength="200" show-word-limit />
        </el-form-item>
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

<script setup name="SportsCategory">
import { listCategory, getCategory, addCategory, updateCategory, delCategory } from "@/api/sports/category";

const { proxy } = getCurrentInstance();
const { sp_equipment_class, sys_normal_disable } = proxy.useDict("sp_equipment_class", "sys_normal_disable");

const loading = ref(false);
const showSearch = ref(true);
const total = ref(0);
const categoryList = ref([]);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const open = ref(false);
const title = ref("");

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    categoryName: undefined,
    classType: undefined,
    status: undefined
  },
  form: {},
  rules: {
    categoryName: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
    classType: [{ required: true, message: "器材大类不能为空", trigger: "change" }],
    categoryCode: [{ required: true, message: "分类编码不能为空", trigger: "blur" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listCategory(queryParams.value).then(res => {
    categoryList.value = res.rows;
    total.value = res.total;
  }).catch(() => {
    categoryList.value = [];
    total.value = 0;
  }).finally(() => {
    loading.value = false;
  });
}

function reset() {
  form.value = {
    categoryId: undefined,
    categoryName: undefined,
    classType: "1",
    categoryCode: undefined,
    orderNum: 0,
    status: "0",
    remark: undefined
  };
  proxy.resetForm("categoryRef");
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
  ids.value = selection.map(item => item.categoryId);
  single.value = selection.length !== 1;
  multiple.value = selection.length === 0;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增器材分类";
}

function handleUpdate(row) {
  reset();
  const categoryId = row?.categoryId || ids.value[0];
  getCategory(categoryId).then(res => {
    form.value = res.data;
    open.value = true;
    title.value = "修改器材分类";
  });
}

function cancel() {
  open.value = false;
}

function submitForm() {
  proxy.$refs.categoryRef.validate(valid => {
    if (!valid) return;
    const req = form.value.categoryId ? updateCategory(form.value) : addCategory(form.value);
    req.then(() => {
      proxy.$modal.msgSuccess(form.value.categoryId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row) {
  const categoryIds = row?.categoryId || ids.value;
  proxy.$modal.confirm("确认删除分类编号为 " + categoryIds + " 的数据项？").then(() => {
    return delCategory(categoryIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

getList();
</script>
