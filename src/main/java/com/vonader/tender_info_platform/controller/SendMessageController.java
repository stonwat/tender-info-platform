package com.vonader.tender_info_platform.controller;

import com.vonader.tender_info_platform.common.Result;
import com.vonader.tender_info_platform.domain.Contact;
import com.vonader.tender_info_platform.domain.Config;
import com.vonader.tender_info_platform.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息相关功能的控制器，负责处理与消息相关的HTTP请求，
 * 提供配置信息获取、联系人查询等接口服务，基础访问路径为/tender-info-platform-api/messages
 */
@RestController
@RequestMapping("/tender-info-platform-api/messages")
@CrossOrigin(origins = "*") // 允许所有来源的跨域请求访问本控制器接口
public class SendMessageController {

    private final SendMessageService sendMessageService;

    /**
     * 初始化控制器，通过依赖注入获取消息服务层实例
     */
    @Autowired
    public SendMessageController(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    /**
     * 获取系统配置信息列表
     * @return 统一格式的配置信息列表（Result<List<Config>>）
     */
    @GetMapping("/config")
    public Result<List<Config>> getConfig() {
        List<Config> configs = sendMessageService.getConfig();
        return Result.success(configs);
    }

    /**
     * 更新系统配置信息
     * @param config 包含更新信息的配置对象
     * @return 统一格式的更新结果（Result<Config>）
     */
    @PutMapping("/config")
    public Result<Config> updateConfig(@RequestBody Config config) {
        Config updatedConfig = sendMessageService.updateConfig(config);
        return Result.success(updatedConfig);
    }

    /**
     * 分页查询联系人列表
     * @param page 页码，从0开始，默认值为0
     * @param size 每页记录数，默认值为10
     * @return 统一格式的分页联系人数据（Result<Page<Contact>>）
     */
    @GetMapping("/contact")
    public Result<Page<Contact>> getAllContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Contact> contacts = sendMessageService.getAllContacts(pageable);
        return Result.success(contacts);
    }

    /**
     * 新增联系人
     * @param contact 包含联系人信息的对象（userName、email、remarks）
     * @return 统一格式的新增结果（Result<Contact>），包含自动生成的userId
     */
    @PostMapping("/contact")
    public Result<Contact> addContact(@RequestBody Contact contact) {
        Contact savedContact = sendMessageService.addContact(contact);
        return Result.success(savedContact);
    }

    /**
     * 根据userId更新联系人信息
     * @param userId 联系人ID（路径参数）
     * @param contact 包含更新信息的对象（请求体）
     * @return 统一格式的更新结果（Result<Contact>）
     */
    @PutMapping("/contact/{userId}")
    public Result<Contact> updateContactByUserId(
            @PathVariable Integer userId,
            @RequestBody Contact contact) {
        contact.setUserId(userId); // 确保更新的是指定用户
        Contact updatedContact = sendMessageService.updateContactByUserId(contact);
        return Result.success(updatedContact);
    }

    /**
     * 根据userId删除联系人
     * @param userId 联系人ID
     * @return 统一格式的删除成功响应（Result<Void>）
     */
    @DeleteMapping("/contact/{userId}")
    public Result<Void> deleteContactByUserId(@PathVariable Integer userId) {
        sendMessageService.deleteContactByUserId(userId);
        return Result.success(null); // 无数据返回时，data为null
    }

    /**
     * 批量删除联系人
     * @param userIds 联系人ID列表
     * @return 统一格式的删除成功响应（Result<Void>）
     */
    @DeleteMapping("/contact")
    public Result<Void> deleteContactByUserIds(@RequestBody List<Integer> userIds) {
        sendMessageService.deleteContactByUserIds(userIds);
        return Result.success(null); // 无数据返回时，data为null
    }
}