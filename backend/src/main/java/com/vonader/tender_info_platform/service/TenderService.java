package com.vonader.tender_info_platform.service;

import com.vonader.tender_info_platform.domain.ProjectPurchase;
import com.vonader.tender_info_platform.domain.ServiceMart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 招标相关服务接口，定义项目采购和服务超市的业务方法
 */
public interface TenderService {

    /**
     * 带条件分页查询项目采购信息
     * @param region 地区筛选条件（可为空）
     * @param keyword 关键词筛选（可为空）
     * @param date 日期筛选条件（可为空）
     * @param pageable 分页参数对象
     * @return 分页形式的项目采购数据
     * @throws IllegalArgumentException 当分页参数为空时抛出
     */
    Page<ProjectPurchase> getFilteredProjectPurchase(String region, String keyword, String date, Pageable pageable);

    /**
     * 根据URL查询项目采购详情
     * @param url 项目采购详情页URL
     * @return 对应的项目采购详情对象，若未找到则返回null
     * @throws IllegalArgumentException 当URL为空或空白时抛出
     */
    ProjectPurchase getProjectPurchaseDetailByUrl(String url);

    /**
     * 带条件分页查询服务超市信息
     * @param region 地区筛选条件（可为空）
     * @param keyword 关键词筛选（可为空）
     * @param date 日期筛选条件（可为空）
     * @param pageable 分页参数对象
     * @return 分页形式的服务超市数据
     * @throws IllegalArgumentException 当分页参数为空时抛出
     */
    Page<ServiceMart> getFilteredServiceMart(String region, String keyword, String date, Pageable pageable);

    /**
     * 根据URL查询服务超市详情
     * @param url 服务超市详情页URL
     * @return 对应的服务超市详情对象，若未找到则返回null
     * @throws IllegalArgumentException 当URL为空或空白时抛出
     */
    ServiceMart getServiceMartTenderByUrl(String url);
}
