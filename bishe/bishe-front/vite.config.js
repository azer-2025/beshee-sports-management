import { defineConfig, loadEnv } from "vite";
import vue from "@vitejs/plugin-vue";
import path from "path";

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd());
  return {
    plugins: [vue()],
    resolve: {
      alias: {
        "@": path.resolve(__dirname, "./src")
      }
    },
    server: {
      port: 5173,
      host: true,
      proxy: {
        [env.VITE_APP_BASE_API || "/dev-api"]: {
          target: "http://localhost:8080",
          changeOrigin: true,
          rewrite: p => p.replace(/^\/dev-api/, "")
        }
      }
    }
  };
});
