import request from "@/utils/request";

export function getProfile() {
  return request({
    url: "/system/user/profile",
    method: "get"
  });
}

export function updateProfile(data) {
  return request({
    url: "/system/user/profile",
    method: "put",
    data
  });
}

export function updatePassword(data) {
  return request({
    url: "/system/user/profile/updatePwd",
    method: "put",
    data
  });
}
