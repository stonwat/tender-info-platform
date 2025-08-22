package com.vonader.tender_info_platform.repository;

import com.vonader.tender_info_platform.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息发送相关数据访问层接口，负责处理联系人(Contact)和配置(Config)的数据库操作
 * 继承JpaRepository，基于Spring Data JPA实现基本CRUD功能
 */
@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {

    /**
     * 查询所有发送邮箱配置信息
     * 从Config表中获取全部配置记录，用于系统参数初始化
     * @return 配置信息列表，包含邮件服务器、端口等参数
     */
    @Query("SELECT t FROM Config t")
    List<Config> findConfig();


    /**
     * 检查指定ID的配置是否存在
     * 用于更新配置前的有效性校验，避免更新不存在的记录
     * @param id 配置ID
     * @return 存在返回true，否则返回false
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Config t WHERE t.id = :id")
    boolean existsConfigById(Integer id);

    /**
     * 保存配置信息
     * 当配置ID存在时执行更新操作，不存在时执行新增（实际业务中配置通常为单条记录，此处主要用于更新）
     * @param config 包含更新信息的配置对象
     * @return 更新后的配置对象，包含数据库自动生成的字段（如更新时间等）
     */
    @Modifying
    @Transactional
    @Query("UPDATE Config t SET " +
            "t.sendEmail = :#{#config.sendEmail}, " +
            "t.nickName = :#{#config.nickName}, " +
            "t.smtpServer = :#{#config.smtpServer}, " +
            "t.smtpPort = :#{#config.smtpPort}, " +
            "t.grantCode = :#{#config.grantCode} " +
            "WHERE t.id = :#{#config.id}")
    int saveConfig(Config config);
}