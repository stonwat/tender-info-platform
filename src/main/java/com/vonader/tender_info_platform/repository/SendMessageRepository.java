package com.vonader.tender_info_platform.repository;

import com.vonader.tender_info_platform.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SendMessageRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c")
    Page<Contact> findAllContacts(Pageable pageable);
}
