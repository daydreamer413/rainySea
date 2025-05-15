package com.example.entity;

import java.io.Serializable;

/**
 * 在线留言(Message)实体类
 *
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 886217715229600485L;
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 留言用户ID
     */
    private Integer memberid;
    /**
     * 留言内容
     */
    private String content;
    /**
     * 留言时间
     */
    private String savetime;
    /**
     * 是否匿名
     */
    private String hfcontent;
    private String hftime;
    private String username;
    private Member member;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSavetime() {
        return savetime;
    }

    public void setSavetime(String savetime) {
        this.savetime = savetime;
    }

    public String getHfcontent() {
        return hfcontent;
    }

    public void setHfcontent(String hfcontent) {
        this.hfcontent = hfcontent;
    }

    public String getHftime() {
        return hftime;
    }

    public void setHftime(String hftime) {
        this.hftime = hftime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}

