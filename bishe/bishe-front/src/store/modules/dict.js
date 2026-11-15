import { defineStore } from "pinia";

export const useDictStore = defineStore("front-dict", {
  state: () => ({
    dict: []
  }),
  actions: {
    getDict(key) {
      if (!key) return null;
      const found = this.dict.find((item) => item.key === key);
      return found ? found.value : null;
    },
    setDict(key, value) {
      if (!key) return;
      const idx = this.dict.findIndex((item) => item.key === key);
      if (idx >= 0) {
        this.dict[idx].value = value;
      } else {
        this.dict.push({ key, value });
      }
    },
    removeDict(key) {
      const idx = this.dict.findIndex((item) => item.key === key);
      if (idx >= 0) {
        this.dict.splice(idx, 1);
        return true;
      }
      return false;
    },
    cleanDict() {
      this.dict = [];
    }
  }
});

