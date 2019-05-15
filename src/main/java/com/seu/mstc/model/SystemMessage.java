package com.seu.mstc.model;

import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 * 该系统消息表包括系统消息，和自己相关的技术贴，活动，编程题，评论点赞等消息的推送,可以考虑采用生产者消费者模型，采用推拉模式
 */
public class SystemMessage {
    private int id;//系统通知消息的id
    private int fromId;//发给消息的人
    private int toId;//接收该消息的人
    private String content;//消息内容
    private int hasRead;//该消息用户有没有读过
    private Date createTime;//消息发送时间
    private int entityId;//消息序号
    private int entityType;//消息类型


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}
