<template>
  <div class="app-container">
    <SidebarNav @changeRouter="handleNavClick" @toggleCollapse="handleNavToggle" />
    <div class="contact-config-container">
      <!-- 发送邮箱配置区域 -->
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <span class="loading-text">加载中...</span>
      </div>
      <div class="email-config">
        <div class="email-title">
          发送邮箱配置
          <button class="btn success" @click="handleUpdateConfig">保存配置</button>
        </div>

        <div class="email-form">

          <div class="form-item">
            <label class="form-label">发送邮箱</label>
            <input class="form-input" v-model="configForm.sendEmail" placeholder="请输入发送邮箱地址">
          </div>
          <div class="form-item">
            <label class="form-label">邮箱名称</label>
            <input class="form-input" v-model="configForm.nickName" placeholder="请输入邮箱显示名称">
          </div>
          <div class="form-item">
            <label class="form-label">SMTP服务器</label>
            <input class="form-input" v-model="configForm.smtpServer" placeholder="例如: smtp.qq.com">
          </div>
          <div class="form-item">
            <label class="form-label">SMTP端口</label>
            <input class="form-input" v-model="configForm.smtpPort" placeholder="例如: 465">
          </div>
          <div class="form-item">
            <label class="form-label">邮箱授权码</label>
            <input class="form-input" v-model="configForm.grantCode" placeholder="请输入邮箱密码或授权码">
          </div>
        </div>
      </div>

      <!-- 联系人列表区域 -->
      <div class="contact-list-section">
        <div class="section-header">
          <h3 class="contact-title">联系人邮箱列表</h3>
          <div class="action-buttons">
            <button class="btn primary" @click="handleAddContact">添加联系人</button>
            <button class="btn danger" @click="handleDeleteContacts" :disabled="!hasSelected">删除选中</button>
          </div>
        </div>
        <table class="contact-table">
          <thead>
            <tr>
              <th class="checkbox-column">
                <input type="checkbox" @change="handleSelectAll" :checked="isAllSelected">
              </th>
              <th style="width:100px">联系人名称</th>
              <th style="width:140px">邮箱地址</th>
              <th style="width:100px">备注</th>
              <th style="width:100px">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, index) in contactList" :key="row.userId">
              <td class="checkbox-column">
                <input type="checkbox" :checked="selectedContacts.includes(row)"
                  @change="(e) => handleRowCheck(row, e.target.checked)">
              </td>
              <td>{{ row.userName }}</td>
              <td>{{ row.email }}</td>
              <td>{{ row.remarks }}</td>
              <td class="action-column">
                <button class="btn text" @click="handleUpdateContact(row)">更新</button>
                <button class="btn text danger" @click="handleDeleteContact(row)">删除</button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 添加/更新联系人弹窗 -->
        <div class="dialog-mask" v-show="dialogVisible">
          <div class="dialog">
            <div class="dialog-header">
              <h2>{{ dialogTitle }}</h2>
              <button class="dialog-close" @click="dialogVisible = false">×</button>
            </div>
            <div class="dialog-body">
              <form class="dialog-form" ref="dialogFormRef">
                <div class="form-item">
                  <label class="form-label">联系人名称</label>
                  <input class="form-input" v-model="dialogForm.userName" placeholder="请输入联系人名称">
                  <p class="error-message" v-if="dialogErrors.name">
                    {{ dialogErrors.name }}
                  </p>
                </div>
                <div class="form-item">
                  <label class="form-label">邮箱地址</label>
                  <input class="form-input" v-model="dialogForm.email" placeholder="请输入邮箱地址">
                  <p class="error-message" v-if="dialogErrors.email">
                    {{ dialogErrors.email }}
                  </p>
                </div>
                <div class="form-item">
                  <label class="form-label">备注</label>
                  <textarea class="form-textarea" v-model="dialogForm.remarks" :rows="3"></textarea>
                </div>
              </form>
            </div>
            <div class="dialog-footer">
              <button class="btn default" @click="dialogVisible = false">取消</button>
              <button class="btn primary" v-show="addCotactVisible" @click="confirmAddContact()">提交</button>
              <button class="btn primary" v-show="updateCotactVisible" @click="confirmUpdateContact()">确认</button>
            </div>
          </div>
        </div>

        <!-- 确认删除对话框容器 -->
        <div class="confirm-mask" v-if="dialogDelVisible">
          <div class="confirm-dialog">
            <div class="confirm-header">
              <h3>{{ confirmDeleteTitle }}</h3>
              <button class="confirm-close" @click="handleCancle">×</button>
            </div>
            <div class="confirm-body">
              {{ dialogDelContent.text }}
            </div>
            <div class="confirm-footer">
              <button class="btn " @click="handleCancle">取消</button>
              <button class="btn danger" v-show="deleteContactVisible" @click="confirmDeleteContact">确定</button>
              <button class="btn danger" v-show="deleteContactsVisible" @click="confirmDeleteContacts">确定</button>
            </div>
          </div>
        </div>

        <!-- 分页控件 -->
        <div class="pagination-container">
          <div class="pagination-info">
            共 {{ totalContacts }} 条，当前第 {{ currentPage }} 页
          </div>
          <div class="pagination-controls">
            <button class="btn" style="color: black;"
              @click="currentPage > 1 && handleCurrentChange(currentPage - 1)">上一页</button>
            <button class="btn" style="color: black;"
              @click="currentPage < totalPages && handleCurrentChange(currentPage + 1)">下一页</button>
            <select class="page-size-select" @change="handlePageSizeChange">
              <option :value="size" v-for="size in [10, 20, 50]" :key="size">
                {{ size }} 条/页
              </option>
            </select>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script setup>
