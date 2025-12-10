/**
 * 表单校验工具类
 * 包含常用的表单字段校验方法
 */
export const Validator = {
  /**
   * 校验联系人名称
   * @param {string} name - 联系人名称
   * @returns {string} 错误信息（空字符串表示校验通过）
   */
  validateName(name) {
    if (!name?.trim()) { // 处理undefined/null和空字符串
      return '请输入联系人名称'
    }
    return ''
  },

  /**
   * 校验邮箱格式
   * @param {string} email - 邮箱地址
   * @returns {string} 错误信息（空字符串表示校验通过）
   */
  validateEmail(email) {
    if (!email) {
      return '请输入邮箱地址'
    }
    const emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
    if (!emailReg.test(email)) {
      return '请输入正确的邮箱格式'
    }
    return ''
  },

  // 可扩展其他校验方法，如手机号、必填项等
  /**
   * 校验必填项
   * @param {string} value - 待校验值
   * @param {string} fieldName - 字段名称（用于错误提示）
   * @returns {string} 错误信息
   */
  validateRequired(value, fieldName) {
    if (!value?.toString().trim()) {
      return `请输入${fieldName}`
    }
    return ''
  }
}