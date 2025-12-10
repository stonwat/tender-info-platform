import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import Message from './components/base/Message.vue';// 全局引用封装的消息提示组件

// 引入 Tailwind（确保 tailwind.config.js 正确）
import './assets/styles/tailwind.css'

// 引入 font-awesome 样式
import 'font-awesome/css/font-awesome.min.css'

const app = createApp(App);
app.use(router);
app.mount('#app');

app.component('Message', Message);