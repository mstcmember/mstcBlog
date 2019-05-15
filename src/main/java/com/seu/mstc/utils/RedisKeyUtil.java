package com.seu.mstc.utils;

/*
本类用于统一产生jedis的key（为了防止不统一的key对jedis数据库产生误操作）
*/
public class RedisKeyUtil {
    private static String SPLIT = ":";

    private static String BIZ_EVENT = "BizEvent";                  //事件

    private static String QUESTION_LIKE = "QuestionLike";         //技术讨论帖模块的点赞
    private static String QUESTION_DISLIKE = "QuestionDislike";   //技术讨论帖模块的点踩
    private static String QUESTION_VIEW = "QuestionView";         //技术讨论帖模块的浏览量

    private static String PROGRAMMING_LIKE = "ProgrammingLike";    //算法编程模块的点赞
    private static String PROGRAMMING_DISLIKE = "ProgrammingDislike";//算法编程模块的点踩
    private static String PROGRAMMING_VIEW = "ProgrammingView";         //算法编程模块的浏览量
    //private static String PAGE_VIEWS = "PageViews";//浏览量

    private static String BLOG_LIKE = "BlogLike";         //博客模块的点赞
    private static String BLOG_DISLIKE = "BlogDislike";     //博客模块的点踩
    private static String BLOG_VIEW = "BlogView";         //博客模块的浏览量

    private static String ACTIVITY_LIKE = "ActivityLike";         //活动模块的点赞
    private static String ACTIVITY_DISLIKE = "ActivityDislike";     //活动模块的点踩
    private static String ACTIVITY_VIEW = "ActivityView";         //活动模块的浏览量



    public static String getBlogViewKey(int entityId) {
        return BLOG_VIEW + SPLIT+ entityId;
    }

    //type表示类型，有对博客的点赞，有对评论的点赞，Id表示点赞的博客的id
    public static String getBlogLikeKey(int entityType,int entityId){
        return BLOG_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
    public static String getBlogDislikeKey(int entityType,int entityId){
        return BLOG_DISLIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getActivityViewKey(int entityId) {
        return ACTIVITY_VIEW + SPLIT+ entityId;
    }

    //type表示类型，有对活动的点赞，有对评论的点赞，Id表示点赞的博客的id
    public static String getActivityLikeKey(int entityType,int entityId){
        return ACTIVITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
    public static String getActivityDislikeKey(int entityType,int entityId){
        return ACTIVITY_DISLIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getQuestionViewKey(int entityId) {
        return QUESTION_VIEW + SPLIT+ entityId;
    }

    public static String getQuestionLikeKey(int entityType,int entityId){
        return QUESTION_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getQuestionDislikeKey(int entityType,int entityId){
        return QUESTION_DISLIKE + SPLIT + entityType + SPLIT + entityId;
    }


    public static String getProgrammingViewKey(int entityId) {
        return PROGRAMMING_VIEW + SPLIT+ entityId;
    }

    public static String getProgrammingLikeKey(int entityType,int entityId){
        return PROGRAMMING_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getProgrammingDislikeKey(int entityType,int entityId){
        return PROGRAMMING_DISLIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getBizEventKey(){
        return BIZ_EVENT;
    }
}
