import request from "@/utils/request";

export function uploadFile(file) {
  const formData = new FormData();
  formData.append("file", file);
  return request({
    url: "/common/upload",
    method: "post",
    headers: { "Content-Type": "multipart/form-data" },
    data: formData
  });
}

export function uploadAvatar(file) {
  const formData = new FormData();
  formData.append("avatarfile", file);
  return request({
    url: "/system/user/profile/avatar",
    method: "post",
    headers: { "Content-Type": "multipart/form-data" },
    data: formData
  });
}

export function getDictDataByType(dictType) {
  return request({
    url: `/system/dict/data/type/${dictType}`,
    method: "get"
  });
}
