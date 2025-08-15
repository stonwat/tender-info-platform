package com.vonader.tender_info_platform.service;

import com.vonader.tender_info_platform.domain.Contact;
import com.vonader.tender_info_platform.domain.Config;
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

    // 查询发送邮箱配置
    public List<Config> getConfig() {
        // 调用Repository层的分页查询发送邮箱配置
        return sendMessageRepository.findConfig();
    }

    // 分页查询联系人邮箱列表
    public Page<Contact> getAllContacts(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("分页参数不能为空");
        }
        // 调用Repository层的分页查询方法
        return sendMessageRepository.findAllContacts(pageable);
    }

}
