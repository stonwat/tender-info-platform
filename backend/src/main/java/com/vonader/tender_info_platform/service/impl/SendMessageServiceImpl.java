package com.vonader.tender_info_platform.service.impl;

import com.vonader.tender_info_platform.common.ErrorCode;
import com.vonader.tender_info_platform.domain.Config;
import com.vonader.tender_info_platform.domain.Contact;
import com.vonader.tender_info_platform.exception.BusinessException;
import com.vonader.tender_info_platform.repository.ConfigRepository;
import com.vonader.tender_info_platform.repository.ContactRepository;
import com.vonader.tender_info_platform.service.SendMessageService;
import com.vonader.tender_info_platform.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 消息发送服务接口的实现类，实现具体的业务逻辑
 */
@Service
public class SendMessageServiceImpl implements SendMessageService {

    private final ConfigRepository configRepository;

    private final ContactRepository contactRepository;

    @Autowired
    public SendMessageServiceImpl(ConfigRepository configRepository,
                                  ContactRepository contactRepository) {
        this.configRepository = configRepository;
        this.contactRepository = contactRepository;
    }

    //========== 发送邮箱配置 ==========
    @Override
    public List<Config> getConfig() {
        return configRepository.findConfig();
    }

    @Override
    @Transactional
    public Config updateConfig(Config config) {
        // 验证ID
        if (config.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "配置ID不能为空");
        }
        // 检查配置是否存在
        boolean exists = configRepository.existsConfigById(config.getId());
        if (!exists) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "配置信息不存在，ID: " + config.getId());
        }
        // 执行更新，获取影响行数
        int affectedRows = configRepository.saveConfig(config);
        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "更新配置失败，未找到对应记录");
        }
        // 返回更新后的对象
        List<Config> updatedConfigs = getConfig();
        return updatedConfigs.stream()
                .filter(c -> c.getId().equals(config.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "更新后查询配置失败"));
    }

    //========== 联系人邮箱处理 ==========
    @Override
    public Page<Contact> getAllContacts(Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "分页参数不能为空");
        }
        return contactRepository.findAllContacts(pageable);
    }

    @Override
    @Transactional
    public Contact addContact(Contact contact) {
        // 验证邮箱非空
        String email = contact.getEmail();
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "邮箱不能为空");
        }
        email = email.trim();

        // 验证邮箱唯一性
        if (contactRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.CONFLICT_ERROR.getCode(), "邮箱已存在：" + email);
        }

        // 验证邮箱格式规范性
        if(!Validator.isValidEmail(email)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "邮箱格式不正确");
        }
        // 执行插入操作
        contactRepository.saveContact(contact);

        // 查询新增记录
        return contactRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "新增联系人失败，未找到记录"));
    }

    @Override
    @Transactional
    public Contact updateContactByUserId(Contact contact) {
        Integer userId = contact.getUserId();
        // 校验userId是否存在
        if (!contactRepository.existsByUserId(userId)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "联系人不存在，userId: " + userId);
        }

        // 校验邮箱
        String newEmail = contact.getEmail();
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请填写邮箱");
        }
        newEmail = newEmail.trim();

        // 校验邮箱唯一性（排除自身）
        Optional<Contact> existingContact = contactRepository.findByEmail(newEmail);
        if (existingContact.isPresent() && !existingContact.get().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.CONFLICT_ERROR.getCode(), "邮箱已被占用：" + newEmail);
        }

        // 执行更新
        contactRepository.updateContact(contact);

        // 返回更新后记录
        return contactRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "更新联系人失败，未找到记录"));
    }

    @Override
    @Transactional
    public void deleteContactByUserId(Integer userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "用户ID不能为空");
        }
        // 校验userId是否存在
        boolean exists = contactRepository.existsByUserId(userId);
        if (!exists) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "联系人不存在，userId: " + userId);
        }
        // 执行删除
        contactRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteContactByUserIds(List<Integer> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "用户ID列表不能为空");
        }

        // 校验用户ID列表是否存在
        List<Integer> existingIds = contactRepository.findExistingUserIds(userIds);
        if (existingIds.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "所有联系人都不存在");
        }

        // 如果存在部分不存在的ID，可以选择抛出异常或仅删除存在的
/*        if (existingIds.size() < userIds.size()) {
            List<Integer> nonExistingIds = userIds.stream()
                    .filter(id -> !existingIds.contains(id))
                    .collect(Collectors.toList());
            log.warn("部分联系人不存在，将跳过这些ID: {}", nonExistingIds);
        }*/

        // 这里选择仅删除存在的ID
        // 执行批量删除
        contactRepository.deleteByUserIds(userIds);
    }
}
