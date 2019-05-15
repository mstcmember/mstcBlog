package com.seu.mstc.model;

import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 */
public class BlogComment {
    private int id;//博文评论id
    private int entityId;//评论实体的id，标志是哪一个实体
    private int entityType;//评论的类型，表明是对活动的评论还是对评论的评论（0代表对活动的评论，1代表对评论的评论 ）
    private int userId;//评论人的id
    private int toUserId;//回复对象的用户id(可能有，可能没有)
    private Date createTime;//评论创建时间
    private String content;//评论的内容
    private int status;//评论可见性标志，用来表示删除评论(0代表可见，1代表被删除不可见)
    private String imageUrl;//博文评论的图片
    private int likeCount;//评论的点赞数量
    private int dislikeCount;//评论的点踩数量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
