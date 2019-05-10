package com.seu.mstc.model;

import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 */
public class MyCollection {
    private int id;
    private int userId;//收藏用户的Id
    private int entityId;//收藏实体ID
    private int entityType;//收藏实体的类型（0代表算法题，1代表博文，2代表技术贴，3代表活动，4代表新闻）
    private Date createTime;//收藏时间
    private String title;//收藏内容的题目

    private String timeView;//前端显示时间格式

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

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }
}
