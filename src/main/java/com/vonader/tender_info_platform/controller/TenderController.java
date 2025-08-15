package com.vonader.tender_info_platform.controller;

import com.vonader.tender_info_platform.domain.ProjectPurchase;
import com.vonader.tender_info_platform.domain.ServiceMart;
import com.vonader.tender_info_platform.service.TenderService;

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
@RequestMapping("/tender-info-platform-api/tenders")
@CrossOrigin(origins = "*") // 允许跨域
public class TenderController {

    
    private final TenderService tenderService;

    // ========== 项目采购服务 ==========
    @Autowired
    public TenderController(TenderService tenderService) {
        this.tenderService = tenderService;
    }

    // ========== 项目采购列表 ==========
    @GetMapping("/project")
    public Page<ProjectPurchase> getFilteredProjectPurchase(
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return tenderService.getFilteredProjectPurchase(region, keyword, date, pageable);
    }

    @GetMapping("/project/detail")
    public ProjectPurchase getProjectPurchaseDetailByUrl(@RequestParam("url") String url) {
        ProjectPurchase projectPurchase = tenderService.getProjectPurchaseDetailByUrl(url);
        if (projectPurchase == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到项目采购信息");
        }
        return projectPurchase;
    }

    // ========== 服务工程接口 ==========
    @GetMapping("/service")
    public Page<ServiceMart> getFilteredServiceEngineeringTenders(
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return tenderService.getFilteredServiceMart(
                region, keyword, date, pageable
        );
    }

    @GetMapping("/service/detail")
    public ServiceMart getServiceMartTenderDetailByUrl(@RequestParam("url") String url) {
        ServiceMart tender = tenderService.getServiceMartTenderByUrl(url);
        if (tender == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到服务工程信息");
        }
        return tender;
    }
}