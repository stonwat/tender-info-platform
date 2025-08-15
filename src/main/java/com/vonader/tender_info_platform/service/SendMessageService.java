package com.vonader.tender_info_platform.service;

import com.vonader.tender_info_platform.domain.Contact;
import com.vonader.tender_info_platform.repository.SendMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class SendMessageService {

    private final SendMessageRepository sendMessageRepository;

    @Autowired
    public SendMessageService(final SendMessageRepository sendMessageRepository) {
        this.sendMessageRepository = sendMessageRepository;
    }

    // 调用Repository的findAll方法，实现分页查询
    public Page<Contact> findAllContacts(Pageable pageable) {
        // 可以在这里添加业务逻辑（如参数校验、权限控制等）
        if (pageable == null) {
            throw new IllegalArgumentException("分页参数不能为空");
        }
        // 调用Repository层的分页查询方法
        return sendMessageRepository.findAllContacts(pageable);
    }

}
