package com.vonader.tender_info_platform.service;

import com.vonader.tender_info_platform.domain.ContactSendMessage;
import com.vonader.tender_info_platform.repository.ContactSendMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class ContactSendMessageService {

    private final ContactSendMessageRepository contactSendMessageRepository;

    @Autowired
    public ContactSendMessageService(final ContactSendMessageRepository contactSendMessageRepository) {
        this.contactSendMessageRepository = contactSendMessageRepository;
    }

    // 调用Repository的findAll方法，实现分页查询
    public Page<ContactSendMessage> findAllContacts(Pageable pageable) {
        // 可以在这里添加业务逻辑（如参数校验、权限控制等）
        if (pageable == null) {
            throw new IllegalArgumentException("分页参数不能为空");
        }
        // 调用Repository层的分页查询方法
        return contactSendMessageRepository.findAll(pageable);
    }

    // 无分页：查询所有数据
    public List<ContactSendMessage> findAllContacts() {
        // 可选：添加业务逻辑（如数据过滤、权限校验等）
        return contactSendMessageRepository.findAll(); // 调用无参findAll方法，返回所有记录的列表
    }

}
