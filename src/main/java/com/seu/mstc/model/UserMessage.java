package com.seu.mstc.model;

import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 * 该系统消息表表示用户之间的私信聊天记录，可以考虑采用生产者消费者模型，采用推拉模式
 */
public class UserMessage {
    private int id;//
    private int fromId;//发该消息的人
    private int toId;//接收该消息的人
    private String content;//消息内容
    private Date createTime;//消息发送时间
    private int hasRead;//该消息用户有没有读过
    private String conversationId;//会话用户id关系,格式为：23_12，表示用户23和12之间的聊天

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
