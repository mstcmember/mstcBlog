package com.seu.mstc.service;

import com.seu.mstc.model.Programming;
import com.seu.mstc.model.Question;
import com.seu.mstc.result.ResultInfo;

/**
 * Created by lk on 2018/5/3.
 */
public interface ProgrammingService {

    public ResultInfo addProgramming(Programming programming);//发布编程题目
    public ResultInfo getLatestProgramming(int start,int num,int flag);//获取最近状态正常的编程题目信息
    public ResultInfo getProgrammingDetail(int programmingId);//获取编程题目详情
    public ResultInfo addProgrammingComment(int entityType,int entityId,int userId,String content,int toCommentId);//发布评论
    public ResultInfo getLatestProgrammingComment(int start,int num,int entityId,int entityType);//获取指定模块的最近状态正常的评论
    public ResultInfo addProgrammingLike(int entityType,int entityId,int userId);//对编程题目或评论点赞
    public ResultInfo addProgrammingDislike(int entityType,int entityId,int userId);//对编程题目或评论点踩
    public int getProgrammingLikeStatus(int entityType,int entityId,int userId);//获取某个用户当前的点赞、点踩状态
    public int getProgrammingLikeCount(int entityType,int entityId);//获取某个编程题目或评论点赞的总数
    public ResultInfo deleteProgramming(int entityType, int entityId,int userId);//删除某个编程题目，entityId代表编程题目id，userId代表操作者id,entityType为0代表删除问题，1代表删除评论

    public ResultInfo addProgrammingViews(int entityId,int userId,String date);//对编程题页面添加浏览量
    public int getProgrammingViewsCount(int entityId);//获取某个编程题的总浏览量
    public ResultInfo getHotProgrammingDetail();//获取编程题页面的热门推荐
    public ResultInfo updateIsTop(int status,int id);//对编程题进行置顶操作

}
