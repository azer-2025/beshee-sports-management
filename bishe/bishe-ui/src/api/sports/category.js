import request from "@/utils/request";

export function listCategory(query) {
  return request({
    url: "/sports/category/list",
    method: "get",
    params: query
  });
}

export function getCategory(categoryId) {
  return request({
    url: "/sports/category/" + categoryId,
    method: "get"
  });
}

export function addCategory(data) {
  return request({
    url: "/sports/category",
    method: "post",
    data: data
  });
}

export function updateCategory(data) {
  return request({
    url: "/sports/category",
    method: "put",
    data: data
  });
}

export function delCategory(categoryId) {
  return request({
    url: "/sports/category/" + categoryId,
    method: "delete"
  });
}

export function enabledCategoryList() {
  return request({
    url: "/sports/category/enabled",
    method: "get"
  });
}
