import request from '../request';

// 项目采购招标列表接口
export const getProjectPurchaseList = (params) => {
  return request.get('/tenders/project', { params });
};

// 项目采购详情接口
export const getProjectPurchaseDetailByUrl = (url) => {
  return request.get('/tenders/project/detail', { params: { url } });
};
