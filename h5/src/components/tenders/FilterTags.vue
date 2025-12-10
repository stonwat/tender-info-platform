<template>
    <div class="flex flex-wrap gap-2">
        <!-- 搜索标签 -->
        <span v-if="searchQuery" class="px-2 py-1 bg-primary/10 text-primary text-xs rounded-full flex items-center">
            搜索：<span class="ml-1">{{ searchQuery }}</span>
            <button class="ml-1 text-primary/70 hover:text-primary" @click="$emit('clear-search')">
                <i class="fa fa-times-circle"></i>
            </button>
        </span>
        <!-- 地区标签 -->
        <span v-if="selectedRegion" class="px-2 py-1 bg-primary/10 text-primary text-xs rounded-full flex items-center">
            地区：<span class="ml-1">{{ selectedRegion }}</span>
            <button class="ml-1 text-primary/70 hover:text-primary" @click="$emit('clear-region')">
                <i class="fa fa-times-circle"></i>
            </button>
        </span>

        <!-- 清除全部 -->
        <button v-if="selectedRegion || searchQuery" class="text-xs text-neutral hover:text-primary"
            @click="$emit('clear-all')">
            清除全部
        </button>
    </div>
</template>

<script setup>
const props = defineProps({
    selectedRegion: { type: String, default: '' },
    searchQuery: { type: String, default: '' }
});

const emit = defineEmits(['clear-region', 'clear-search', 'clear-all']);
</script>