import { ref, computed, inject } from 'vue';
import SidebarNav from '../../components/messages/SidebarNav.vue';
import { getConfig, updateConfig } from '../../api/messages/config';
import { getContactList, addContact, updateContact, deleteContact, deleteContacts } from '../../api/messages/contact';

const showMessage = inject('showMessage');

// 处理导航点击
const handleNavClick = (item) => { };

// 处理导航折叠状态变化
const handleNavToggle = (isCollapsed) => { };

// 加载状态控制
const loading = ref('false');

// 分页控件
const currentPage = ref(1);
const pageSize = ref(10);
const totalContacts = ref(0);
const totalPages = ref(0);
const selectedContacts = ref([]);

// 获取发送邮箱配置
const configForm = ref({});
const fetchConfig = async () => {
  try {
    loading.value = true; // 开始加载
    const res = await getConfig();
    configForm.value = res.data[0];
  } catch (error) {
    console.error(error);
    showMessage.error('获取信息失败，请稍后重试1');
  } finally {
    setTimeout(() => {
      loading.value = false;
    },500);
  }
};
fetchConfig();

// 更新发送邮箱配置
const handleUpdateConfig = async () => {
  const configData = {
    id: configForm.value.id, // 从现有配置中获取ID
    sendEmail: configForm.value.sendEmail,
    nickName: configForm.value.nickName,
    smtpServer: configForm.value.smtpServer,
    smtpPort: configForm.value.smtpPort,
    grantCode: configForm.value.grantCode
  };
  try {
    const res = await updateConfig(configData);
    if (res.code === 200) { // 假设 200 代表成功
      showMessage.success('配置更新成功！');
      fetchConfig(); // 重新查询最新配置
    } else {
      // 处理业务错误（有明确错误信息）
      showMessage.error(`更新失败：${res.msg || '未知错误'}`);
    }
  } catch (error) {
    if (error.response?.data?.message) {
      // showMessage.error(`更新失败：${error.response.data.message}`);
    } else {
      // showMessage.error('网络异常，请稍后重试');
    }
  }
};

/**
 * 联系人邮箱列表相关
 */
// 获取联系人邮箱列表
const contactList = ref([]);
const fetchContactList = async () => {
  try {
    loading.value = true; // 开始加载
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value,
    };
    const res = await getContactList(params);
    if (res.code === 200) { // 假设 200 代表成功
      contactList.value = res.data.content.map(item => {
        return {
          ...item,
          userId: item.userId,
          userName: item.userName,
        };
      });
      totalContacts.value = res.data.totalElements;
      totalPages.value = Math.ceil(totalContacts.value / pageSize.value);
    } else {
      // 处理业务错误（有明确错误信息）
      showMessage.error(`获取信息失败：${res.msg || '未知错误'}`);
    }
  } catch (error) {
    showMessage.error('获取信息失败，请稍后重试');
  } finally {
    setTimeout(() => {
      loading.value = false;
    },500)
  }
};
fetchContactList();

