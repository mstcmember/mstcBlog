package com.seu.mstc.service;

import com.seu.mstc.model.Activity;
import com.seu.mstc.model.ActivityComment;
import com.seu.mstc.result.ResultInfo;

/**
 * Created by lk on 2018/5/3.
 */
public interface ActivityService {

    public ResultInfo addActivity(Activity activity);//发布活动
    public ResultInfo getActivity(int start,int num);//获取所有活动列表
    public ResultInfo addActivityLike(int entityType,int entityId,int userId);//对问题或评论点赞
    public ResultInfo addActivityDislike(int entityType,int entityId,int userId);//对问题或评论点踩
    public int getActivityLikeStatus(int entityType,int entityId,int userId);//获取某个用户当前的点赞、点踩状态
    public int getActivityLikeCount(int entityType,int entityId);//获取某个问题或评论点赞的总数
    public ResultInfo getActivityDetail(int activityId);//获取活动详情
    public ResultInfo getActivityComment(int offset,int limit,int entityId,int entityType);//获取活动的评论
    public ResultInfo addActivityComment(ActivityComment activityComment, int toCommentId);//发布评论
    public ResultInfo deleteActivity(int entityType, int entityId,int userId);//删除某个活动或活动的评论，entityId代表活动id，userId代表操作者id,entityType为0代表删除评论，1代表评论
    public ResultInfo addActivityViews(int entityId,int userId,String date);//对评论页面添加浏览量
    public int getActivityViewsCount(int entityId);//获取某个评论的总浏览量
    public ResultInfo updateIsTop(int status,int id);//对评论进行置顶操作
}
