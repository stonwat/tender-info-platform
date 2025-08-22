package com.vonader.tender_info_platform.repository;

import com.vonader.tender_info_platform.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 消息发送相关数据访问层接口，负责处理联系人(Contact)和配置(Config)的数据库操作
 * 继承JpaRepository，基于Spring Data JPA实现基本CRUD功能
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    /**
     * 分页查询联系人邮箱列表
     * 从Contact表中分页获取联系人记录，支持前端分页展示
     * @param pageable 分页参数（页码、每页条数、排序规则）
     * @return 分页形式的联系人数据，包含当前页记录和分页元信息
     */
    @Query("SELECT c FROM Contact c")
    Page<Contact> findAllContacts(Pageable pageable);

    /**
     * 检查邮箱是否已存在
     * 用于新增联系人时验证邮箱唯一性，避免重复添加
     * @param email 待检查的邮箱地址
     * @return 存在返回true，否则返回false
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Contact c WHERE c.email = :email")
    boolean existsByEmail(String email);

    /**
     * 保存新联系人
     * 插入新的联系人记录到数据库，自动生成userId
     * @param contact 包含联系人信息的对象（userName、email、remarks）
     * @return 保存后的联系人对象，包含自动生成的userId
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO contact (user_name, email, remarks) " +
            "VALUES (:#{#contact.userName}, :#{#contact.email}, :#{#contact.remarks})",
            nativeQuery = true)
    void saveContact(Contact contact);

    /**
     * 新增后查询刚插入的联系人（用于获取自动生成的userId）
     * @param email 联系人邮箱（唯一）
     * @return 包含完整信息的联系人对象
     */
    @Query("SELECT c FROM Contact c WHERE c.email = :email")
    Optional<Contact> findByEmail(String email);

    /**
     * 检查userId是否存在
     * @param userId 联系人ID
     * @return 存在返回true，否则返回false
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Contact c WHERE c.userId = :userId")
    boolean existsByUserId(@Param("userId") Integer userId);

    /**
     * 根据userId查询联系人（用于更新后获取完整记录）
     * @param userId 联系人ID
     * @return 联系人对象（Optional包装）
     */
    @Query("SELECT c FROM Contact c WHERE c.userId = :userId")
    Optional<Contact> findByUserId(@Param("userId") Integer userId);

    /**
     * 根据userId更新联系人信息
     * @param contact 包含更新信息的对象（userName、email、remarks、userId）
     */
    @Modifying
    @Transactional
    @Query("UPDATE Contact c SET " +
            "c.userName = :#{#contact.userName}, " +
            "c.email = :#{#contact.email}, " +
            "c.remarks = :#{#contact.remarks} " +
            "WHERE c.userId = :#{#contact.userId}")
    void updateContact(@Param("contact") Contact contact);

    /**
     * 根据userId删除联系人
     * @param userId 联系人ID
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Contact c WHERE c.userId = :userId")
    void deleteByUserId(@Param("userId") Integer userId);

    /**
     * 批量根据userId删除联系人
     * @param userIds 联系人ID列表
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Contact c WHERE c.userId IN :userIds")
    void deleteByUserIds(@Param("userIds") List<Integer> userIds);

    /**
     * 查询存在的userId
     * @param userIds 待查询的userId列表
     * @return 存在的userId列表
     */
    @Query("SELECT c.userId FROM Contact c WHERE c.userId IN :userIds")
    List<Integer> findExistingUserIds(@Param("userIds") List<Integer> userIds);
}