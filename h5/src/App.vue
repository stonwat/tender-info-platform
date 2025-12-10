<template>
  <div id="app">
    <router-view /> <!-- 路由出口，显示页面内容 -->
    <Message ref="message" /> <!-- 消息组件容器 -->
  </div>
</template>

<script setup>
import { ref, onMounted, provide } from 'vue';
import Message from './components/base/Message.vue';

const message = ref(null);

// 全局提供消息方法，供子组件调用
onMounted(() => {
  provide('showMessage', message.value.showMessage);
});
</script>

<style>
</style>