package com.vonader.tender_info_platform.repository;

import com.vonader.tender_info_platform.domain.ContactSendMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactSendMessageRepository extends JpaRepository<ContactSendMessage, Long> {

    // @Query(" SELECT t FROM ContactSendMessage t WHERE " + ) // 自定义查询
    Page<ContactSendMessage> findAll(Pageable pageable);
}
