<template>
  <div class="min-h-screen bg-gray-50">
    <!-- 导航栏 -->
    <Navbar :selected-region="selectedRegion" :search-query="searchQuery" :selected-date="selectedDate"
      @region-change="handleRegionChange" @date-change="handleDateChange" @refresh="handleRefresh"
      @search="handleSearch" />

    <!-- 筛选标签（带清除Icon） -->
    <FilterTags :selected-region="selectedRegion" :search-query="searchQuery" @clear-region="handleClearRegion"
      @clear-search="handleClearSearch" @clear-all="handleClearAll" />
    <!-- 主体内容 -->
    <main class="container mx-auto px-4 py-4">
      <!-- Tab 导航栏 -->
      <div class="tab-nav flex flex-wrap border-b border-gray-200 mb-4">
        <button class="tab-btn flex-1 sm:flex-auto px-2 sm:px-4 py-2 text-sm font-medium transition-colors" :class="{
          'text-gray-500 hover:text-primary': currentTab !== 'projectPurchase',
          'text-primary font-semibold': currentTab === 'projectPurchase'
        }" @click="switchTab('projectPurchase')">
          项目采购
          <div v-if="currentTab === 'projectPurchase'" class="w-full h-1 mt-1 bg-gradient-to-r from-primary to-accent">
          </div>
        </button>
        <button class="tab-btn flex-1 sm:flex-auto px-2 sm:px-4 py-2 text-sm font-medium transition-colors" :class="{
          'text-gray-500 hover:text-primary': currentTab !== 'serviceMart',
          'text-primary font-semibold': currentTab === 'serviceMart'
        }" @click="switchTab('serviceMart')">
          服务工程
          <div v-if="currentTab === 'serviceMart'" class="w-full h-1 mt-1 bg-gradient-to-r from-primary to-accent">
          </div>
        </button>
      </div>
      <!-- 列表计数 -->
      <div class="text-xs text-neutral mb-4 flex justify-between items-center">
        <span>共找到
          <span class="text-primary font-medium">{{ currentTotalCount }}</span> 条信息
        </span>
        <span v-if="currentTotalCount > 0" class="text-xs text-gray-400">
          显示 {{ startIndex }}-{{ endIndex }} 条
        </span>
      </div>

      <!-- 列表内容 -->
      <ul class="space-y-3">
        <!-- 加载中（渐变骨架屏） -->
        <template v-if="isLoading && currentTenders.length === 0">
          <li v-for="i in 5" :key="i" class="loading-skeleton rounded-lg p-4">
            <div class="flex items-center space-x-4">
              <div class="w-10 h-10 bg-gray-200 rounded-full animate-pulse"></div>
              <div class="flex-1 space-y-2">
                <div class="h-4 bg-gray-200 rounded w-3/4 animate-pulse"></div>
                <div class="h-4 bg-gray-200 rounded w-1/2 animate-pulse"></div>
              </div>
            </div>
          </li>
        </template>

        <!-- 有数据 -->
        <template v-else-if="currentTenders.length > 0">
          <TenderItem v-for="(tender, index) in currentTenders" :key="tender.url" :tender="tender" :index="index"
            :current-tab="currentTab" :serial-number="getSerialNumber(index)" />
        </template>

        <!-- 无数据（引导性提示） -->
        <template v-else>
          <li class="py-8 text-center space-y-3">
            <div class="w-24 h-24 mx-auto bg-gray-100 rounded-full flex items-center justify-center">
              <i class="fa fa-search text-3xl text-gray-300"></i>
            </div>
            <p class="text-neutral">当天暂无匹配的{{ currentTab === 'projectPurchase' ? '项目采购' : '服务工程' }}信息</p>
            <button @click="handleRefresh"
              class="mt-2 px-4 py-2 bg-primary text-white rounded-lg text-sm hover:bg-primary/90">
              重新加载
            </button>
          </li>
        </template>

        <!-- 👇 下拉加载的观察占位符 -->
        <div ref="observeTarget" class="observe-target"></div>

        <!-- 无更多数据提示 -->
        <li v-if="!hasMore && currentPage > 1" class="no-more-data py-4 text-center text-sm text-neutral">
          <div class="inline-flex items-center gap-2">
            <i class="fa fa-check-circle text-success"></i>
            <span>已加载全部数据</span>
          </div>
        </li>
      </ul>
    </main>

    <!-- 页脚、回到顶部按钮、错误提示 保持原有代码不变 -->
    <footer class="py-4 text-center text-xs text-neutral border-t border-gray-100">
      <p>© 2025 招标信息平台. 保留所有权利.</p>
    </footer>

    <button @click="scrollToTop"
      class="fixed bottom-6 right-6 bg-primary text-white w-10 h-10 rounded-full shadow-lg flex items-center justify-center transition-all z-50"
      :class="{
        'opacity-0 invisible': !showBackToTop,
        'opacity-100 visible': showBackToTop,
        'scale-90 hover:scale-100': showBackToTop
      }">
      <i class="fa fa-arrow-up text-sm"></i>
    </button>

    <div v-if="errorMessage"
      class="error-toast fixed top-4 left-1/2 transform -translate-x-1/2 bg-red-500 text-white px-4 py-2 rounded-lg z-50 animate-fade-in">
      <div class="flex items-center gap-2">
        <i class="fa fa-exclamation-circle"></i>
        <span>{{ errorMessage }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, onActivated, onUnmounted, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import Navbar from '../../components/tenders/Navbar.vue';
import TenderItem from '../../components/tenders/TenderItem.vue';
import FilterTags from '../../components/tenders/FilterTags.vue';

import { getProjectPurchaseList } from '../../api/tenders/project-purchase';
import { getServiceMartList } from '../../api/tenders/service-mart';
import { formatDate } from '../../utils/format';

// 路由和状态管理 
const router = useRouter();

// 响应式变量集中初始化，精简定义
const currentTab = ref('projectPurchase');
const projectPurchaseList = ref([]);
const serviceMartList = ref([]);
const totalCounts = ref({ projectPurchase: 0, serviceMart: 0 });
const currentPage = ref(1);
const itemsPerPage = ref(10);
const isLoading = ref(true);
const hasMore = ref(true);
const searchQuery = ref('');
const selectedRegion = ref('');
const selectedDate = ref('');
const errorMessage = ref('');
const showBackToTop = ref(false);
const observeTarget = ref(null);
let observer = null;

// 公共参数生成函数：提取重复的参数构造逻辑
const getParams = () => ({
  page: currentPage.value - 1,
  size: itemsPerPage.value,
  region: selectedRegion.value || '',
  keyword: searchQuery.value.trim() || '',
  date: selectedDate.value || ''
});

// 数据格式化函数：根据标签类型处理数据，减少重复逻辑
const formatTenders = (items, isProjectPurchase) => {
  if (isProjectPurchase) {
    return items.map(item => ({ ...item }));
  }
  return items.map(item => ({ ...item, budget: `${item.budget}元`, matter: '——' }));
};

// 公共数据请求函数：统一处理API调用和数据更新
const fetchTenders = async (isInitial = false) => {
  isLoading.value = true;
  const isProjectPurchase = currentTab.value === 'projectPurchase';
  const api = isProjectPurchase ? getProjectPurchaseList : getServiceMartList;
  const listRef = isProjectPurchase ? projectPurchaseList : serviceMartList;

  try {
    const res = await api(getParams());
    const formatted = formatTenders(res.data.content, isProjectPurchase);

    // 初始加载覆盖数据，加载更多拼接数据
    listRef.value = isInitial ? formatted : [...listRef.value, ...formatted];
    totalCounts.value[currentTab.value] = res.data.totalElements;
    hasMore.value = (currentPage.value * itemsPerPage.value) < currentTotalCount.value;
    errorMessage.value = '';
  } catch (err) {
    const typeText = isProjectPurchase ? '项目采购' : '服务工程';
    errorMessage.value = `加载${isInitial ? '' : '更多'}${typeText}信息失败，请稍后重试`;
    if (!isInitial) currentPage.value--; // 加载更多失败回退页码
    console.error(err);
  } finally {
    isLoading.value = false;
  }
};

// 核心刷新逻辑（初始加载）
const handleRefresh = () => {
  currentPage.value = 1;
  fetchTenders(true);
};

// 加载更多数据
const loadMoreTenders = async () => {
  if (isLoading.value || !hasMore.value) return;
  currentPage.value++;
  fetchTenders(false);
};

// 初始化 Intersection Observer
const initObserver = () => {
  observer?.disconnect(); // 可选链简化旧观察者断开
  if (observeTarget.value) {
    observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting && hasMore.value && !isLoading.value) {
          loadMoreTenders();
        }
      });
    }, { threshold: 0.1 });
    observer.observe(observeTarget.value);
  }
};

