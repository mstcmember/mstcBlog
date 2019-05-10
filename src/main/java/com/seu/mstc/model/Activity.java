package com.seu.mstc.model;

import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 */
public class Activity {
    private int id;//活动id
    private int userId;//活动发布人的id
    private String title;//活动标题
    private String location;//活动地点
    private String time;//活动举办时间
    private Date createTime;//活动发布时间
    private int status;//活动的状态标志，是否可见，或者用来表示是否删除的状态(0代表可见，1代表被删除不可见)
    private int isTop;//活动是否置顶的标志（0代表不置顶，1代表置顶）
    private String sponsor;//活动的举办方
    private String imageUrl;//活动相关照片，或者宣传海报
    private String content;//活动内容
    private int likeCount;//活动点赞人数
    private int dislikeCount;//活动点踩人数

    private String nickname;//发帖人的昵称
    private String headUrl;//发帖人的头像
    private String commentCount;//技术帖的评论数
    private String collectionCount;//技术帖的收藏数
    private String createTimeStr;//技术帖创建时间
    private int viewCount;//技术帖浏览量
    private int entityType;//活动的标志

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(String collectionCount) {
        this.collectionCount = collectionCount;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }
}
