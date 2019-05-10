package com.seu.mstc.model;


import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 */
public class News {
    private int id;
    private int userId;//发布新闻的用户id
    private String title;//新闻标题
    private String newsAbstract;//新闻摘要
    private String content;//新闻内容
    private Date createTime;//发布时间
    private String imageUrl;//新闻相关图片
    private int isTop;//新闻是否置顶标志（0代表不置顶，1代表置顶）
    private int status;//新闻的状态标志，是否可见，或者用来表示是否删除的状态(0代表可见，1代表被删除不可见)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsAbstract() {
        return newsAbstract;
    }

    public void setNewsAbstract(String newsAbstract) {
        this.newsAbstract = newsAbstract;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
