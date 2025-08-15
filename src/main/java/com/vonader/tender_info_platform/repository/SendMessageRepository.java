package com.vonader.tender_info_platform.repository;

import com.vonader.tender_info_platform.domain.Contact;
import com.vonader.tender_info_platform.domain.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SendMessageRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT t FROM Config t")
    List<Config> findConfig();

    @Query("SELECT c FROM Contact c")
    Page<Contact> findAllContacts(Pageable pageable);
}
