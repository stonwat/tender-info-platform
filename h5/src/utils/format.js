/**
 * 格式化日期为 YYYY-MM-DD（列表页用）
 * @param {Date|string} date - 日期对象或字符串
 * @returns {string}
 */
export function formatDate(date) {
  if (!date) return '';
  if (typeof date === 'string' && date.includes('-')) return date;
  const d = new Date(date);
  return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;
}

/**
 * 格式化日期为 YYYY-MM-DD HH:mm:ss（详情页用）
 * @param {Date|string} date - 日期对象或字符串
 * @returns {string}
 */
export function formatDateTime(date) {
  if (!date) return '';
  const d = new Date(date);
  return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')} ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}:${d.getSeconds().toString().padStart(2, '0')}`;
}

/**
 * 格式化地区（如“绥化市市本级”→“绥化市”）
 * @param {string} region - 地区字符串
 * @returns {string}
 */
export function formatRegion(region) {
  if (!region) return '';
  if (region.endsWith('市本级')) {
    return region.replace('市本级', '市');
  }
  return region;
}

/**
 * 格式化区县（如“None”→“南岗区”）
 * @param {string} county - 区县字符串
 * @returns {string}
 */
export function formatCounty(county) {
  return county && county !== 'None' ? county : '';
}