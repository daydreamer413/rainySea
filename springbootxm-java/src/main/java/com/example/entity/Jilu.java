package com.example.entity;

import java.io.Serializable;

/**
 * 饮食记录(Jilu)实体类
 *
 */
public class Jilu implements Serializable {
    private static final long serialVersionUID = -26817166672613925L;
/**
     * 主键ID
     */
    private Integer id;
/**
     * 用户ID
     */
    private Integer memberid;
/**
     * 饮食日期
     */
    private String rqdate;
/**
     * 食物信息
     */
    private String goodsname;
/**
     * 营养总量
     */
    private Double yybtotal;
/**
     * 总热量
     */
    private Double subtotal;
/**
     * 创建日期
     */
    private String savetime;
    //建议信息
    private String content;
    private String membername;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
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

    public String getRqdate() {
        return rqdate;
    }

    public void setRqdate(String rqdate) {
        this.rqdate = rqdate;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public Double getYybtotal() {
        return yybtotal;
    }

    public void setYybtotal(Double yybtotal) {
        this.yybtotal = yybtotal;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getSavetime() {
        return savetime;
    }

    public void setSavetime(String savetime) {
        this.savetime = savetime;
    }

}

