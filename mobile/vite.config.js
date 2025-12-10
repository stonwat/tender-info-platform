import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  base: '/tender-info-platform/',
  server: {
    // 强制 Node.js 环境
    fs: {
      strict: false,
    },
  },
  optimizeDeps: {
    // 排除可能冲突的模块
    exclude: ['crypto-browserify'],
  },
});