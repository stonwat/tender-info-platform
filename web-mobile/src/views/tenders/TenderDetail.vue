<template>
  <div class="max-w-lg mx-auto p-4">
    <!-- 返回按钮 -->
    <button class="back-btn mb-4" @click="goBack">
      <i class="fa fa-chevron-left mr-2"></i>
    </button>

    <!-- 详情卡片 -->
    <div class="bg-white rounded-lg shadow p-6">
      <h1 class="text-xl font-bold mb-4" id="title">{{ tenderDetail?.title || '加载中...' }}</h1>

      <div class="detail-item flex items-start gap-2">
        <i class="fa fa-money text-primary mt-0.5 w-5 text-center"></i>
        <span>预算：{{ tenderDetail?.budget || '——' }}</span>
      </div>
      <div class="detail-item flex items-start gap-2">
        <i class="fa fa-list-alt text-primary mt-0.5 w-5 text-center"></i>
        <span>标的：{{ tenderDetail?.matter || '——' }}</span>
      </div>

      <div class="detail-item flex items-start gap-2">
        <i class="fa fa-tags text-primary mt-0.5 w-5 text-center"></i>
        <span>类型：{{ tenderDetail?.category || '——' }}</span>
      </div>
      <div class="detail-item flex items-start gap-2">
        <i class="fa fa-map-marker text-primary mt-0.5 w-5 text-center"></i>
        <span>地区：{{ formatRegion(tenderDetail?.region) || '——' }} {{ formatCounty(tenderDetail?.county) || '' }}</span>
      </div>
      <!-- <div class="detail-item flex items-start gap-2">
        <i class="fa fa-calendar-plus-o text-primary mt-0.5 w-5 text-center"></i>
        <span>报名时间：{{ tenderDetail?.startTime || '——' }}</span>
      </div> -->
      <div class="detail-item flex items-start gap-2">
        <i class="fa fa-calendar-plus-o text-primary mt-0.5 w-5 text-center"></i>
        <span>
          {{ route.query.currentTab === 'projectPurchase' ? '报名开始时间' : '报名开始时间' }}：
          {{ tenderDetail?.startTime || '——' }}
        </span>
      </div>
      <div class="detail-item flex items-start gap-2">
        <i class="fa fa-calendar-times-o text-primary mt-0.5 w-5 text-center"></i>
        <span>报名结束时间：{{ tenderDetail?.endTime || '——' }}</span>
      </div>
      <div class="detail-item flex items-start gap-2">
        <i class="fa fa-link text-primary mt-0.5 w-5 text-center"></i>
        链接：
        <a v-if="tenderDetail?.url" :href="tenderDetail.url" target="_blank" class="flex items-center url-link">
          <i class="fa fa-external-link-square text-primary mr-2 w-5"></i> <!-- 外部链接图标 -->
          <span class="text-blue-600 hover:underline">黑龙江省政府采购网</span> <!-- 固定显示网站名称 -->
        </a>
        <span v-else>——</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getProjectPurchaseDetailByUrl } from '../../api/tenders/project-purchase';
import { getServiceMartDetailByUrl } from '../../api/tenders/service-mart';
import { formatRegion, formatCounty } from '../../utils/format';

// 依赖注入和路由
const showMessage = inject('showMessage');
const route = useRoute();
const router = useRouter();

// 响应式数据
const tenderDetail = ref(null);

// 返回方法定义
const goBack = () => {
  router.push({
    name: 'TenderList',
    query: {
      region: route.query.region,
      keyword: route.query.keyword,
      date: route.query.date,
      tab: route.query.currentTab
    }
  });
};

// 数据处理函数
const processProjectPurchaseData = (data) => {
  return { ...data };
};

const processServiceMartData = (data) => {
  let projectType = '——';
  try {
    const parsedLlm = JSON.parse(data.llm);
    projectType = parsedLlm["项目类型"] || '——';
  } catch (parseError) {
    console.error('解析项目类型失败:', parseError);
  }

  return {
    ...data,
    budget: `${data.budget}元`,
    category: projectType,
  };
};

// API 调用函数
const fetchTenderDetail = async () => {
  const currentTab = route.query.currentTab;
  const encodedUrl = route.query.url;

  if (!encodedUrl) {
    showMessage.error('参数错误：未找到url');
    return;
  }

  const decodedUrl = decodeURIComponent(encodedUrl);

  try {
    let result;
    if (currentTab === 'projectPurchase') {
      const res = await getProjectPurchaseDetailByUrl(decodedUrl);
      if (res.code === 200) {
        result = processProjectPurchaseData(res.data);
      } else {
        showMessage.error(`${res.msg || '未知错误'}`);
        return;
      }
    } else if (currentTab === 'serviceMart') {
      const res = await getServiceMartDetailByUrl(decodedUrl);
      if (res.code === 200) {
        result = processServiceMartData(res.data);
      } else {
        showMessage.error(`${res.msg || '未知错误'}`);
        return;
      }
    } else {
      showMessage.error('未知的招标类型');
      return;
    }

    tenderDetail.value = result;
  } catch (err) {
    console.error('加载详情失败:', err);
    showMessage.error(`加载详情失败：${err.message || '请稍后重试'}`);
  }
};

// 生命周期钩子
onMounted(() => {
  fetchTenderDetail();
});
</script>

<style scoped>
.back-btn {
  @apply inline-block px-4 py-2 bg-primary text-white rounded-lg;
}

.detail-item {
  @apply mb-3 text-gray-700 flex items-start gap-2;
  /* 保持图标与文本顶部对齐 */
}

/* 关键：限制文本宽度，超出部分省略 */
.detail-item span {
  @apply max-w-[90%];
}

.url-link {
  @apply text-primary underline hover:text-secondary;
}
</style>