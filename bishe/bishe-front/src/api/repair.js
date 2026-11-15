import request from "@/utils/request";

export function myRepairList(params) {
  return request({
    url: "/front/repair/my",
    method: "get",
    params
  });
}

export function reportRepair(data) {
  return request({
    url: "/front/repair/report",
    method: "post",
    data
  });
}

export function getRepairDetail(repairId) {
  return request({
    url: "/front/repair/" + repairId,
    method: "get"
  });
}
