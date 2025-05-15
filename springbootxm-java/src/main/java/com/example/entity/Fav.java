package com.example.entity;

import java.io.Serializable;

/**
 * 收藏(Fav)实体类
 *
 */
public class Fav implements Serializable {
    private static final long serialVersionUID = 493413644721913876L;
/**
     * 主键ID
     */
    private Integer id;
/**
     * 用户ID
     */
    private Integer memberid;
/**
     * s菜品ID
     */
    private Integer shujuid;
    private String savetime;
    private String shujuname;
    private Shuju shuju;

    public Shuju getShuju() {
        return shuju;
    }

    public void setShuju(Shuju shuju) {
        this.shuju = shuju;
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

    public String getSavetime() {
        return savetime;
    }

    public void setSavetime(String savetime) {
        this.savetime = savetime;
    }

    public String getShujuname() {
        return shujuname;
    }

    public void setShujuname(String shujuname) {
        this.shujuname = shujuname;
    }
}

