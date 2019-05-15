package com.seu.mstc.model;

import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 */
public class MyFollower {
    private int id;
    private int userId;//关注用户的Id
    private int entityId;//被关注ID
    private Date createTime;//关注时间

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }
}