// 计算属性
const currentTenders = computed(() =>
  currentTab.value === 'projectPurchase' ? projectPurchaseList.value : serviceMartList.value
);
const currentTotalCount = computed(() => totalCounts.value[currentTab.value] || 0);
const startIndex = computed(() => 1);
const endIndex = computed(() => Math.min(currentPage.value * itemsPerPage.value, currentTotalCount.value));

// 序号计算
const getSerialNumber = (index) => {
  return index + 1; 
};

// 交互函数
const switchTab = (tab) => {
  if (currentTab.value === tab) return;
  currentTab.value = tab;
  currentPage.value = 1;
  handleRefresh();
};

// 筛选相关函数：统一调用handleRefresh，减少重复
const handleSearch = (query) => { searchQuery.value = query; handleRefresh(); };
const handleDateChange = (date) => { selectedDate.value = date; handleRefresh(); };
const handleRegionChange = (region) => { selectedRegion.value = region; handleRefresh(); };
const handleClearSearch = () => { searchQuery.value = ''; handleRefresh(); };
const handleClearRegion = () => { selectedRegion.value = ''; handleRefresh(); };
const handleClearAll = () => { selectedRegion.value = ''; searchQuery.value = ''; handleRefresh(); };

// 滚动相关函数
const scrollToTop = () => window.scrollTo({ top: 0, behavior: 'smooth' });
const handleScroll = () => {
  showBackToTop.value = (window.pageYOffset || document.documentElement.scrollTop) > 300;
};

