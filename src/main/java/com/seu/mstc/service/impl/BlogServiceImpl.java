package com.seu.mstc.service.impl;

import com.seu.mstc.async.EventModel;
import com.seu.mstc.async.EventProducer;
import com.seu.mstc.async.EventType;
import com.seu.mstc.dao.BlogCommentDao;
import com.seu.mstc.dao.BlogDao;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.jedis.JedisClient;
import com.seu.mstc.model.Blog;
import com.seu.mstc.model.BlogComment;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.BlogService;
import com.seu.mstc.service.CollectionService;
import com.seu.mstc.service.SensitiveService;
import com.seu.mstc.utils.QuestionUtils;
import com.seu.mstc.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by lk on 2018/5/3.
 */
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    BlogDao blogDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    BlogCommentDao blogCommentDao;

    @Autowired
    BlogService blogService;

    @Autowired
    JedisClient jedisClient;

    @Autowired
    EventProducer eventProducer;


    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    CollectionService collectionService;

    @Override
    public ResultInfo addBlog(Blog blog) {
        ResultInfo result = null;
        blog.setLikeCount(0);
        blog.setDislikeCount(0);                          //添加时赞踩都为0
        blog.setIsTop(0);
        int a=blogDao.addBlog(blog);
        if(a>0)
            result = ResultInfo.ok();


        EventModel addEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        addEventModel.setEntityOwnerId(hostHolder.getUser().getId())//发布博客人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(0);//发布博客加分
        eventProducer.fireEvent(addEventModel);


        return result;
    }

    @Override
    public ResultInfo getBlogsbyflag(int start,int num,int flag) {
        ResultInfo result = null;
        List<Blog> ListBlog = new ArrayList<>();
        int blogCount = 0;
        if(flag==0) {
            blogCount = blogDao.selectBlogNum();
            if (start >= blogCount) {
                return ResultInfo.build(999, "end");
            }
//            ListBlog = blogDao.selectAllBlog();
            ListBlog = blogDao.selectPageBlogByOrder(start, num);
        }
        else {
            blogCount=blogDao.selectBlogNumByFlag(flag);
            if (start >= blogCount) {
                return ResultInfo.build(999, "end");
            }
            ListBlog = blogDao.selectPageBlogByflagByOrder(start, num,flag);
        }
        List<Map<String,Object>> FinalBlogs = new ArrayList<>();
        for (Blog blog : ListBlog) {
            Map<String,Object> blogmap = new HashMap<>();
            blogmap=QuestionUtils.beanToMap(blog);
            //blogmap.put("blog",blog)
            blogmap.put("nickname",userDao.selectById(blog.getUserId()).getNickname());
            blogmap.put("headUrl", userDao.selectById(blog.getUserId()).getHeadUrl());
            blogmap.put("commentCount", blogCommentDao.selectBlogCommentNumByIdType(blog.getId(),0));
            blogmap.put("collectionCount",collectionService.getCountOfCollection(1,blog.getId()));//entityType为1代表博客
            blogmap.put("viewCount",blogService.getBlogViewsCount(blog.getId()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = formatter.format(blog.getCreateTime());
            blogmap.put("createTime",timeString);
            FinalBlogs.add(blogmap);
        }
        result = ResultInfo.build(200,String.valueOf(blogCount),FinalBlogs);
        return result;
    }

    @Override
    public ResultInfo getHotBlogDetail() {
        ResultInfo result = null;
        List<Blog> ListBlog = new ArrayList<>();
        ListBlog = blogDao.selectHotBlog();
        List<Map<String,Object>> FinalBlogs = new ArrayList<>();
        for (Blog blog : ListBlog) {
            Map<String,Object> blogmap = new HashMap<>();
            blogmap=QuestionUtils.beanToMap(blog);
            //blogmap.put("blog",blog)
            blogmap.put("nickname",userDao.selectById(blog.getUserId()).getNickname());
            blogmap.put("headUrl", userDao.selectById(blog.getUserId()).getHeadUrl());
            blogmap.put("commentCount", blogCommentDao.selectBlogCommentNumByIdType(blog.getId(),0));
            blogmap.put("collectionCount",collectionService.getCountOfCollection(1,blog.getId()));//entityType为1代表博客
            blogmap.put("viewCount",blogService.getBlogViewsCount(blog.getId()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = formatter.format(blog.getCreateTime());
            blogmap.put("createTime",timeString);
            FinalBlogs.add(blogmap);
        }
        result = ResultInfo.ok(FinalBlogs);
        return result;
    }


    @Override
    public ResultInfo getBlogDetail(int flagId){
        ResultInfo result = null;
        Blog blog = blogDao.selectBlogById(flagId);
        Map<String,Object> blogmap = new HashMap<>();
        blogmap=QuestionUtils.beanToMap(blog);
//        blogmap.put("blog",blog);
        blogmap.put("nickname",userDao.selectById(blog.getUserId()).getNickname());
        blogmap.put("headUrl", userDao.selectById(blog.getUserId()).getHeadUrl());
        blogmap.put("commentCount", blogCommentDao.selectBlogCommentNumByIdType(blog.getId(),0));
        blogmap.put("likeCount",blogService.getBlogLikeCount(0,blog.getId()));
        if(hostHolder.getUser()!=null) {
            blogmap.put("isLike", getBlogLikeStatus(0, blog.getId(), hostHolder.getUser().getId()));//0表示是对博客的评论,而不是对评论的评论
            blogmap.put("isCollection",collectionService.isCollection(hostHolder.getUser().getId(),1,blog.getId()));//2代表博客
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = formatter.format(blog.getCreateTime());
        blogmap.put("createTime",timeString);
        Date nowTime=new Date();
        String timeYmd = formatter.format(nowTime).substring(0,10);
        if(hostHolder.getUser()!=null)
            blogService.addBlogViews(flagId,hostHolder.getUser().getId(),timeYmd);

        result = ResultInfo.ok(blogmap);
        return result;
    }

    @Override
    public ResultInfo getLatestBlogComment(int start,int num,int entityId,int entityType){
        ResultInfo result = null;
        int blogCommentCount = blogCommentDao.selectBlogCommentNumByIdType(entityId,entityType);
        if(start>=blogCommentCount){
            return ResultInfo.build(999,"end");
        }

        List<BlogComment> BlogCommentList = blogCommentDao.selectBlogCommentByIdTypeLimit(entityId,entityType,start,num);

//        List<BlogComment> latestblogCommentList = new ArrayList<>();
        List<Map<String,Object>> FinalComments = new ArrayList<>();
//        int i =0;
//        if(limit>=BlogCommentList.size()){
//            limit=BlogCommentList.size()-1;
//        }
//        for(i=offset;i<=limit;i++){
//            latestblogCommentList.add(BlogCommentList.get(i));
//        }
        for(BlogComment blogComment : BlogCommentList){
            Map<String,Object> commentmap = new HashMap<>();
            commentmap=QuestionUtils.beanToMap(blogComment);
            commentmap.put("nickname",userDao.selectById(blogComment.getUserId()).getNickname());
            commentmap.put("headUrl",userDao.selectById(blogComment.getUserId()).getHeadUrl());
//          commentmap.put("likeCount",blogService.getBlogLikeCount(blogComment.getEntityType(),blogComment.getId()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(blogComment.getCreateTime());
            commentmap.put("createTime",dateString);

            commentmap.put("replyCount",blogCommentDao.selectBlogCommentNumByIdType(blogComment.getId(),1));
            //以下为选出二级评论

            List<BlogComment> blogCommentReplyList = blogCommentDao.selectBlogCommentByIdType(blogComment.getId(),1);
            List<Map<String,Object>> FinalCommentsReply = new ArrayList<>();
            for(BlogComment blogCommentReply : blogCommentReplyList){
                Map<String,Object> commentListmap = new HashMap<>();
                commentListmap=QuestionUtils.beanToMap(blogCommentReply);

                commentListmap.put("nickname",userDao.selectById(blogCommentReply.getUserId()).getNickname());
                commentListmap.put("headUrl",userDao.selectById(blogCommentReply.getUserId()).getHeadUrl());
                commentListmap.put("replyCount",blogCommentDao.selectBlogCommentNumByIdType(blogCommentReply.getId(),1));

                String dateString1 = formatter.format(blogCommentReply.getCreateTime());
                commentListmap.put("createTime",dateString1);
                commentListmap.put("toNickname",userDao.selectById(blogCommentReply.getToUserId()).getNickname());
                FinalCommentsReply.add(commentListmap);
            }
            commentmap.put("blogCommentReplyList",FinalCommentsReply);
            FinalComments.add(commentmap);
        }
        result = ResultInfo.build(200,String.valueOf(blogCommentCount),FinalComments);
//        result = ResultInfo.ok(FinalComments);
        return  result;
    }
    @Override
    public int getBlogLikeCount(int entityType,int entityId){
        try {
            return (int) jedisClient.scard(RedisKeyUtil.getBlogLikeKey(entityType, entityId));
        }catch (Exception e){
            return 0;
        }
    }
    @Override
    public ResultInfo addComment(BlogComment blogComment,int toCommentId){
        blogComment.setCreateTime(new Date());
        blogComment.setStatus(0);
        blogComment.setLikeCount(0);
        blogComment.setDislikeCount(0);
        int entityType=blogComment.getEntityType();
        int entityId=blogComment.getEntityId();
        //blogComment.setContent(HtmlUtils.htmlEscape(blogComment.getContent()));
        blogComment.setContent(sensitiveService.filter(blogComment.getContent()));
        if(entityType==1){                //如果是回复评论，需要加上是回复哪一个人的评论
            if(toCommentId>0){
                blogComment.setToUserId(blogCommentDao.selectBlogCommentById(toCommentId).getUserId());
            }else {
                blogComment.setToUserId(blogCommentDao.selectBlogCommentById(blogComment.getEntityId()).getUserId());
            }
        }
        blogCommentDao.addBlogComment(blogComment);


        EventModel eventModel = new EventModel();//向消息队列中添加评论事件
        String entityIdOfMessage;
        int entityOwnerIdOfMessage;
        if(blogComment.getEntityType()==1){  //对评论的评论
            entityOwnerIdOfMessage = blogComment.getToUserId();
            BlogComment toBlogComment = blogCommentDao.selectBlogCommentById(entityId);
            Blog blog = blogDao.selectBlogById(toBlogComment.getEntityId());
            entityIdOfMessage = String.valueOf(blog.getId());
        }else {  //对问题的评论
            Blog blog = blogDao.selectBlogById(entityId);
            entityOwnerIdOfMessage = blog.getUserId();
            entityIdOfMessage = String.valueOf(blog.getId());
        }
        eventModel.setAuthorId(hostHolder.getUser().getId()) //评论人
                .setEntityId(entityId)//实体id
                .setEntityOwnerId(entityOwnerIdOfMessage)//对谁的帖子或者评论进行评论
                .setEntityType(entityType)//对问题评论还是对评论评论
                .setEventType(EventType.COMMENT)//评论事件
                .setExt("questionId",entityIdOfMessage)//评论事件的技术帖id
                .setExt("entityType","1");//2代表是对技术帖模块的评论
        eventProducer.fireEvent(eventModel);



        EventModel addEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        addEventModel.setEntityOwnerId(blogComment.getUserId())//发起评论人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(2);//评论加分
        eventProducer.fireEvent(addEventModel);

        int userId=0;
        if(blogComment.getEntityType()==0){
            //对发布博客的人加分
            userId=blogDao.selectBlogById(blogComment.getEntityId()).getUserId();
        }
        if(blogComment.getEntityType()==1){
            //对发布评论的人加分
            userId=blogComment.getToUserId();
        }

        addEventModel.setEntityOwnerId(userId)//被评论人或者被回复的人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(2);//评论加分
        eventProducer.fireEvent(addEventModel);

        return ResultInfo.ok();
    }



    //点赞方法，输入点赞的类型，id，用户id，返回当前点赞的数量
    @Override
    public ResultInfo addBlogLike(int entityType,int entityId,int userId){
        jedisClient.sadd(RedisKeyUtil.getBlogLikeKey(entityType,entityId),String.valueOf(userId));
        jedisClient.srem(RedisKeyUtil.getBlogDislikeKey(entityType,entityId),String.valueOf(userId));

        if(!jedisClient.sismember(RedisKeyUtil.getBlogLikeKey(entityType,entityId),String.valueOf(userId))) {
            EventModel eventModel = new EventModel();//向消息队列中添加点赞事件
            String entityIdOfMessage;
            int entityOwnerIdOfMessage;
            Blog blog = blogDao.selectBlogById(entityId);
            entityOwnerIdOfMessage = blog.getUserId();
            entityIdOfMessage = String.valueOf(blog.getId());

            eventModel.setAuthorId(hostHolder.getUser().getId()) //点赞人
                    .setEntityId(entityId)//实体id
                    .setEntityOwnerId(entityOwnerIdOfMessage)//对谁的帖子或者评论进行点赞
                    .setEntityType(entityType)//对问题点赞还是对评论点赞
                    .setEventType(EventType.LIKE)//点赞事件
                    .setExt("questionId", entityIdOfMessage)//点赞事件的技术帖id/博客id
                    .setExt("entityType", "1");//1代表是对博客模块的点赞
            eventProducer.fireEvent(eventModel);


            EventModel hotEventModel = new EventModel();//向消息队列中添加计算热度的事件
            hotEventModel.setEntityId(entityId)//博客或技术贴的id
                    .setEventType(EventType.CALCULATE)//计算事件
                    .setExt("calculateType", "1");//1代表是对博客模块的热度进行计算
            eventProducer.fireEvent(hotEventModel);




            EventModel likeEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            likeEventModel.setEntityOwnerId(hostHolder.getUser().getId())//点赞人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(6);//点赞加分
            eventProducer.fireEvent(likeEventModel);

            int userIdTemp=0;
            if(entityType==0){
                userIdTemp=blogDao.selectBlogById(entityId).getUserId();//给发布博客的人加分
            }
            if(entityType==1){
                userIdTemp=blogCommentDao.selectBlogCommentById(entityId).getUserId();//给发布评论的人加分
            }

            likeEventModel.setEntityOwnerId(userIdTemp)//被点赞人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(6);//点赞加分
            eventProducer.fireEvent(likeEventModel);




        }

        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getBlogLikeKey(entityType,entityId)));
    }

    @Override
    public ResultInfo addBlogDislike(int entityType,int entityId,int userId){
        jedisClient.sadd(RedisKeyUtil.getBlogDislikeKey(entityType,entityId),String.valueOf(userId));
        jedisClient.srem(RedisKeyUtil.getBlogLikeKey(entityType,entityId),String.valueOf(userId));

        EventModel disLikeEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        disLikeEventModel.setEntityOwnerId(hostHolder.getUser().getId())//点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(7);//取消点赞减分
        eventProducer.fireEvent(disLikeEventModel);

        int userIdTemp=0;
        if(entityType==0){
            userIdTemp=blogDao.selectBlogById(entityId).getUserId();//给发布博客的人减分
        }
        if(entityType==1){
            userIdTemp=blogCommentDao.selectBlogCommentById(entityId).getUserId();//给发布评论的人减分
        }

        disLikeEventModel.setEntityOwnerId(userIdTemp)//被点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(7);//取消点赞减分
        eventProducer.fireEvent(disLikeEventModel);


        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getBlogLikeKey(entityType,entityId)));
    }

    @Override
    public ResultInfo addBlogViews(int entityId,int userId,String date){
        jedisClient.sadd(RedisKeyUtil.getBlogViewKey(entityId),String.valueOf(userId)+date);

        EventModel hotEventModel = new EventModel();//向消息队列中添加计算热度的事件
        hotEventModel.setEntityId(entityId)//博客或技术贴的id
                .setEventType(EventType.CALCULATE)//计算事件
                .setExt("calculateType","1");//1代表是对博客模块的热度进行计算
        eventProducer.fireEvent(hotEventModel);

        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getBlogViewKey(entityId)));
    }

    @Override
    public int getBlogViewsCount(int entityId){
        try {
            return (int) jedisClient.scard(RedisKeyUtil.getBlogViewKey(entityId));
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int getBlogLikeStatus(int entityType,int entityId,int userId){
        if(jedisClient.sismember(RedisKeyUtil.getBlogLikeKey(entityType,entityId),String.valueOf(userId))){
            return 1;
        }else if(jedisClient.sismember(RedisKeyUtil.getBlogDislikeKey(entityType,entityId),String.valueOf(userId))) {
            return -1;
        }else{
            return 0;
        }
    }

    @Override
    public ResultInfo deleteBlog(int entityType,int entityId, int userId) {
        EventModel eventModel = new EventModel();//向消息队列中添加删除事件
        if(entityType==0){
            eventModel.setEntityOwnerId(blogDao.selectBlogById(entityId).getUserId());//删除对象的作者的id
        }else{
            eventModel.setEntityOwnerId(blogCommentDao.selectBlogCommentById(entityId).getUserId());//删除对象的作者的id
        }
        eventModel.setAuthorId(userId) //删除的操作人id
                .setEntityId(entityId)//删除对象的id 表示博客或者评论在数据表中的id
                .setEntityType(1)//1代表是对博客模块的删除
                .setEventType(EventType.DELETE)//删除事件
                .setExt("entityType", String.valueOf(entityType));//0代表删除博客，1代表删除评论
        eventProducer.fireEvent(eventModel);

        if(entityType==0){
            EventModel deleteEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            deleteEventModel.setEntityOwnerId(blogDao.selectBlogById(entityId).getUserId())//发布博客人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(1);//删除博客减分
            eventProducer.fireEvent(deleteEventModel);
        }else if(entityType==1){
            EventModel deleteEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            deleteEventModel.setEntityOwnerId(blogCommentDao.selectBlogCommentById(entityId).getUserId())//发布评论人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(5);//删除评论减分
            eventProducer.fireEvent(deleteEventModel);
        }




        return ResultInfo.ok();
    }

    @Override
    public ResultInfo updateIsTop(int status,int id) {
        try {
            blogDao.updateIsTop(status,id);
            return ResultInfo.ok();
        }catch (Exception e){
            return null;
        }

    }
}
