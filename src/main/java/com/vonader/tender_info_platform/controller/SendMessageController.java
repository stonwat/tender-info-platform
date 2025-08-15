package com.vonader.tender_info_platform.controller;

import com.vonader.tender_info_platform.domain.Contact;
import com.vonader.tender_info_platform.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Configuration
@RestController
@RequestMapping("/tender-info-platform-api/messages")
@CrossOrigin(origins = "*") // 允许跨域
public class SendMessageController {

    private final SendMessageService sendMessageService;

    @Autowired
    public SendMessageController(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @GetMapping("/contacts")
    public Page<Contact> getAllContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        // 构造分页参数：第page页（从0开始），每页size条
        Pageable pageable = PageRequest.of(page, size);
        // 调用Service层方法
        return sendMessageService.findAllContacts(pageable);
    }

}
