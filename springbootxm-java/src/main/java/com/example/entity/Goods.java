package com.example.entity;

import java.io.Serializable;

/**
 * 食物信息(Goods)实体类
 *
 */
public class Goods implements Serializable {
    private static final long serialVersionUID = -80685657190910503L;
/**
     * ID主键
     */
    private Integer id;
/**
     * 名称
     */
    private String name;
    //营养成分
    private String cfname;
/**
     * 热量
     */
    private Double rlnum;
/**
     * 营养值
     */
    private Double yynum;
/**
     * 详情
     */
    private String content;

    public String getCfname() {
        return cfname;
    }

    public void setCfname(String cfname) {
        this.cfname = cfname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRlnum() {
        return rlnum;
    }

    public void setRlnum(Double rlnum) {
        this.rlnum = rlnum;
    }

    public Double getYynum() {
        return yynum;
    }

    public void setYynum(Double yynum) {
        this.yynum = yynum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

