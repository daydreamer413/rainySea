package com.example.entity;

import java.io.Serializable;

/**
 * 行为日志(Rizhi)实体类
 *
 */
public class Rizhi implements Serializable {
    private static final long serialVersionUID = -94220028614212138L;
/**
     * ID主键
     */
    private Integer id;
/**
     * 用户账号
     */
    private String username;
/**
     * 用户角色
     */
    private String role;
/**
     * 行为类型
     */
    private String typename;
/**
     * 发生日期
     */
    private String savetime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getSavetime() {
        return savetime;
    }

    public void setSavetime(String savetime) {
        this.savetime = savetime;
    }

}

