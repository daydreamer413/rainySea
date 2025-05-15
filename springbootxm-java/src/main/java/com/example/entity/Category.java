package com.example.entity;

import java.io.Serializable;

/**
 * (Category)实体类
 *
 */
public class Category implements Serializable {
    private static final long serialVersionUID = -83936851653369435L;
    /**
     * ID主键
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 逻辑删除字段
     */
    private Integer delstatus;

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

    public Integer getDelstatus() {
        return delstatus;
    }

    public void setDelstatus(Integer delstatus) {
        this.delstatus = delstatus;
    }

}

