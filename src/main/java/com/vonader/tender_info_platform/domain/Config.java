package com.vonader.tender_info_platform.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "config")
public class Config {
    // 对应表中的 ID 字段，自增主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    // 对应表中的 ParamName 字段，长度 100，允许为空
    @Column(name = "ParamName", length = 100, nullable = true)
    private String paramName;

    // 对应表中的 ParamValue 字段，长度 100，允许为空
    @Column(name = "ParamValue", length = 100, nullable = true)
    private String paramValue;

    // 获取 ID 的方法
    public Integer getId() {
        return id;
    }

    // 设置 ID 的方法
    public void setId(Integer id) {
        this.id = id;
    }

    // 获取 ParamName 的方法
    public String getParamName() {
        return paramName;
    }

    // 设置 ParamName 的方法
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    // 获取 ParamValue 的方法
    public String getParamValue() {
        return paramValue;
    }

    // 设置 ParamValue 的方法
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}