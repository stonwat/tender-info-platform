package com.vonader.tender_info_platform.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "project_purchase")
public class ProjectPurchase {

    @Id
    @Column(name = "url", length = 255, nullable = false)
    private String url; // 使用URL作为主键

    @Column(name = "budget", length = 255)
    private String budget;

    @Column(name = "start_time", length = 255)
    private String startTime;

    @Column(name = "end_time", length = 255)
    private String endTime;

    @Column(name = "matter", length = 1024)
    private String matter;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "applicant", length = 2048)
    private String applicant;

    @Column(name = "category", length = 255)
    private String category;

    @Column(name = "region", length = 255)
    private String region;

    @Column(name = "llm", length = 3072)
    private String llm;

    @Column(name = "date")
    private String date;

    @Column(name = "county", length = 255) //
    private String county;

    // Getters and setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getMatter() { return matter; }
    public void setMatter(String matter) { this.matter = matter; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getLlm() { return llm; }
    public void setLlm(String llm) { this.llm = llm; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }
}