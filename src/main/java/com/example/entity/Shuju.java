package com.example.entity;

import java.io.Serializable;

/**
 * 菜品信息(Shuju)实体类
 *
 */
public class Shuju implements Serializable {
    private static final long serialVersionUID = 664854194837531041L;
/**
     * 主键ID
     */
    private Integer id;
/**
     * 图片
     */
    private String fileimg;
/**
     * 名称
     */
    private String name;
/**
     * 口味分类ID
     */
    private Integer protypeid;
    /**
     * 分类ID
     */
    private Integer categoryid;
/**
     * 发布用户ID
     */
    private Integer memberid;
/**
     * 简要介绍
     */
    private String jjcontent;
    //适合人群
    private String mbtype;
    //适合身体状态
    private String jktype;
/**
     * 浏览量
     */
    private Integer llnum;
/**
     * 详情
     */
    private String content;
/**
     * 创建日期
     */
    private String savetime;
    /**
     * 评分
     */
    private double pfnum;

    private String protypename;
    private String categoryname;
    private String membername;
    private Integer dznum;
    private Integer fanum;

    public String getMbtype() {
        return mbtype;
    }

    public void setMbtype(String mbtype) {
        this.mbtype = mbtype;
    }

    public String getJktype() {
        return jktype;
    }

    public void setJktype(String jktype) {
        this.jktype = jktype;
    }

    public Integer getProtypeid() {
        return protypeid;
    }

    public void setProtypeid(Integer protypeid) {
        this.protypeid = protypeid;
    }

    public String getProtypename() {
        return protypename;
    }

    public void setProtypename(String protypename) {
        this.protypename = protypename;
    }

    public Integer getDznum() {
        return dznum;
    }

    public void setDznum(Integer dznum) {
        this.dznum = dznum;
    }

    public Integer getFanum() {
        return fanum;
    }

    public void setFanum(Integer fanum) {
        this.fanum = fanum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileimg() {
        return fileimg;
    }

    public void setFileimg(String fileimg) {
        this.fileimg = fileimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public String getJjcontent() {
        return jjcontent;
    }

    public void setJjcontent(String jjcontent) {
        this.jjcontent = jjcontent;
    }

    public Integer getLlnum() {
        return llnum;
    }

    public void setLlnum(Integer llnum) {
        this.llnum = llnum;
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

    public double getPfnum() {
        return pfnum;
    }

    public void setPfnum(double pfnum) {
        this.pfnum = pfnum;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }
}

