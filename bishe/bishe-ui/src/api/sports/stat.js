import request from "@/utils/request";

export function getStatSummary() {
  return request({
    url: "/sports/stat/summary",
    method: "get"
  });
}

export function getUsageRank() {
  return request({
    url: "/sports/stat/usageRank",
    method: "get"
  });
}

export function getDamageReason() {
  return request({
    url: "/sports/stat/damageReason",
    method: "get"
  });
}

export function getMonthlyTrend() {
  return request({
    url: "/sports/stat/monthlyTrend",
    method: "get"
  });
}

export function getSuggestionReport(query) {
  return request({
    url: "/sports/stat/suggestion",
    method: "get",
    params: query
  });
}
