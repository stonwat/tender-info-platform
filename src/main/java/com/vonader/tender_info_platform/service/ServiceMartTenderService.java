package com.vonader.tender_info_platform.service;

import com.vonader.tender_info_platform.domain.ServiceMartTender;
import com.vonader.tender_info_platform.repository.ServiceMartTenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ServiceMartTenderService {

    private final ServiceMartTenderRepository serviceMartTenderRepository;

    @Autowired
    public ServiceMartTenderService(ServiceMartTenderRepository serviceMartTenderRepository) {
        this.serviceMartTenderRepository = serviceMartTenderRepository;
    }

    /**
     * 过滤查询：处理空参数，调用 Repository
     */
    public Page<ServiceMartTender> getFilteredServiceMartTenders(
            String region,
            String keyword,
            String date,
            Pageable pageable) {

        // 将空字符串转为 null，适配 JPQL 的 IS NULL 判断
        region = region.isEmpty() ? null : region;
        keyword = keyword.isEmpty() ? null : keyword;
        date = date.isEmpty() ? null : date;

        return serviceMartTenderRepository.findFilteredServiceMartTenders(
                region, keyword, date, pageable
        );
    }

    /**
     * 按 url 查询详情
     */
    public ServiceMartTender getServiceMartTenderByUrl(String url) {
        return serviceMartTenderRepository.findByUrl(url)
                .orElse(null); // 未找到返回 null
    }
}