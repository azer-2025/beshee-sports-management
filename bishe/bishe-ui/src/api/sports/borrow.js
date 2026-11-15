import request from "@/utils/request";

export function listBorrow(query) {
  return request({
    url: "/sports/borrow/list",
    method: "get",
    params: query
  });
}

export function getBorrow(borrowId) {
  return request({
    url: "/sports/borrow/" + borrowId,
    method: "get"
  });
}

export function approveBorrow(data) {
  return request({
    url: "/sports/borrow/approve",
    method: "put",
    data: data
  });
}

export function returnBorrow(borrowId) {
  return request({
    url: "/sports/borrow/return/" + borrowId,
    method: "put"
  });
}

export function listOverdueBorrow(query) {
  return request({
    url: "/sports/borrow/overdue/list",
    method: "get",
    params: query
  });
}

export function getOverdueDashboard() {
  return request({
    url: "/sports/borrow/overdue/dashboard",
    method: "get"
  });
}

export function getOverdueReasonStats() {
  return request({
    url: "/sports/borrow/overdue/reasonStats",
    method: "get"
  });
}

export function fillOverdueReason(data) {
  return request({
    url: "/sports/borrow/overdue/reason",
    method: "put",
    data: data
  });
}
