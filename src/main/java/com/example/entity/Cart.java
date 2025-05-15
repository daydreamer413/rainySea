package com.example.entity;

import java.io.Serializable;

/**
 * 饮食清单(Cart)实体类
 *
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = -34334617994429783L;
/**
     * 主键ID
     */
    private Integer id;
/**
     * 用户ID
     */
    private Integer memberid;
/**
     * 食物ID
     */
    private Integer goodsid;
/**
     * 数量
     */
    private Integer slnum;
    private Double heji;
    private Double yyheji;
    private String goodsname;
    private Goods goods;

    public Double getYyheji() {
        return yyheji;
    }

    public void setYyheji(Double yyheji) {
        this.yyheji = yyheji;
    }

    public Double getHeji() {
        return heji;
    }

    public void setHeji(Double heji) {
        this.heji = heji;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public Integer getSlnum() {
        return slnum;
    }

    public void setSlnum(Integer slnum) {
        this.slnum = slnum;
    }

}

