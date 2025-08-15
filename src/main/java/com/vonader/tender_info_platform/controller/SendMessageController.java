package com.vonader.tender_info_platform.controller;

import com.vonader.tender_info_platform.domain.ContactSendMessage;
import com.vonader.tender_info_platform.service.ContactSendMessageService;
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

    private final ContactSendMessageService contactSendMessageService;

    @Autowired
    public SendMessageController(ContactSendMessageService contactSendMessageService) {
        this.contactSendMessageService = contactSendMessageService;
    }

    @GetMapping("/contacts")
    public Page<ContactSendMessage> getAllContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        // 构造分页参数：第page页（从0开始），每页size条
        Pageable pageable = PageRequest.of(page, size);
        // 调用Service层方法
        return contactSendMessageService.findAllContacts(pageable);
    }

    @GetMapping("/contacts2")
    public List<ContactSendMessage> getAllContacts() {
        return contactSendMessageService.findAllContacts();
    }
}
