package com.vonader.tender_info_platform.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "service_mart")
public class ServiceMart {

    // 表中url是主键，映射为实体类主键
    @Id
    @Column(name = "url", length = 255, nullable = false)
    private String url;

    @Column(name = "budget", length = 255)
    private String budget;

    @Column(name = "start_time", length = 255)
    private String startTime;

    @Column(name = "end_time", length = 255)
    private String endTime;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "applicant", length = 2048)
    private String applicant;

    @Column(name = "region", length = 255)
    private String region;

    @Column(name = "llm", length = 2048)
    private String llm;

    @Column(name = "date", length = 255) // 表中date是varchar(255)
    private String date;

    @Column(name = "county", length = 255) //
    private String county;


    // Getters and Setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getLlm() { return llm; }
    public void setLlm(String llm) { this.llm = llm; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }
}