// 添加/更新联系人弹窗相关
const dialogVisible = ref(false);
const addCotactVisible = ref(false);// 添加联系人显示
const updateCotactVisible = ref(false);// 更新联系人显示
const dialogTitle = ref('添加联系人');
const dialogForm = ref({
  userId: null,
  userName: '',
  email: '',
  remarks: ''
});
const dialogErrors = ref({
  name: '',
  email: ''
});
// 确认删除对话框实现
const dialogDelVisible = ref(false);
const deleteContactVisible = ref(false);
const deleteContactsVisible = ref(false);
const confirmDeleteTitle = ref('提示');
const dialogDelContent = ref({
  text: '',
  userId: null
});

// 添加联系人邮箱操作
const handleAddContact = () => {
  updateCotactVisible.value = false;
  addCotactVisible.value = true;
  dialogTitle.value = '添加联系人';
  dialogForm.value = { id: null, name: '', email: '', remark: '' };
  dialogErrors.value = { name: '', email: '' };
  dialogVisible.value = true;
};
// 确认添加联系人邮箱
const confirmAddContact = async () => {
  const addContactData = {
    userName: dialogForm.value.userName,
    email: dialogForm.value.email,
    remarks: dialogForm.value.remarks || ''
  };
  try {
    const res = await addContact(addContactData);
    if (res.code == 200) {
      showMessage.success('添加联系人邮箱成功！');
      dialogVisible.value = false;
      fetchContactList();
    } else {
      showMessage.error(`添加联系人邮箱失败：${res.msg || '未知错误'}`);
    }

  } catch (error) {
    showMessage.error(`网络异常`);
  }
};
// 更新联系人邮箱操作
const handleUpdateContact = (row) => {
  addCotactVisible.value = false;
  updateCotactVisible.value = true;
  dialogTitle.value = '更新联系人';
  dialogForm.value = { ...row };
  dialogErrors.value = { name: '', email: '' };
  dialogVisible.value = true;
};
// 确认更新联系人邮箱
const confirmUpdateContact = async () => {
  const userId = dialogForm.value.userId;
  const updateContactData = {
    userName: dialogForm.value.userName,
    email: dialogForm.value.email,
    remarks: dialogForm.value.remarks || ''
  };
  try {
    const res = await updateContact(userId, updateContactData);
    if (res.code == 200) {
      showMessage.success('更新联系人邮箱成功！');
      dialogVisible.value = false;
      fetchContactList();
    } else {
      showMessage.error(`更新联系人邮箱失败：${res.msg || '未知错误'}`);
    }
  } catch (error) {
    showMessage.error(`更新失败`);
  }
};
// 删除联系人邮箱操作
const handleDeleteContact = (row) => {
  dialogDelVisible.value = true;
  deleteContactVisible.value = true;
  deleteContactsVisible.value = false;
  dialogDelContent.value.text = `确定要删除联系人"${row.userName}"吗?`;
  dialogDelContent.value.userId = row.userId;
};
// 确认删除联系人邮箱
const confirmDeleteContact = async () => {
  const userId = dialogDelContent.value.userId;
  try {
    const res = await deleteContact(userId);
    if (res.code == 200) {
      showMessage.success('删除联系人邮箱成功！');
      fetchContactList();
    } else {
      showMessage.error(`删除联系人邮箱失败：${res.msg || '未知错误'}`);
    }
  } catch (error) {
    showMessage.error(`删除失败`);
  } finally {
    dialogDelVisible.value = false;
  }
};

// 删除选中/批量删除
const handleSelectAll = (e) => {// 全选
  if (e.target.checked) {
    selectedContacts.value = [...contactList.value];
    console.log(selectedContacts.value);
  } else {
    selectedContacts.value = [];
  }
};
const handleRowCheck = (row, isChecked) => {// 单行选中
  if (isChecked) {
    selectedContacts.value.push(row);
    console.log(selectedContacts.value);
  } else {
    selectedContacts.value = selectedContacts.value.filter(item => item.id !== row.id);
  }
};
// 计算处理联系人邮箱列表选中状态
const hasSelected = computed(() => selectedContacts.value.length > 0);

