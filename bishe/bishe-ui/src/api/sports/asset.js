import request from "@/utils/request";

export function listAsset(query) {
  return request({
    url: "/sports/asset/list",
    method: "get",
    params: query
  });
}

export function getAsset(assetId) {
  return request({
    url: "/sports/asset/" + assetId,
    method: "get"
  });
}

export function addAsset(data) {
  return request({
    url: "/sports/asset",
    method: "post",
    data: data
  });
}

export function updateAsset(data) {
  return request({
    url: "/sports/asset",
    method: "put",
    data: data
  });
}

export function delAsset(assetId) {
  return request({
    url: "/sports/asset/" + assetId,
    method: "delete"
  });
}

export function auditAsset(data) {
  return request({
    url: "/sports/asset/audit",
    method: "put",
    data: data
  });
}

export function offlineAsset(assetIds) {
  return request({
    url: "/sports/asset/offline/" + assetIds,
    method: "put"
  });
}

export function listAvailableAsset(query) {
  return request({
    url: "/sports/asset/available/list",
    method: "get",
    params: query
  });
}
