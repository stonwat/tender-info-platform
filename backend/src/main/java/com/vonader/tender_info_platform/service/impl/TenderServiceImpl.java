package com.vonader.tender_info_platform.service.impl;

import com.vonader.tender_info_platform.common.ErrorCode;
import com.vonader.tender_info_platform.domain.ProjectPurchase;
import com.vonader.tender_info_platform.domain.ServiceMart;
import com.vonader.tender_info_platform.exception.BusinessException;
import com.vonader.tender_info_platform.repository.ProjectPurchaseRepository;
import com.vonader.tender_info_platform.repository.ServiceMartRepository;

import com.vonader.tender_info_platform.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;


@Service
public class TenderServiceImpl implements TenderService {

    private final ServiceMartRepository serviceMartRepository;

    private final ProjectPurchaseRepository projectPurchaseRepository;

    @Autowired
    public TenderServiceImpl(ProjectPurchaseRepository projectPurchaseRepository,
                             ServiceMartRepository serviceMartRepository) {
        this.serviceMartRepository = serviceMartRepository;
        this.projectPurchaseRepository = projectPurchaseRepository;
    }

    public Page<ProjectPurchase> getFilteredProjectPurchase(
            String region,
            String keyword,
            String date,
            Pageable pageable) {
        // 处理空值情况
        region = "".equals(region) ? null : region;
        keyword = "".equals(keyword) ? null : keyword;
        date = "".equals(date) ? null : date;
        return projectPurchaseRepository.findFilteredProjectPurchase(
                region,
                keyword,
                date,
                pageable
        );
    }

    // 根据url查询项目采购详情
    public ProjectPurchase getProjectPurchaseDetailByUrl(String url) {
        return projectPurchaseRepository.findProjectPurchaseDetailByUrl(url)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "未找到项目采购信息"));
    }

    /**
     * 过滤查询：处理空参数，调用 Repository
     */
    public Page<ServiceMart> getFilteredServiceMart(
            String region,
            String keyword,
            String date,
            Pageable pageable) {

        // 将空字符串转为 null，适配 JPQL 的 IS NULL 判断
        region = region.isEmpty() ? null : region;
        keyword = keyword.isEmpty() ? null : keyword;
        date = date.isEmpty() ? null : date;

        return serviceMartRepository.findFilteredServiceMart(
                region, keyword, date, pageable
        );
    }

    /**
     * 按 url 查询详情
     */
    public ServiceMart getServiceMartTenderByUrl(String url) {
        return serviceMartRepository.findServiceMartDetailByUrl(url)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "未找到服务工程信息"));
    }

}