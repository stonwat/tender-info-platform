package com.vonader.tender_info_platform.controller;

import com.vonader.tender_info_platform.domain.Contact;
import com.vonader.tender_info_platform.domain.Config;
import com.vonader.tender_info_platform.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息相关功能的控制器，负责处理与消息相关的HTTP请求，
 * 提供配置信息获取、联系人查询等接口服务，基础访问路径为/tender-info-platform-api/messages
 */
@Configuration
@RestController
@RequestMapping("/tender-info-platform-api/messages")
@CrossOrigin(origins = "*") // 允许所有来源的跨域请求访问本控制器接口
public class SendMessageController {

    private final SendMessageService sendMessageService;

    /**
     * 初始化控制器，通过依赖注入获取消息服务层实例，
     * 用于调用业务逻辑处理方法
     */
    @Autowired
    public SendMessageController(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    /**
     * 获取系统配置信息列表
     * @return 配置信息列表
     */
    @GetMapping("/config")
    public List<Config> getConfig( ){
        // 调用服务层方法获取配置信息并返回
        return sendMessageService.getConfig();
    }

    /**
     * 更新系统配置信息
     * @param config 包含更新信息的配置对象
     * @return 更新后的配置信息
     */
    @PutMapping("/config")
    public Config updateConfig(@RequestBody Config config) {
        // 调用服务层方法更新配置信息并返回更新结果
        return sendMessageService.updateConfig(config);
    }

    /**
     * 分页查询联系人列表
     * @param page 页码，从0开始，默认值为0
     * @param size 每页记录数，默认值为10
     * @return 分页形式的联系人数据
     */
    @GetMapping("/contacts")
    public Page<Contact> getAllContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        // 根据传入的分页参数创建分页对象
        Pageable pageable = PageRequest.of(page, size);
        // 调用服务层方法执行分页查询并返回结果
        return sendMessageService.getAllContacts(pageable);
    }

}