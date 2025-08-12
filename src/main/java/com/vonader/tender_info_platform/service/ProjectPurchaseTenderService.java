package com.vonader.tender_info_platform.service;

import com.vonader.tender_info_platform.domain.ProjectPurchaseTender;
import com.vonader.tender_info_platform.repository.ProjectPurchaseTenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProjectPurchaseTenderService {

    private final ProjectPurchaseTenderRepository tenderRepository;

    @Autowired
    public ProjectPurchaseTenderService(ProjectPurchaseTenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public Page<ProjectPurchaseTender> getFilteredTenders(
            String region,
            String keyword,
            String date,
            Pageable pageable) {

        // 处理空值情况
        region = "".equals(region) ? null : region;
        keyword = "".equals(keyword) ? null : keyword;
        date = "".equals(date) ? null : date;

        return tenderRepository.findFilteredTenders(
                region,
                keyword,
                date,
                pageable
        );
    }

    // 根据url查询
    public ProjectPurchaseTender getTenderByUrl(String url) {
        // 调用Repository方法（需在Repository中定义）
        return tenderRepository.findByUrl(url)
                .orElse(null); // 未找到返回null
    }

}