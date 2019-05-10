package com.seu.mstc.service;

import com.seu.mstc.model.Question;
import com.seu.mstc.model.QuestionComment;
import com.seu.mstc.result.ResultInfo;


public interface QuestionService {

    public ResultInfo addQuestion(Question question);//发布技术讨论帖
    public ResultInfo getLatestQuestion(int start,int num,int flag);//获取最近状态正常的技术讨论帖信息
    public ResultInfo getQuestionDetail(int questionId);//获取技术帖详情
    public ResultInfo addQuestionComment(int entityType,int entityId,int userId,String content,int toCommentId);//发布评论
    public ResultInfo getLatestQuestionComment(int start,int num,int entityId,int entityType);//获取指定模块的最近状态正常的评论
    public ResultInfo addQuestionLike(int entityType,int entityId,int userId);//对问题或评论点赞
    public ResultInfo addQuestionDislike(int entityType,int entityId,int userId);//对问题或评论点踩
    public int getQuestionLikeStatus(int entityType,int entityId,int userId);//获取某个用户当前的点赞、点踩状态
    public int getQuestionLikeCount(int entityType,int entityId);//获取某个问题或评论点赞的总数
    public ResultInfo deleteQuestion(int entityType, int entityId,int userId);//删除某个问题，entityId代表问题id，userId代表操作者id,entityType为0代表删除问题，1代表删除评论
    public ResultInfo getHotQuestionDetail();//获取博客页面的热门推荐
    public int getQuestionViewsCount(int entityId);
    public ResultInfo addQuestionViews(int entityId,int userId,String date);
    public ResultInfo updateIsTop(int status,int id);//对技术帖进行置顶操作





}
