package com.seu.mstc.model;

import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 */
public class UserFollower {
    private int id;//用户的粉丝记录表
    private int fromId;//关注人
    private int toId;//被关注对象
    private String relationshipId;//关注用户之间的id关系，格式采用:12_23,即代表用户id为12和23的用户之间的关系
    private int isFriend;//用户双方有没有互相关注的标志，有的话为1，没有的话为0
    private Date createTime;//关注时间

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

    public String getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(String relationshipId) {
        this.relationshipId = relationshipId;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
