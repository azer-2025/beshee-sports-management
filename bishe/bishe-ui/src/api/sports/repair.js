import request from "@/utils/request";

export function listRepair(query) {
  return request({
    url: "/sports/repair/list",
    method: "get",
    params: query
  });
}

export function getRepair(repairId) {
  return request({
    url: "/sports/repair/" + repairId,
    method: "get"
  });
}

export function updateRepair(data) {
  return request({
    url: "/sports/repair",
    method: "put",
    data: data
  });
}
