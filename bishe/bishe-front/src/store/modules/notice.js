import { defineStore } from "pinia";
import { myBorrowList } from "@/api/borrow";
import { myRepairList } from "@/api/repair";
import { recentNoticeList } from "@/api/notice";

const BORROW_STATUS_TEXT = {
  "0": "待审批",
  "1": "审批通过",
  "2": "审批驳回",
  "3": "借出中",
  "4": "已归还",
  "5": "已逾期"
};

const REPAIR_STATUS_TEXT = {
  "0": "待受理",
  "1": "已受理",
  "2": "维修中",
  "3": "已修复",
  "4": "已驳回"
};

function safeTimeValue(time) {
  if (!time) return 0;
  return new Date(String(time).replace(" ", "T")).getTime() || 0;
}

function stripHtml(content) {
  return String(content || "").replace(/<[^>]*>/g, "").trim();
}

function briefText(content, maxLen = 48) {
  const txt = stripHtml(content);
  if (txt.length <= maxLen) {
    return txt;
  }
  return `${txt.slice(0, maxLen)}...`;
}

function resolveNoticeLevel(item) {
  const title = String(item.noticeTitle || "");
  const content = stripHtml(item.noticeContent);
  const fullText = `${title} ${content}`;
  if (/紧急|重要|预警|故障|停服|维护|风险|异常/i.test(fullText)) {
    return "danger";
  }
  if (String(item.noticeType) === "1") {
    return "warning";
  }
  return "info";
}

export const useNoticeStore = defineStore("front-notice", {
  state: () => ({
    summary: {
      pendingBorrow: 0,
      overdueBorrow: 0,
      pendingRepair: 0,
      processingRepair: 0,
      unreadTotal: 0
    },
    recentList: [],
    lastRefreshTime: "",
    loading: false
  }),
  getters: {
    noticeCount: (state) => state.summary.unreadTotal || 0
  },
  actions: {
    async safeBorrowQuery(params) {
      try {
        return await myBorrowList(params);
      } catch (error) {
        return { total: 0, rows: [] };
      }
    },
    async safeRepairQuery(params) {
      try {
        return await myRepairList(params);
      } catch (error) {
        return { total: 0, rows: [] };
      }
    },
    async safeNoticeQuery(params) {
      try {
        const res = await recentNoticeList(params);
        return Array.isArray(res.data) ? res.data : [];
      } catch (error) {
        return [];
      }
    },
    async refreshSummary() {
      const [pendingBorrowRes, overdueBorrowRes, pendingRepairRes, acceptedRepairRes, processingRepairRes] = await Promise.all([
        this.safeBorrowQuery({ pageNum: 1, pageSize: 1, status: "0" }),
        this.safeBorrowQuery({ pageNum: 1, pageSize: 1, status: "5" }),
        this.safeRepairQuery({ pageNum: 1, pageSize: 1, status: "0" }),
        this.safeRepairQuery({ pageNum: 1, pageSize: 1, status: "1" }),
        this.safeRepairQuery({ pageNum: 1, pageSize: 1, status: "2" })
      ]);

      const pendingBorrow = Number(pendingBorrowRes.total || 0);
      const overdueBorrow = Number(overdueBorrowRes.total || 0);
      const pendingRepair = Number(pendingRepairRes.total || 0);
      const processingRepair = Number(acceptedRepairRes.total || 0) + Number(processingRepairRes.total || 0);

      this.summary = {
        pendingBorrow,
        overdueBorrow,
        pendingRepair,
        processingRepair,
        unreadTotal: pendingBorrow + overdueBorrow + pendingRepair + processingRepair
      };

      this.lastRefreshTime = new Date().toLocaleString();
    },
    async refreshRecent(limit = 8) {
      const [borrowRes, repairRes, noticeRes] = await Promise.all([
        this.safeBorrowQuery({ pageNum: 1, pageSize: limit }),
        this.safeRepairQuery({ pageNum: 1, pageSize: limit }),
        this.safeNoticeQuery({ limit })
      ]);

      const borrowNotices = (borrowRes.rows || []).map((item) => ({
        id: `borrow-${item.borrowId}`,
        bizType: "borrow",
        title: `借还 ${BORROW_STATUS_TEXT[item.status] || "状态变更"}`,
        content: `${item.assetName || "器材"} x${item.quantity || 1}`,
        rawContent: `${item.assetName || "器材"} x${item.quantity || 1}`,
        statusLabel: BORROW_STATUS_TEXT[item.status] || "未知",
        level: item.status === "5" ? "danger" : item.status === "0" ? "warning" : "info",
        time: item.updateTime || item.createTime || "",
        route: "/my-borrow",
        actionText: "立即处理"
      }));

      const repairNotices = (repairRes.rows || []).map((item) => ({
        id: `repair-${item.repairId}`,
        bizType: "repair",
        title: `报修 ${REPAIR_STATUS_TEXT[item.status] || "状态变更"}`,
        content: item.faultDesc || (item.assetName ? `${item.assetName} 报修` : "报修记录"),
        rawContent: item.faultDesc || (item.assetName ? `${item.assetName} 报修` : "报修记录"),
        statusLabel: REPAIR_STATUS_TEXT[item.status] || "未知",
        level: item.status === "4" ? "danger" : item.status === "0" ? "warning" : "info",
        time: item.updateTime || item.createTime || "",
        route: "/my-repair",
        actionText: "立即处理"
      }));

      const systemNotices = (noticeRes || []).map((item) => ({
        id: `sys-notice-${item.noticeId}`,
        bizType: "notice",
        title: item.noticeTitle || "系统通知",
        content: briefText(item.noticeContent) || "请查看系统公告详情",
        rawContent: String(item.noticeContent || ""),
        statusLabel: String(item.noticeType) === "1" ? "系统通知" : "平台公告",
        level: resolveNoticeLevel(item),
        time: item.createTime || item.updateTime || "",
        route: "",
        actionText: ""
      }));

      this.recentList = [...borrowNotices, ...repairNotices, ...systemNotices]
        .sort((a, b) => safeTimeValue(b.time) - safeTimeValue(a.time))
        .slice(0, limit);
    },
    async refreshAll() {
      if (this.loading) return;
      this.loading = true;
      try {
        await Promise.all([this.refreshSummary(), this.refreshRecent()]);
      } finally {
        this.loading = false;
      }
    },
    clear() {
      this.summary = {
        pendingBorrow: 0,
        overdueBorrow: 0,
        pendingRepair: 0,
        processingRepair: 0,
        unreadTotal: 0
      };
      this.recentList = [];
      this.lastRefreshTime = "";
    }
  }
});
