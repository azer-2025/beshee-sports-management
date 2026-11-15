import { ref, toRefs } from "vue";
import { getDictDataByType } from "@/api/common";
import { useDictStore } from "@/store/modules/dict";

export function useDict(...args) {
  const res = ref({});
  return (() => {
    const dictStore = useDictStore();
    args.forEach((dictType) => {
      res.value[dictType] = [];
      const cached = dictStore.getDict(dictType);
      if (cached && Array.isArray(cached)) {
        res.value[dictType] = cached;
        return;
      }
      getDictDataByType(dictType).then((resp) => {
        const data = Array.isArray(resp?.data) ? resp.data : [];
        const mapped = data.map((p) => ({
          label: p.dictLabel,
          value: p.dictValue,
          elTagType: p.listClass,
          elTagClass: p.cssClass
        }));
        res.value[dictType] = mapped;
        dictStore.setDict(dictType, mapped);
      }).catch(() => {
        res.value[dictType] = [];
      });
    });
    return toRefs(res.value);
  })();
}

