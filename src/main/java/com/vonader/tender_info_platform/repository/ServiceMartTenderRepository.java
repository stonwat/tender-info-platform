package com.vonader.tender_info_platform.repository;

import com.vonader.tender_info_platform.domain.ServiceMartTender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceMartTenderRepository extends JpaRepository<ServiceMartTender, String> {
    // 主键是 String 类型的 url

    // 过滤查询：支持按地区、关键词、日期筛选

    @Query("SELECT s FROM ServiceMartTender s WHERE " +
            "(:region IS NULL OR s.region = :region) AND " +
            "(:keyword IS NULL OR s.title LIKE %:keyword%) AND " +
            "(:date IS NULL OR s.date = :date)")
    Page<ServiceMartTender> findFilteredServiceMartTenders(
            @Param("region") String region,
            @Param("keyword") String keyword,
            @Param("date") String date,
            Pageable pageable);

    // 按 url 查询详情
    Optional<ServiceMartTender> findByUrl(String url);
}