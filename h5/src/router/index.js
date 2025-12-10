import { createRouter, createWebHistory } from 'vue-router';
// 导入各模块路由
import tenderRoutes from './modules/tenders';
import messageRoutes from './modules/messages';

// 可以在这里添加全局路由守卫
const router = createRouter({
  history: createWebHistory('/tender-info-platform/'),
  // 合并所有路由模块
  routes: [
    ...tenderRoutes,
    ...messageRoutes
  ]
});

export default router;