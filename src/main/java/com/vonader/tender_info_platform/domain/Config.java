package com.vonader.tender_info_platform.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

/**
 * 系统配置实体类，映射数据库config表
 * 存储邮件发送相关配置参数，支持系统邮件服务的动态配置
 */
@Entity
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 发送邮箱地址
     * 用于系统发送通知、验证码等邮件的发件人账号
     */
    @Column(name = "send_email", length = 100, nullable = true)
    private String sendEmail;

    /**
     * 邮箱显示名称
     * 收件人看到的发件人昵称，提升邮件辨识度
     */
    @Column(name = "nick_name", length = 100, nullable = true)
    private String nickName;

    /**
     * SMTP服务器地址
     * 邮件发送协议服务器地址，如smtp.163.com、smtp.qq.com等
     */
    @Column(name = "smtp_server", length = 100, nullable = true)
    private String smtpServer;

    /**
     * SMTP服务器端口
     * 对应邮件服务商的SMTP端口，常见如465(SSL)、587(TLS)
     */
    @Column(name = "smtp_port", length = 100, nullable = true)
    private String smtpPort;

    /**
     * 邮箱授权码
     * 第三方登录邮箱的专用密码，替代账号密码用于安全验证
     */
    @Column(name = "grant_code", length = 100, nullable = true)
    private String grantCode;

    // 以下为属性访问方法，遵循JavaBean规范
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getGrantCode() {
        return grantCode;
    }

    public void setGrantCode(String grantCode) {
        this.grantCode = grantCode;
    }
}
