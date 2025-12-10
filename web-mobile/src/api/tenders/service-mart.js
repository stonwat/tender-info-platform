import request from '../request';

// 服务工程招标列表接口
export const getServiceMartList = (params) => {
  return request.get('/tenders/service', { params });
};

// 服务工程详情接口（若有）
export const getServiceMartDetailByUrl = (url) => {
  return request.get('/tenders/service/detail', { params: { url } });
};