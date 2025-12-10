import request from '../request';

// 发送邮箱配置查询接口
export const getConfig = (params) => {
  return request.get('/messages/config', { params });
};

// 发送邮箱配置更新接口
export const updateConfig = (params) => {
  return request.put('/messages/config', params );
};