// 监听筛选条件和路由同步
watch(
  [() => selectedRegion.value, () => selectedDate.value, () => searchQuery.value, () => currentTab.value],
  ([newRegion, newDate, newKeyword, newTab]) => {
    router.replace({
      name: 'TenderList',
      query: { region: newRegion, date: newDate, keyword: newKeyword, tab: newTab }
    });
  },
  { immediate: true }
);

// 监听数据变化重新初始化观察者
watch(currentTenders, () => nextTick(initObserver));

// 生命周期函数
onMounted(() => {
  const urlParams = new URLSearchParams(window.location.search);
  // 从URL恢复参数
  selectedRegion.value = urlParams.get('region') || '';
  selectedDate.value = urlParams.get('date') || '';
  searchQuery.value = urlParams.get('keyword') || '';
  const tabFromUrl = urlParams.get('tab');
  if (tabFromUrl && ['projectPurchase', 'serviceMart'].includes(tabFromUrl)) {
    currentTab.value = tabFromUrl;
  }
  if (!selectedDate.value) selectedDate.value = formatDate(new Date());

  handleRefresh();
  window.addEventListener('scroll', handleScroll);
  nextTick(initObserver); // 确保DOM渲染后初始化观察者
});

onUnmounted(() => {
  observer?.disconnect(); // 清理观察者
  window.removeEventListener('scroll', handleScroll);
});

onActivated(() => {
  handleRefresh();
  nextTick(initObserver); // 激活时重新初始化观察者
});
</script>

<style scoped>
/* 骨架屏动画 */
.loading-skeleton {
  background: linear-gradient(90deg, #f3f4f6 25%, #e5e7eb 50%, #f3f4f6 75%);
  background-size: 200% 100%;
  animation: skeleton-animation 1.5s infinite;
}

@keyframes skeleton-animation {
  0% {
    background-position: -200% 0;
  }

  100% {
    background-position: 200% 0;
  }
}

/* 错误提示动效 */
@keyframes fade-in {
  from {
    opacity: 0;
    transform: translate(-50%, -10px);
  }

  to {
    opacity: 1;
    transform: translate(-50%, 0);
  }
}

.animate-fade-in {
  animation: fade-in 0.3s ease-out;
}

/* 响应式调整 */
@media (max-width: 640px) {
  .tab-btn {
    @apply px-2;
  }
}

/* 👇 新增：观察占位符样式（透明，不影响布局） */
.observe-target {
  height: 1px;
  visibility: hidden;
}
</style>