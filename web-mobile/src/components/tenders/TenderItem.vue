<template>
  <li class="bg-white rounded-xl shadow-sm overflow-hidden card-hover fade-in relative"
    :style="{ animationDelay: `${index * 0.05}s` }">
    <router-link :to="{
      name: 'TenderDetail',
      query: {
        currentTab: currentTab,
        url: tender.url,
        region: $route.query.region || '',
        keyword: $route.query.keyword || '',
        date: $route.query.date || ''
      }
    }" class="block p-4 relative">

      <!-- 小角标数字（左上角） -->
      <!-- <div class="serial-badge">{{ serialNumber }}</div> -->

      <!-- 右上角日期 -->
      <div class="absolute bottom-2 right-10 text-xs text-neutral px-1 py-1 rounded">
        {{ tender.date }}
      </div>

      <div class="flex items-start gap-3">
        <div class="bg-primary/10 text-primary p-2 rounded-lg">
          <i class="fa fa-file-text-o text-lg"></i>
        </div>
        <div class="flex-grow">
          <h3 class="font-medium text-base mb-1 line-clamp-2">{{ tender.title }}</h3>
          <div class="flex flex-wrap items-center gap-x-3 gap-y-1 text-xs text-neutral 
            sm:flex-col sm:gap-y-2 sm:items-start">
            <span class="w-60 flex-shrink-0"><i class="fa fa-money text-primary mr-1 w-4 inline-block"></i>预算：{{
              tender.budget }}</span>
            <!-- <span class="w-60 flex-shrink-0"><i class="fa fa-list-alt text-primary mr-1 w-4 inline-block"></i>标的：{{
              tender.matter }}</span> -->
            <span class="w-60 flex-shrink-0">
              <i class="fa fa-map-marker text-primary mr-1 w-4 inline-block"></i>
              地区：{{formatRegion(tender?.region) || '——' }} {{ formatCounty(tender?.county) || '' }}</span>
          </div>
        </div>

        <div class="absolute bg-stone-100 bottom-2 right-2 text-black px-2 py-1 rounded-full text-xs font-bold">
          {{ serialNumber }}
        </div>
        <i class="fa fa-angle-right text-neutral self-center"></i>
      </div>
    </router-link>
  </li>
</template>

<script setup>

import { customRef } from 'vue';
import { formatRegion, formatCounty } from '../../utils/format';

const props = defineProps({
  tender: Object,
  index: Number,
  currentTab: String,
  serialNumber: Number
});

</script>

<style>
/* 淡入动画样式定义 */
.fade-in {
  animation: fadeIn 0.3s ease-in-out;
  /* 应用动画：名称、时长、缓动函数 */
}

/* 定义动画关键帧：从透明+位移到完全显示 */
@keyframes fadeIn {
  from {
    opacity: 0;
    /* 初始状态：完全透明 */
    transform: translateY(10px);
    /* 初始状态：向下偏移10px（增强动画感） */
  }

  to {
    opacity: 1;
    /* 结束状态：完全不透明 */
    transform: translateY(0);
    /* 结束状态：回到原位置 */
  }
}

/* 可选：为 hover 效果添加过渡（如果之前没有） */
.card-hover {
  transition: all 0.3s ease;
}

.card-hover:hover {
  transform: translateY(-2px);
  /*  hover 时轻微上浮，增强交互感 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  /* 增强阴影 */
}
</style>