<template>
    <div class="message-container">
        <!-- 消息列表，通过v-for渲染 -->
        <div v-for="msg in messages" :key="msg.id" class="message-item" :class="msg.type"
            :style="{ opacity: msg.show ? 1 : 0, transform: msg.show ? 'translateY(0)' : 'translateY(-20px)' }">
            {{ msg.content }}
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

// 消息列表（存储所有消息）
const messages = ref([]);
let msgId = 0; // 用于生成唯一key

// 对外暴露的显示消息方法
const showMessage = (content, type = 'success') => {
    // 生成唯一ID
    const id = ++msgId;
    // 添加消息到列表（初始隐藏）
    messages.value.push({
        id,
        content,
        type,
        show: false
    });

    // 显示动画（延迟触发，确保DOM已更新）
    setTimeout(() => {
        const msg = messages.value.find(m => m.id === id);
        if (msg) msg.show = true;
    }, 10);

    // 3秒后隐藏并移除
    setTimeout(() => {
        const msg = messages.value.find(m => m.id === id);
        if (msg) msg.show = false;
        // 等待动画结束后从列表中删除
        setTimeout(() => {
            messages.value = messages.value.filter(m => m.id !== id);
        }, 300);
    }, 3000);
};

// 扩展失败提示的快捷方法
showMessage.success = (content) => showMessage(content, 'success');
showMessage.error = (content) => showMessage(content, 'error'); // 新增失败类型

// 暴露方法供全局调用
defineExpose({ showMessage });
</script>

<style scoped>
.message-container {
    position: fixed;
    top: 20px;
    left: 50%;
    /* 水平居中 */
    transform: translateX(-50%);
    /* 配合left:50%实现水平居中 */
    z-index: 9999;
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.message-item {
    padding: 12px 20px;
    border-radius: 4px;
    color: #fff;
    font-size: 14px;
    transition: all 0.3s ease;
    opacity: 0;
    transform: translateY(-20px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

/* 成功类型样式 */
.message-item.success {
    background-color: #52c41a;
    /* 绿色 */
}

/* 失败类型样式 */
.message-item.error {
    background-color: #f5222d;
    /* 红色 */
}
</style>