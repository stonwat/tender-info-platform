package com.vonader.tender_info_platform.controller;

import com.vonader.tender_info_platform.common.Result;
import com.vonader.tender_info_platform.domain.ProjectPurchase;
import com.vonader.tender_info_platform.domain.ServiceMart;
import com.vonader.tender_info_platform.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 招标信息相关功能控制器，负责处理与招标信息（项目采购、服务工程等）相关的 HTTP 请求，
 * 提供项目采购列表查询、详情获取，服务工程列表查询、详情获取等接口服务，
 * 基础访问路径为 /tender - info - platform - api/tenders
 */
@Configuration
@RestController
@RequestMapping("/tender-info-platform-api/tenders")
@CrossOrigin(origins = "*") // 允许所有来源的跨域请求访问本控制器接口
public class TenderController {

    private final TenderService tenderService;

    // ========== 项目采购服务 ==========
    /**
     * 初始化控制器，通过依赖注入获取招标业务服务层实例，
     * 用于调用业务逻辑处理方法，支撑项目采购、服务工程相关接口功能
     */
    @Autowired
    public TenderController(TenderService tenderService) {
        this.tenderService = tenderService;
    }

    // ========== 项目采购列表 ==========
    /**
     * 分页查询项目采购信息，支持按地区、关键词、日期筛选
     * @param region 地区筛选条件，非必需
     * @param keyword 关键词筛选条件，非必需
     * @param date 日期筛选条件，非必需
     * @param page 页码，从 0 开始，默认值为 0
     * @param size 每页记录数，默认值为 10
     * @return 分页形式的项目采购信息
     */
    @GetMapping("/project")
    public Result<Page<ProjectPurchase>> getFilteredProjectPurchase(
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectPurchase> projectPurchase = tenderService.getFilteredProjectPurchase(region, keyword, date, pageable);
        return Result.success(projectPurchase);
    }

    /**
     * 根据链接获取项目采购详情信息
     * @param url 项目采购详情页链接，用于定位具体信息
     * @return 项目采购详情对象，若未找到则抛出 404 异常
     */
    @GetMapping("/project/detail")
    public Result<ProjectPurchase> getProjectPurchaseDetailByUrl(@RequestParam("url") String url) {
        ProjectPurchase detail = tenderService.getProjectPurchaseDetailByUrl(url);
        return Result.success(detail);
    }

    // ========== 服务工程接口 ==========
    /**
     * 分页查询服务工程信息，支持按地区、关键词、日期筛选
     * @param region 地区筛选条件，非必需
     * @param keyword 关键词筛选条件，非必需
     * @param date 日期筛选条件，非必需
     * @param page 页码，从 0 开始，默认值为 0
     * @param size 每页记录数，默认值为 10
     * @return 分页形式的服务工程信息
     */
    @GetMapping("/service")
    public Result<Page<ServiceMart>> getFilteredServiceMartTenders(
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceMart> serviceMartTenders = tenderService.getFilteredServiceMart(
                region, keyword, date, pageable
        );
        return Result.success(serviceMartTenders);
    }

    /**
     * 根据链接获取服务工程详情信息
     * @param url 服务工程详情页链接，用于定位具体信息
     * @return 服务工程详情对象，若未找到则抛出 404 异常
     */
    @GetMapping("/service/detail")
    public Result<ServiceMart> getServiceMartTenderDetailByUrl(@RequestParam("url") String url) {
        ServiceMart detail = tenderService.getServiceMartTenderByUrl(url);
        return Result.success(detail);
    }
}