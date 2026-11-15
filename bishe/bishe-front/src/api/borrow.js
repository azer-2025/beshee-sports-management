import request from "@/utils/request";

export function myBorrowList(params) {
  return request({
    url: "/front/borrow/my",
    method: "get",
    params
  });
}

export function applyBorrow(data) {
  return request({
    url: "/front/borrow/apply",
    method: "post",
    data
  });
}

export function cancelBorrow(borrowId) {
  return request({
    url: "/front/borrow/cancel/" + borrowId,
    method: "put"
  });
}

export function returnBorrow(borrowId) {
  return request({
    url: "/front/borrow/return/" + borrowId,
    method: "put"
  });
}

export function reviewBorrow(data) {
  return request({
    url: "/front/borrow/review",
    method: "put",
    data
  });
}
