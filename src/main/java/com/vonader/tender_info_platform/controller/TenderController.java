package com.vonader.tender_info_platform.controller;

import com.vonader.tender_info_platform.domain.ServiceMartTender;
import com.vonader.tender_info_platform.domain.ProjectPurchaseTender;
import com.vonader.tender_info_platform.service.ServiceMartTenderService;
import com.vonader.tender_info_platform.service.ProjectPurchaseTenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Configuration
@RestController
@RequestMapping("/api/tenders")
@CrossOrigin(origins = "*") // 允许跨域
public class TenderController {

    // ========== 项目采购服务 ==========
    private final ProjectPurchaseTenderService projectPurchaseTenderService;
    // ========== 服务工程服务 ==========
    private final ServiceMartTenderService serviceMartTenderService;

    @Autowired
    public TenderController(ProjectPurchaseTenderService projectPurchaseTenderService,
                            ServiceMartTenderService serviceMartTenderService) {
        this.projectPurchaseTenderService = projectPurchaseTenderService;
        this.serviceMartTenderService = serviceMartTenderService;
    }

    // ========== 项目采购列表 ==========
    @GetMapping("/project")
    public Page<ProjectPurchaseTender> getFilteredTenders(
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return projectPurchaseTenderService.getFilteredTenders(region, keyword, date, pageable);
    }

    @GetMapping("/project/detail")
    public ProjectPurchaseTender getTenderDetailByUrl(@RequestParam("url") String url) {
        ProjectPurchaseTender tender = projectPurchaseTenderService.getTenderByUrl(url);
        if (tender == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到项目采购信息");
        }
        return tender;
    }

    // ========== 服务工程接口 ==========
    @GetMapping("/service")
    public Page<ServiceMartTender> getFilteredServiceEngineeringTenders(
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return serviceMartTenderService.getFilteredServiceMartTenders(
                region, keyword, date, pageable
        );
    }

    @GetMapping("/service/detail")
    public ServiceMartTender getServiceMartTenderDetailByUrl(@RequestParam("url") String url) {
        ServiceMartTender tender = serviceMartTenderService.getServiceMartTenderByUrl(url);
        if (tender == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到服务工程信息");
        }
        return tender;
    }
}