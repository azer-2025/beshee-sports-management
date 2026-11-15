import request from "@/utils/request";

export function getProfileExt() {
  return request({
    url: "/front/profile/ext",
    method: "get"
  });
}

export function updateProfileExt(data) {
  return request({
    url: "/front/profile/ext",
    method: "put",
    data
  });
}

