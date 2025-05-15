package com.example.entity;

import java.io.Serializable;

/**
 * 健康资讯信息(News)实体类
 *
 */
public class News implements Serializable {
    private static final long serialVersionUID = -72847633871559497L;
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 图片
     */
    private String fileimg;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 发布时间
     */
    private String savetime;

    public String getFileimg() {
        return fileimg;
    }

    public void setFileimg(String fileimg) {
        this.fileimg = fileimg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

}

