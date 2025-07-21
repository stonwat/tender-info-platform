package com.vonader.tender_info_platform.repository;

import com.vonader.tender_info_platform.domain.ProjectPurchaseTender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectPurchaseTenderRepository extends JpaRepository<ProjectPurchaseTender, Long> {

    @Query("SELECT t FROM ProjectPurchaseTender t WHERE " +
            "(:region IS NULL OR t.region = :region) AND " +
            "(:keyword IS NULL OR t.title LIKE %:keyword%) AND "+
            "(:date IS NULL OR t.date = :date)")
    Page<ProjectPurchaseTender> findFilteredTenders(
            @Param("region") String region,
            @Param("keyword") String keyword,
            @Param("date") String date,
            Pageable pageable);

    Optional<ProjectPurchaseTender> findByUrl(String url);
}