<template>
    <header class="bg-white shadow-sm sticky top-0 z-50">
        <div class="container mx-auto px-3 py-3">
            <!-- 标题、刷新按钮和筛选切换按钮 -->
            <div class="flex items-center justify-between mb-3">
                <h1 class="text-lg font-bold">招标信息平台</h1>
                <div class="flex items-center gap-2">
                    <!-- 筛选切换按钮 -->
                    <button class="text-neutral hover:text-primary transition-colors p-2" @click="toggleFilters"
                        title="隐藏/显示筛选">
                        <i v-if="!showFilters" class="fa fa-filter"></i>
                        <i v-else class="fa fa-chevron-up"></i>
                        <!-- <i class="fa fa-filter"></i> -->
                    </button>

                    <!-- 刷新按钮 -->
                    <button class="text-neutral hover:text-primary transition-colors p-2" @click="$emit('refresh')"
                        title="刷新">
                        <i class="fa fa-refresh"></i>
                    </button>
                </div>
            </div>

            <!-- 搜索框（始终显示） -->
            <div class="mb-2">
                <div class="relative">
                    <input type="text" placeholder="搜索招标文件..."
                        class="w-full pl-4 pr-20 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-primary/30 focus:border-primary text-sm"
                        v-model="localSearchQuery" @keyup.enter="$emit('search', localSearchQuery)">
                    <!-- <i class="fa fa-search absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 text-sm"></i> -->
                    <button
                        class="absolute right-3 top-1/2 -translate-y-1/2 text-primary/70 hover:text-primary p-2 rounded-full transition-colors duration-200"
                        @click="$emit('search', localSearchQuery)">
                        <i class="fa fa-search text-xl"></i>
                        搜索
                    </button>
                </div>
            </div>

            <!-- 地区和日期筛选（根据状态显示/隐藏） -->
            <div v-if="showFilters" class="grid grid-cols-2 gap-2">
                <!-- 地区选择 -->
                <div class="relative">
                    <select
                        class="w-full pl-3 pr-8 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-primary/30 focus:border-primary appearance-none bg-white text-sm"
                        v-model="localRegion" @change="$emit('region-change', localRegion)">
                        <option value="">全部地区</option>
                        <option value="黑龙江省">黑龙江省</option>
                        <option value="绥化市">绥化市</option>
                        <option value="大庆市">大庆市</option>
                        <option value="哈尔滨市">哈尔滨市</option>
                        <option value="齐齐哈尔市">齐齐哈尔市</option>
                        <option value="牡丹江市">牡丹江市</option>
                        <option value="鸡西市">鸡西市</option>
                        <option value="鹤岗市">鹤岗市</option>
                        <option value="双鸭山市">双鸭山市</option>
                        <option value="伊春市">伊春市</option>
                        <option value="七台河市">七台河市</option>
                        <option value="黑河市">黑河市</option>
                        <option value="大兴安岭地区">大兴安岭地区</option>
                    </select>
                    <i
                        class="fa fa-chevron-down absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 text-xs pointer-events-none"></i>
                </div>

                <!-- 日期选择 -->
                <div class="relative">
                    <input type="date" v-model="localDate"
                        class="w-full pl-3 pr-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-primary/30 focus:border-primary text-sm"
                        @input="$emit('date-change', localDate)">
                </div>
            </div>
        </div>
    </header>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { formatDate } from '../../utils/format';

const props = defineProps({
    selectedRegion: { type: String, default: '' },
    searchQuery: { type: String, default: '' },
    selectedDate: { type: String, default: '' }
});

const emit = defineEmits(['refresh', 'search', 'region-change', 'date-change']);

// 控制筛选区域显示/隐藏的状态
const showFilters = ref(true); // 默认显示

// 内部状态与父组件同步
const localRegion = ref(props.selectedRegion);
const localSearchQuery = ref(props.searchQuery);
const localDate = ref(props.selectedDate);

// 切换筛选区域显示状态
const toggleFilters = () => {
    showFilters.value = !showFilters.value;
};

// 监听父组件状态变化
watch(() => props.selectedRegion, (newVal) => { localRegion.value = newVal; });
watch(() => props.searchQuery, (newVal) => { localSearchQuery.value = newVal; });
watch(() => props.selectedDate, (newVal) => { localDate.value = newVal; });

// 初始化默认日期
onMounted(() => {
    if (!props.selectedDate) {
        const today = new Date();
        localDate.value = formatDate(today);
    }
});
</script>

<style scoped>
/* 优化日期选择框样式 */
input[type="date"] {
    &::-webkit-calendar-picker-indicator {
        padding: 0 2px;
        cursor: pointer;
    }
}

/* 超小屏幕（如320px手机）进一步压缩间距 */
@media (max-width: 340px) {

    input,
    select {
        padding-top: 0.35rem !important;
        padding-bottom: 0.35rem !important;
    }
}
</style>