import request from '../request';

// 新增联系人邮箱接口
export const getContactList = (params) => {
  return request({
    url: '/messages/contact',
    method: 'get',
    params
  });
};

// 新增联系人邮箱接口
export const addContact = (data) => {
  return request({
    url: '/messages/contact',
    method: 'post',
    data: data
  });
};

// 更新联系人邮箱接口
export const updateContact = (userId, data) => {
  return request({
    url: `/messages/contact/${userId}`,
    method: 'put',
    data: data
  });
};

// 删除联系人邮箱接口
export const deleteContact = (userId) => {
  return request({
    url: `/messages/contact/${userId}`,
    method: 'delete'
  });
};

// 删除选中联系人邮箱，可批量删除接口
export const deleteContacts = (userIds) => {
  return request({
    url: '/messages/contact',
    method: 'delete',
    data: userIds  // 注意：批量删除使用request body传递ID列表
  });
};
    