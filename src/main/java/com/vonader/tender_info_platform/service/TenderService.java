package com.vonader.tender_info_platform.service;

import com.vonader.tender_info_platform.domain.ProjectPurchase;
import com.vonader.tender_info_platform.repository.TenderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class TenderService {

    private final TenderRepository tenderRepository;

    @Autowired
    public TenderService(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
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

        return tenderRepository.findFilteredProjectPurchase(
                region,
                keyword,
                date,
                pageable
        );
    }

    // 根据url查询
    public ProjectPurchase getProjectPurchaseDetailByUrl(String url) {
        // 调用Repository方法（需在Repository中定义）
        return tenderRepository.findProjectPurchaseDetailByUrl(url)
                .orElse(null); // 未找到返回null
    }

}