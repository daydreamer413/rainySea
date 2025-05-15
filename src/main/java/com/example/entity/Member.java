package com.example.entity;

import java.io.Serializable;

/**
 * 普通用户信息表(Member)实体类
 *
 */
public class Member implements Serializable {
    private static final long serialVersionUID = -32277721026757243L;
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 联系方式
     */
    private String tel;
    /**
     * 图片
     */
    private String img;
    /**
     * 角色
     */
    private String role;

    private Integer delstatus;

    private String status;
    private int protypeid;
    private String mbtype;
    private String jktype;
    private String protypename;
    private String verCode;

    public int getProtypeid() {
        return protypeid;
    }

    public void setProtypeid(int protypeid) {
        this.protypeid = protypeid;
    }

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

    public String getProtypename() {
        return protypename;
    }

    public void setProtypename(String protypename) {
        this.protypename = protypename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getDelstatus() {
        return delstatus;
    }

    public void setDelstatus(Integer delstatus) {
        this.delstatus = delstatus;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }
}

