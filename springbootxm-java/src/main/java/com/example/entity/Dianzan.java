package com.example.entity;

import java.io.Serializable;

/**
 * 点赞(Dianzan)实体类
 *
 */
public class Dianzan implements Serializable {
    private static final long serialVersionUID = 846511302596809978L;
/**
     * 主键ID
     */
    private Integer id;
/**
     * 用户ID
     */
    private Integer memberid;
/**
     * 包含：菜品ID
     */
    private Integer shujuid;
/**
     * 点赞日期
     */
    private String savetime;
    /**
     * 数据名称
     */
    private String shujuname;

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

    public String getShujuname() {
        return shujuname;
    }

    public void setShujuname(String shujuname) {
        this.shujuname = shujuname;
    }

    public String getSavetime() {
        return savetime;
    }

    public void setSavetime(String savetime) {
        this.savetime = savetime;
    }

}

