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

    /**
     * 新增联系人
     * @param contact 包含联系人信息的对象
     * @return 新增成功的联系人对象（包含自动生成的ID）
     * @throws IllegalArgumentException 当联系人邮箱为空或已存在时抛出
     */
    Contact addContact(Contact contact);

    /**
     * 根据userId更新联系人信息
     * @param contact 包含userId和更新信息的对象（userName、email、remarks）
     * @return 更新后的联系人对象
     * @throws IllegalArgumentException 当userId不存在或邮箱重复时抛出
     */
    Contact updateContactByUserId(Contact contact);

    /**
     * 根据userId删除联系人
     * @param userId 联系人ID
     * @throws IllegalArgumentException 当userId不存在时抛出
     */
    void deleteContactByUserId(Integer userId);

    /**
     * 批量删除联系人。
     * 此方法将成功删除所有存在的userId，并忽略任何不存在的userId。
     * @param userIds 要删除的联系人ID列表
     * @throws IllegalArgumentException 如果userIds列表为null或为空
     */
    void deleteContactByUserIds(List<Integer> userIds);
}
