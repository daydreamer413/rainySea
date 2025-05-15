package com.example.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 评价(Evaluate)实体类
 *
 */
public class Evaluate implements Serializable {
    private static final long serialVersionUID = -22049598927961707L;
/**
     * 主键ID
     */
    private Integer id;
/**
     * 用户ID
     */
    private Integer memberid;
/**
     * 菜品ID
     */
    private Integer shujuid;
/**
     * 评分
     */
    private Integer pfnum;
/**
     * 评价内容
     */
    private String content;
/**
     * 评价日期
     */
    private String savetime;
    /**
     * 评价父级ID
     */
    private String evaluateid;
    private Member member;
    private Shuju shuju;
    private List<Evaluate> hflist;

    public List<Evaluate> getHflist() {
        return hflist;
    }

    public void setHflist(List<Evaluate> hflist) {
        this.hflist = hflist;
    }

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

    public Integer getShujuid() {
        return shujuid;
    }

    public void setShujuid(Integer shujuid) {
        this.shujuid = shujuid;
    }

    public Integer getPfnum() {
        return pfnum;
    }

    public void setPfnum(Integer pfnum) {
        this.pfnum = pfnum;
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

    public String getEvaluateid() {
        return evaluateid;
    }

    public void setEvaluateid(String evaluateid) {
        this.evaluateid = evaluateid;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Shuju getShuju() {
        return shuju;
    }

    public void setShuju(Shuju shuju) {
        this.shuju = shuju;
    }
}

