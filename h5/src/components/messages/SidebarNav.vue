<template>
    <div class="sidebar-wrapper" :style="wrapperStyle">
        <!-- 侧边栏主体 -->
        <aside class="sidebar" :class="{ 'collapsed': collapsed }" :style="{
            width: collapsed ? `70px` : `220px`,
            backgroundColor: bgColor,
            color: textColor
        }">
            <div class="nav-title">
                <img src="/tender-info-platform.svg" alt="系统logo" class="title-logo" />
                <span class="title-text" v-if="!collapsed">配置中心</span>
            </div>
            <div class="nav-container">
                <!-- 导航菜单 -->
                <nav class="nav-menu">
                    <ul class="menu-list">
                        <li v-for="(item, index) in items" :key="index" class="menu-item"
                            :class="{ 'active': route.path === item.path }" @click="handleItemClick(item)">
                            <!-- 菜单图标 -->
                            <div class="menu-icon">
                                <img :src="item.iconPath" alt="item.label" width="24" height="24" />
                            </div>

                            <!-- 菜单文本（折叠时隐藏） -->
                            <span class="menu-text" v-if="!collapsed">{{ item.label }}</span>
                        </li>
                    </ul>
                </nav>
                <!-- 折叠/展开按钮 -->
                <button class="toggle-btn" @click="toggleCollapse" :aria-label="collapsed ? '展开导航' : '折叠导航'">
                    <svg class="toggle-icon" :class="{ 'rotate': collapsed }" width="20" height="20" viewBox="0 0 24 24"
                        fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                            stroke-linejoin="round" />
                    </svg>
                </button>
            </div>

        </aside>

        <!-- 遮罩层（移动端折叠时点击关闭） -->
        <div class="sidebar-backdrop" v-if="showBackdrop" @click="toggleCollapse"></div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import homeIcon from '../../assets/icons/home.png';
import configIcon from '../../assets/icons/config.png';

const router = useRouter();
const route = useRoute(); // 获取当前路由实例

// 定义组件属性
const props = defineProps({
    items: {
        type: Array,
        default: () => [
            // 给每个菜单项添加对应的路由路径（与 router/index.js 中的 path 一致）
            {
                label: '首页',
                iconPath: homeIcon,
                path: '/send-message'  // 对应路由路径
            },
            // {
            //     label: '邮件配置',
            //     iconPath: configIcon,
            //     path: '/send-message'  // 对应路由路径
            // }
        ]
    },
    // 其他属性保持不变...
    expandedWidth: { type: Number, default: 220 },
    collapsedWidth: { type: Number, default: 60 },
    bgColor: { type: String, default: '#1e293b' },
    textColor: { type: String, default: '#e2e8f0' },
    activeColor: { type: String, default: '#3b82f6' },
    mobile: { type: Boolean, default: false }
});

const items = ref([...props.items]);// 深拷贝避免直接修改 props

// 定义事件 
const emit = defineEmits(['toggleCollapse', 'changeRouter']);

// 折叠状态 
const collapsed = ref(false);

// 切换折叠状态 
const toggleCollapse = () => {
    collapsed.value = !collapsed.value;
    emit('toggleCollapse', collapsed.value);
};

// 处理菜单项点击：添加路由跳转逻辑
const handleItemClick = (item) => {
    emit('changeRouter', item);
    if (item.path) {
        router.push(item.path);
    }
};

// 计算是否显示遮罩（移动端且未折叠时）
const showBackdrop = computed(() => {
    return props.mobile && !collapsed.value;
});

// 计算外层容器样式
const wrapperStyle = computed(() => {
    return props.mobile
        ? { position: 'fixed', top: 0, left: 0, zIndex: 1000 }
        : { position: 'relative' };
});
</script>

<style scoped>
/* 基础布局属性 */
.sidebar-wrapper {}

.sidebar {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  height: 100%;
  overflow: hidden;
  transition: width 0.3s ease;
}

.collapsed {
  overflow: visible;
}

.nav-title {
  position: fixed;
  display: flex;
  align-items: center;
  height: 60px;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: padding 0.3s ease;
}

.title-logo {
  width: 32px;
  height: 32px;
  object-fit: contain;
}

.title-text {
  margin-left: 12px;
  font-size: 18px;
  font-weight: 500;
  white-space: nowrap;
}

.nav-container {
  position: fixed;
  display: flex;
  flex-direction: row;
  margin-top: 60px;
  height: 100%;
  width: 220px;
  overflow: auto;
  transition: width 0.3s ease;
}

.collapsed .nav-container {
  width: 70px;
}

.toggle-btn {
  position: absolute;
  top: 51%;
  right: 5px;
  margin: 0 auto;
  background: transparent;
  border: none;
  color: inherit;
  cursor: pointer;
}

.collapsed .toggle-btn {
  position: absolute;
  top: 51%;
  right: -5px;
  transition: transform 0.2s ease;
}

.toggle-icon {
  transition: transform 0.3s ease;
}

.rotate {
  transform: rotate(180deg);
}

.nav-menu {
  flex: 1;
}

.menu-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  cursor: pointer;
  transition: background-color 0.2s ease;
  color: inherit;
}

.menu-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  flex-shrink: 0;
}

.menu-text {
  margin-left: 16px;
  transition: opacity 0.3s ease;
  white-space: nowrap;
}

.sidebar-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: -1;
  transition: opacity 0.3s ease;
}

@media (max-width: 768px) {
  .sidebar:not(.collapsed) {
    box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  }
}

/* 视觉表现属性（补充归类） */
.menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.menu-item.active {
  background-color: v-bind(activeColor);
  color: white;
}
</style>
