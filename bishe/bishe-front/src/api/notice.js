import request from "@/utils/request";

export function recentNoticeList(params) {
  return request({
    url: "/front/notice/recent",
    method: "get",
    params
  });
}

