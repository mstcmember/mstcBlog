package com.seu.mstc.model;

import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 */
public class Question {
    private int id;//技术讨论帖id
    private int userId;//技术讨论帖发布人的id
    private String title;//技术讨论帖标题
    private String content;//技术讨论帖内容
    private String imageUrl;//技术讨论帖图片
    private Date createTime;//技术讨论帖发布时间
    private String keyWord;//技术讨论帖关键词
    private int status;//技术讨论帖的状态标志，是否可见，或者用来表示是否删除的状态(0代表可见，1代表被删除不可见)
    private int flag;//技术讨论帖类型标志，比如1代表C++，2代表JAVA，3前端，4后台，5机器学习，6硬件嵌入式等等，
    private int likeCount;//技术讨论帖点赞人数
    private int dislikeCount;//技术讨论帖点踩人数
    private int isTop;//技术讨论帖是否置顶的标志（0代表不置顶，1代表置顶）
    private double hotScore;//技术帖的热度分数
//    private Map<String,String> exts = new HashMap<>();//定义扩展的字段，方便今后向实体中添加新的字段

    private String nickname;//发帖人的昵称
    private String headUrl;//发帖人的头像
    private String commentCount;//技术帖的评论数
    private String collectionCount;//技术帖的收藏数
    private String createTimeStr;//技术帖创建时间
    private int viewCount;//技术帖浏览量
    private int entityType;//技术讨论帖的标志

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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

//    public Question setExt(String key, String value){
//        exts.put(key,value);
//        return this;
//    }
//
//    public String getExt(String key){
//        return exts.get(key);
//    }


    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }
}
