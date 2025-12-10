package com.vonader.tender_info_platform.repository;

import com.vonader.tender_info_platform.domain.ServiceMart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

@Repository
public interface ServiceMartRepository extends JpaRepository<ServiceMart, Long> {
    @Query("SELECT s FROM ServiceMart s WHERE " +
            "(:region IS NULL OR s.region = :region) AND " +
            "(:keyword IS NULL OR s.title LIKE %:keyword%) AND " +
            "(:date IS NULL OR s.date = :date)")
    Page<ServiceMart> findFilteredServiceMart(
            @Param("region") String region,
            @Param("keyword") String keyword,
            @Param("date") String date,
            Pageable pageable);

    // 按 url 查询详情
    @Query("SELECT s FROM ServiceMart s WHERE s.url = :url")
    Optional<ServiceMart> findServiceMartDetailByUrl(String url);
}