const isAllSelected = computed(() => {
  return (
    contactList.value.length > 0 &&
    selectedContacts.value.length === contactList.value.length
  );
});
// 删除选中联系人邮箱操作
const handleDeleteContacts = () => {
  dialogDelVisible.value = true;
  deleteContactVisible.value = false;
  deleteContactsVisible.value = true;
  dialogDelContent.value.text = `确定要删除选中的${selectedContacts.value.length}个联系人吗?`;
};
const confirmDeleteContacts = async () => {
  const userIds = ref([]);
  userIds.value = selectedContacts.value.map(item => item.userId);
  console.log(userIds.value);
  try {
    const res = await deleteContacts(userIds.value);
    if (res.code == 200) {
      showMessage.success('删除选中的联系人成功！');
      fetchContactList();
    } else {
      showMessage.error(`删除选中的联系人失败：${res.msg} || '未知错误'`);
    }
  } catch (error) {
    showMessage.error(`删除失败`);
  } finally {
    dialogDelVisible.value = false;
  }
};

// 弹窗/对话框取消按钮
const handleCancle = () => {
  dialogVisible.value = false;
  dialogDelVisible.value = false;
  deleteContactVisible.value = false;
  deleteContactsVisible.value = false;

};

/**
 * 分页控件相关
 */
// 页码切换
const handlePageSizeChange = (e) => {
  pageSize.value = parseInt(e.target.value);
  currentPage.value = 1;
  fetchContactList();
};
// 页号切换
const handleCurrentChange = (val) => {
  currentPage.value = val;
  selectedContacts.value = [];
  fetchContactList();
};
</script>

<style scoped>
/* 基础布局属性 - 全局基础 */
html {
  font-size: 14px;
}

.app-container {
  display: flex;
}

