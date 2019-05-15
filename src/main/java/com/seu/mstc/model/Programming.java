package com.seu.mstc.model;

import java.util.Date;


public class Programming {
    private int id;//编程题id
    private int userId;//编程题发布人的id
    private String title;//编程题标题
    private String ideas;//编程题内容
    private String answer;//编程题测试代码的url
    private String imageUrl;//编程题图片
    private Date createTime;//编程题发布时间
    private int status;//编程题的状态标志，是否可见，或者用来表示是否删除的状态(0代表可见，1代表被删除不可见)
    private int flag;//编程题类型标志，比如1代表剑指offer，2代表LeetCode，3其他
    private int likeCount;//编程题点赞人数
    private int dislikeCount;//编程题点踩人数
    private int isTop;//编程题是否置顶的标志（0代表不置顶，1代表置顶）
    private double hotScore;//编程题的热度分数



    private String nickname;//发帖人的昵称
    private String headUrl;//发帖人的头像
    private String commentCount;//技术帖的评论数
    private String collectionCount;//技术帖的收藏数
    private String createTimeStr;//技术帖创建时间
    private int viewCount;//技术帖浏览量
    private int entityType;//算法编程题的标志

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

    public double getHotScore() {
        return hotScore;
    }

    public void setHotScore(double hotScore) {
        this.hotScore = hotScore;
    }

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

    public String getIdeas() {
        return ideas;
    }

    public void setIdeas(String ideas) {
        this.ideas = ideas;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }
}
