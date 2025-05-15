package com.example.entity;

import java.io.Serializable;

/**
 * 轮播图(Imgadv)实体类
 *
 */
public class Imgadv implements Serializable {
    private static final long serialVersionUID = -94531865749416145L;
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 图片
     */
    private String fileimg;
    /**
     * 跳转链接
     */
    private String url;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

