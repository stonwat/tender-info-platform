import axios from 'axios';

// 从环境变量读取API域名
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://vonader.cn:8080/tender-info-platform-api';

// 创建axios实例
const request = axios.create({
  baseURL: apiBaseUrl,
  timeout: 5000, // 超时时间
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器：添加token和请求处理
request.interceptors.request.use(
  (config) => {
    // 从本地存储获取token并添加到请求头
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    // 处理请求发送失败
    console.error('请求发送失败:', error.message);
    // 移动端可使用自定义toast提示
    alert('网络异常，请检查连接');
    return Promise.reject(error);
  }
);

// 响应拦截器：处理响应数据和错误
request.interceptors.response.use(
  (response) => {
    // 直接返回响应体中的data部分（简化前端调用）
    return response.data;
  }
);

export default request;
    