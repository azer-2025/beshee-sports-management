import request from "@/utils/request";

export function listFrontCategory() {
  return request({
    url: "/front/equipment/category/list",
    method: "get"
  });
}

export function listFrontEquipment(params) {
  return request({
    url: "/front/equipment/list",
    method: "get",
    params
  });
}

export function getFrontEquipment(assetId) {
  return request({
    url: "/front/equipment/" + assetId,
    method: "get"
  });
}
