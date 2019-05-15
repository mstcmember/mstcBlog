package com.seu.mstc.service;

import com.seu.mstc.model.Blog;
import com.seu.mstc.model.BlogComment;
import com.seu.mstc.result.ResultInfo;


/**
 * Created by lk on 2018/5/3.
 */
public interface BlogService {

    public ResultInfo addBlog(Blog blog);//发布博文
    public ResultInfo getBlogsbyflag(int start,int num,int flag);//获取博文
    public ResultInfo getBlogDetail(int blogId);//获取博客详情
    public ResultInfo getLatestBlogComment(int offset,int limit,int entityId,int entityType);//获取指定模块的最近状态正常的评论
    public int getBlogLikeCount(int entityType,int entityId);//获取某个问题或评论点赞的总数
    public ResultInfo addComment(BlogComment blogComment,int toCommentId);//发布评论
    public ResultInfo addBlogLike(int entityType,int entityId,int userId);//对问题或评论点赞
    public ResultInfo addBlogDislike(int entityType,int entityId,int userId);//对问题或评论点踩
    public int getBlogLikeStatus(int entityType,int entityId,int userId);//获取某个用户当前的点赞、点踩状态
    public ResultInfo deleteBlog(int entityType, int entityId,int userId);//删除某个博客或者评论，entityId代表博客id，userId代表操作者id,entityType为0代表删除博客，1代表评论
    public ResultInfo addBlogViews(int entityId,int userId,String date);//对博客页面添加浏览量
    public int getBlogViewsCount(int entityId);//获取某个博客的总浏览量
    public ResultInfo getHotBlogDetail();//获取博客页面的热门推荐
    public ResultInfo updateIsTop(int status,int id);//对博客进行置顶操作
}
