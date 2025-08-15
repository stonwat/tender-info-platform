package com.vonader.tender_info_platform.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "contact")
public class ContactSendMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Userid")
    private Integer userId;

    @Column(name = "Username", length = 100, nullable = true )
    private String userName;

    @Column(name = "Email", length = 100, unique = true, nullable = false )
    private String email;

    @Column(name = "remarks", length = 100)
    private String remarks;

    public Integer getUserId() {return userId;}
    public void setUserId(Integer userId) {this.userId = userId;}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getRemarks() {return remarks;}
    public void setRemarks(String remarks) {this.remarks = remarks;}

}
