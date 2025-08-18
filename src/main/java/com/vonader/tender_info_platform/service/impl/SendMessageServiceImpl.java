package com.vonader.tender_info_platform.service.impl;

import com.vonader.tender_info_platform.domain.Config;
import com.vonader.tender_info_platform.domain.Contact;
import com.vonader.tender_info_platform.repository.SendMessageRepository;
import com.vonader.tender_info_platform.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息发送服务接口的实现类，实现具体的业务逻辑
 */
@Service
public class SendMessageServiceImpl implements SendMessageService {

    private final SendMessageRepository sendMessageRepository;

    @Autowired
    public SendMessageServiceImpl(final SendMessageRepository sendMessageRepository) {
        this.sendMessageRepository = sendMessageRepository;
    }

    //========== 发送邮箱配置 ==========
    /**
     * 查询发送邮箱配置信息，调用数据访问层获取配置列表
     * @return 配置信息列表
     */
    @Override
    public List<Config> getConfig() {
        // 调用Repository层方法查询配置信息
        return sendMessageRepository.findConfig();
    }

    /**
     * 更新发件人邮箱配置信息，先验证配置ID有效性，再执行更新操作
     * @param config 包含更新信息的配置对象
     * @return 更新后的配置信息
     * @throws IllegalArgumentException 当配置ID为空或不存在时抛出
     */
    @Override
    @Transactional
    public Config updateConfig(Config config) {
        // 验证ID
        if (config.getId() == null) {
            throw new IllegalArgumentException("配置ID不能为空");
        }
        // 检查配置是否存在
        boolean exists = sendMessageRepository.existsConfigById(config.getId());
        if (!exists) {
            throw new IllegalArgumentException("配置信息不存在，ID: " + config.getId());
        }
        // 执行更新，获取影响行数
        int affectedRows = sendMessageRepository.saveConfig(config);
        if (affectedRows == 0) {
            throw new RuntimeException("更新配置失败，未找到对应记录");
        }
        // 返回更新后的对象（可重新查询一次数据库获取最新数据）
        List<Config> updatedConfigs = getConfig();
        // 如果是单条配置，直接返回第一条；如果是多条，筛选当前更新的记录
        return updatedConfigs.stream()
                .filter(c -> c.getId().equals(config.getId()))
                .findFirst()
                .orElse(updatedConfigs.get(0)); // 兼容单配置场景
    }

    //========== 联系人邮箱处理 ==========
    /**
     * 分页查询联系人邮箱列表，先验证分页参数有效性，再调用数据访问层查询
     * @param pageable 分页参数对象
     * @return 分页形式的联系人数据
     * @throws IllegalArgumentException 当分页参数为空时抛出
     */
    @Override
    public Page<Contact> getAllContacts(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("分页参数不能为空");
        }
        // 调用Repository层的分页查询方法
        return sendMessageRepository.findAllContacts(pageable);
    }
}