/* 加载动画样式 */
.loading {
  position: absolute;
  top: 0;
  left: 220px;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100px;
  padding: 2rem;

  background-color: rgba(255, 255, 255, 0.7);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.loading-text {
  margin-left: 10px;
  color: #666;
  font-size: 14px;
}

/* 基础布局属性 - 容器 */
.contact-config-container {
  padding: 1rem;
  min-height: calc(100vh - 2rem);
}

/* 视觉表现属性 - 容器 */
.contact-config-container {
  background-color: #f5f7fa;
}

/* 基础布局属性 - 标签页 */
.tabs {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.tab-item {
  padding: 0.5rem 1rem;
}

/* 视觉表现属性 - 标签页 */
.tab-item {
  border: 1px solid #e6e6e6;
  border-radius: 4px;
}

.tab-item.active {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
}

/* 基础布局属性 - 邮箱配置 */
.email-config {
  min-height: 30vh;
}

/* 视觉表现属性 - 邮箱配置 */
.email-config {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.email-title {
  display: flex;
  justify-content: space-between;
  padding: 10px 20px 0 12px;
}

/* 视觉表现属性 - 邮箱标题 */
.email-title {
  color: #333;
  font-size: 20px;
  font-weight: 600;
}

/* 基础布局属性 - 邮箱表单 */
.email-form {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: space-between;
  padding: 1rem;
  padding-top: 0.4rem;
  margin-bottom: 1rem;
}

/* 基础布局属性 - 表单项 */
.form-item {
  display: flex;
  align-items: center;
  width: 100%;
  gap: 0.8rem;
}

.form-label {
  width: 100px;
  text-align: right;
  flex-shrink: 0;
}

/* 视觉表现属性 - 表单项文字 */
.form-label {
  font-size: 1rem;
  color: #666;
}

/* 基础布局属性 - 表单输入 */
.form-input,
.form-textarea {
  padding: 0.5rem 0.8rem;
  flex-grow: 1;
}

/* 视觉表现属性 - 表单输入 */
.form-input,
.form-textarea {
  border: 1px solid #dcdcdc;
  border-radius: 4px;
}

/* 基础布局属性 - 列表区域 */
.contact-list-section {
  min-height: calc(70vh - 60px);
  padding: 1rem;
  margin-top: 1rem;
}

/* 视觉表现属性 - 列表区域 */
.contact-list-section {
  background: #ffffff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  border: 1px solid #e6e6e6;
  border-radius: 4px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

/* 视觉表现属性 - 列表标题 */
.contact-title {
  color: #333;
  font-size: 20px;
  font-weight: 600;
}

/* 基础布局属性 - 操作按钮组 */
.action-buttons {
  display: flex;
  gap: 1rem;
}

/* 基础布局属性 - 表格 */
.contact-table {
  width: 100%;
}

.contact-table th,
.contact-table td {
  padding: 0.5rem;
  text-align: center;
}

/* 视觉表现属性 - 表格边框 */
.contact-table {
  border-collapse: collapse;
}

.contact-table th,
.contact-table td {
  border: 1px solid #e6e6e6;
}

.checkbox-column,
.action-column {
  width: 20px;
}

/* 基础布局属性 - 分页 */
.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 0.2rem;
}

/* 基础布局属性 - 弹窗遮罩 */
.dialog-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 视觉表现属性 - 弹窗遮罩 */
.dialog-mask {
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 1000;
}

/* 基础布局属性 - 弹窗容器 */
.dialog {
  width: 90%;
  max-width: 600px;
  overflow: hidden;
}

/* 视觉表现属性 - 弹窗容器 */
.dialog {
  background-color: #ffffff;
  border-radius: 6px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
}

/* 视觉表现属性 - 弹窗头部边框 */
.dialog-header {
  border-bottom: 1px solid #e6e6e6;
}

.dialog-body {
  padding: 20px;
}

.dialog-form {
  width: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
  padding: 10px 20px;
}

/* 视觉表现属性 - 弹窗底部边框 */
.dialog-footer {
  border-top: 1px solid #e6e6e6;
}

.dialog-close {
  font-size: 18px;
}

/* 视觉表现属性 - 弹窗关闭按钮 */
.dialog-close {
  background: transparent;
  border: none;
  color: #999;
}

/* 动态交互属性 - 弹窗关闭按钮 */
.dialog-close {
  cursor: pointer;
}

.error-message {
  margin: 0;
  padding-top: 4px;
  font-size: 12px;
}

/* 视觉表现属性 - 错误提示 */
.error-message {
  color: #f56c6c;
}

.message-item {
  padding: 12px 20px;
  margin-bottom: 8px;
  border-radius: 4px;
  color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  opacity: 0;
  transform: translateX(100%);
  transition: opacity 0.3s, transform 0.3s;
}

/* 动态交互属性 - 消息动画 */
.message-item.show {
  opacity: 1;
  transform: translateX(0);
}

/* 视觉表现属性 - 消息颜色 */
.message-item.success {
  background-color: #52c41a;
}

.message-item.error {
  background-color: #f56c6c;
}

.message-item.info {
  background-color: #409eff;
}

/* 基础布局属性 - 确认弹窗遮罩 */
.confirm-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 视觉表现属性 - 确认弹窗遮罩 */
.confirm-mask {
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 1001;
}

/* 基础布局属性 - 确认弹窗容器 */
.confirm-dialog {
  width: 90%;
  max-width: 400px;
}

/* 视觉表现属性 - 确认弹窗容器 */
.confirm-dialog {
  background-color: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.confirm-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
}

/* 视觉表现属性 - 确认弹窗头部边框 */
.confirm-header {
  border-bottom: 1px solid #e6e6e6;
}

.confirm-body {
  padding: 20px;
}

/* 视觉表现属性 - 确认弹窗内容 */
.confirm-body {
  color: #666;
}

.confirm-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 10px 20px;
}

/* 视觉表现属性 - 确认弹窗底部边框 */
.confirm-footer {
  border-top: 1px solid #e6e6e6;
}

.confirm-close {
  font-size: 18px;
}

/* 视觉表现属性 - 确认弹窗关闭按钮 */
.confirm-close {
  background: transparent;
  border: none;
  color: #999;
}

/* 动态交互属性 - 确认弹窗关闭按钮 */
.confirm-close {
  cursor: pointer;
}

/* 基础布局属性 - 按钮 */
.btn {
  padding: 0.5rem 0.6rem;
  border: none;
  border-radius: 4px;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s;
}

/* 视觉表现属性 - 按钮颜色 */
.btn.default {
  background-color: #6b7280;
}

.btn.primary {
  background-color: #409eff;
}

.btn.primary:hover {
  background-color: #66b1ff;
}

.btn.danger {
  background-color: #f56c6c;
}

.btn.danger:hover {
  background-color: #f78989;
}

.btn.success {
  background-color: #52c41a;
}

.btn.success:hover {
  background-color: #73d13d;
}

.btn.text {
  background: transparent;
  color: #409eff;
  font-size: 15px;
}

.btn.text:hover {
  background-color: #f0f7ff;
}

/* 视觉表现属性 - 禁用按钮 */
.btn:disabled {
  opacity: 0.6;
}

/* 动态交互属性 - 禁用按钮 */
.btn:disabled {
  cursor: not-allowed;
}

/* 响应式适配（小屏幕单列布局） */
@media (max-width: 768px) {
  .form-item {
    width: 100%;
  }
}
</style>
