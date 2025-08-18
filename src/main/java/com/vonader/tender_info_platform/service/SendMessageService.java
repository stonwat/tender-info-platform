package com.vonader.tender_info_platform.service;

import com.vonader.tender_info_platform.domain.Contact;
import com.vonader.tender_info_platform.domain.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 消息发送服务接口，定义消息发送相关的业务方法
 */
public interface SendMessageService {

    /**
     * 查询系统邮箱配置信息
     * @return 配置信息列表
     */
    List<Config> getConfig();

    /**
     * 更新系统配置信息
     * @param config 包含更新信息的配置对象
     * @return 更新后的配置信息
     */
    Config updateConfig(Config config);

    //========== 联系人邮箱处理 ==========
    /**
     * 分页查询联系人列表
     * @param pageable 分页参数对象
     * @return 分页形式的联系人数据
     * @throws IllegalArgumentException 当分页参数为空时抛出
     */
    Page<Contact> getAllContacts(Pageable pageable);
}
