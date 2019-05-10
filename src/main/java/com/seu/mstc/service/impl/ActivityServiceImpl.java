package com.seu.mstc.service.impl;

import com.seu.mstc.async.EventModel;
import com.seu.mstc.async.EventProducer;
import com.seu.mstc.async.EventType;
import com.seu.mstc.dao.ActivityCommentDao;
import com.seu.mstc.dao.ActivityDao;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.jedis.JedisClient;
import com.seu.mstc.model.Activity;
import com.seu.mstc.model.ActivityComment;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.ActivityService;
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
 * Created by zl on 2018/10/3.
 */
@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    ActivityDao activityDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    ActivityCommentDao activityCommentDao;

    @Autowired
    ActivityService activityService;

    @Autowired
    JedisClient jedisClient;

    @Autowired
    EventProducer eventProducer;


    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    CollectionService collectionService;

    @Override
    public ResultInfo addActivity(Activity activity) {
        ResultInfo result = null;
        activity.setLikeCount(0);
        activity.setDislikeCount(0);                           //添加时赞踩都为0
        activity.setStatus(0);//删除后该字段才置为1
        activity.setIsTop(0);//默认不置顶
        int a=activityDao.addActivity(activity);
        if(a>0)
            result = ResultInfo.ok();



        EventModel addEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        addEventModel.setEntityOwnerId(hostHolder.getUser().getId())//发布活动人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(0);//发布活动加分
        eventProducer.fireEvent(addEventModel);


        return result;
    }

    @Override
    public ResultInfo getActivity(int start,int num) {
        ResultInfo result = null;
        List<Activity> ListActivity = new ArrayList<>();
        int activityCount = 0;
        activityCount = activityDao.selectActivityNum();
        if (start >= activityCount) {
            return ResultInfo.build(999, "end");
        }
        ListActivity = activityDao.selectPageActivity(start, num);
        List<Map<String,Object>> FinalActivitys = new ArrayList<>();
        for (Activity activity : ListActivity) {
            Map<String,Object> activitymap = new HashMap<>();
            activitymap=QuestionUtils.beanToMap(activity);
            //activitymap.put("activity",activity)
            activitymap.put("nickname",userDao.selectById(activity.getUserId()).getNickname());
            activitymap.put("headUrl", userDao.selectById(activity.getUserId()).getHeadUrl());
            activitymap.put("commentCount", activityCommentDao.selectActivityCommentNumByIdType(activity.getId(),0));
            activitymap.put("collectionCount",collectionService.getCountOfCollection(1,activity.getId()));//entityType为1代表活动
            activitymap.put("viewCount",activityService.getActivityViewsCount(activity.getId()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = formatter.format(activity.getCreateTime());
            activitymap.put("createTime",timeString);
            FinalActivitys.add(activitymap);
        }
        result = ResultInfo.build(200,String.valueOf(activityCount),FinalActivitys);
        return result;
    }


    @Override
    public ResultInfo getActivityDetail(int id){
        ResultInfo result = null;
        Activity activity = activityDao.selectActivityById(id);
        Map<String,Object> activitymap = new HashMap<>();
        activitymap=QuestionUtils.beanToMap(activity);
//        activitymap.put("activity",activity);
        activitymap.put("nickname",userDao.selectById(activity.getUserId()).getNickname());
        activitymap.put("headUrl", userDao.selectById(activity.getUserId()).getHeadUrl());
        activitymap.put("commentCount", activityCommentDao.selectActivityCommentNumByIdType(activity.getId(),0));
        activitymap.put("likeCount",activityService.getActivityLikeCount(0,activity.getId()));
        if(hostHolder.getUser()!=null) {
            activitymap.put("isLike", getActivityLikeStatus(0, activity.getId(), hostHolder.getUser().getId()));//0表示是对活动的评论,而不是对评论的评论
            activitymap.put("isCollection",collectionService.isCollection(hostHolder.getUser().getId(),1,activity.getId()));//2代表活动
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = formatter.format(activity.getCreateTime());
        activitymap.put("createTime",timeString);
        Date nowTime=new Date();
        String timeYmd = formatter.format(nowTime).substring(0,10);
        if(hostHolder.getUser()!=null)
            activityService.addActivityViews(id,hostHolder.getUser().getId(),timeYmd);

        result = ResultInfo.ok(activitymap);
        return result;
    }

    @Override
    public ResultInfo getActivityComment(int start,int num,int entityId,int entityType){
        ResultInfo result = null;
        int activityCommentCount = activityCommentDao.selectActivityCommentNumByIdType(entityId,entityType);
        if(start>=activityCommentCount){
            return ResultInfo.build(999,"end");
        }
        List<ActivityComment> ActivityCommentList = activityCommentDao.selectActivityCommentByIdTypeLimit(entityId,entityType,start,num);
//        List<ActivityComment> latestactivityCommentList = new ArrayList<>();
        List<Map<String,Object>> FinalComments = new ArrayList<>();
//        int i =0;
//        if(limit>=ActivityCommentList.size()){
//            limit=ActivityCommentList.size()-1;
//        }
//        for(i=offset;i<=limit;i++){
//            latestactivityCommentList.add(ActivityCommentList.get(i));
//        }
        for(ActivityComment activityComment : ActivityCommentList){
            Map<String,Object> commentmap = new HashMap<>();
            commentmap=QuestionUtils.beanToMap(activityComment);
            commentmap.put("nickname",userDao.selectById(activityComment.getUserId()).getNickname());
            commentmap.put("headUrl",userDao.selectById(activityComment.getUserId()).getHeadUrl());
//          commentmap.put("likeCount",activityService.getActivityLikeCount(activityComment.getEntityType(),activityComment.getId()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(activityComment.getCreateTime());
            commentmap.put("createTime",dateString);

            commentmap.put("replyCount",activityCommentDao.selectActivityCommentNumByIdType(activityComment.getId(),1));
            //以下为选出二级评论

            List<ActivityComment> activityCommentReplyList = activityCommentDao.selectActivityCommentByIdType(activityComment.getId(),1);
            List<Map<String,Object>> FinalCommentsReply = new ArrayList<>();
            for(ActivityComment activityCommentReply : activityCommentReplyList){
                Map<String,Object> commentListmap = new HashMap<>();
                commentListmap=QuestionUtils.beanToMap(activityCommentReply);

                commentListmap.put("nickname",userDao.selectById(activityCommentReply.getUserId()).getNickname());
                commentListmap.put("headUrl",userDao.selectById(activityCommentReply.getUserId()).getHeadUrl());
                commentListmap.put("replyCount",activityCommentDao.selectActivityCommentNumByIdType(activityCommentReply.getId(),1));

                String dateString1 = formatter.format(activityCommentReply.getCreateTime());
                commentListmap.put("createTime",dateString1);
                commentListmap.put("toNickname",userDao.selectById(activityCommentReply.getToUserId()).getNickname());
                FinalCommentsReply.add(commentListmap);
            }
            commentmap.put("activityCommentReplyList",FinalCommentsReply);
            FinalComments.add(commentmap);
        }
        result = ResultInfo.build(200,String.valueOf(activityCommentCount),FinalComments);
        result = ResultInfo.ok(FinalComments);
        return  result;
    }
    @Override
    public int getActivityLikeCount(int entityType,int entityId){
        try {
            return (int) jedisClient.scard(RedisKeyUtil.getActivityLikeKey(entityType, entityId));
        }catch (Exception e){
            return 0;
        }
    }
    @Override
    public ResultInfo addActivityComment(ActivityComment activityComment,int toCommentId){
        activityComment.setCreateTime(new Date());
        activityComment.setStatus(0);
        activityComment.setLikeCount(0);
        activityComment.setDislikeCount(0);
        int entityType=activityComment.getEntityType();
        int entityId=activityComment.getEntityId();
        //activityComment.setContent(HtmlUtils.htmlEscape(activityComment.getContent()));
        activityComment.setContent(sensitiveService.filter(activityComment.getContent()));
        if(activityComment.getEntityType()==1){                //如果是回复评论，需要加上是回复哪一个人的评论
            if(toCommentId>0){
                activityComment.setToUserId(activityCommentDao.selectActivityCommentById(toCommentId).getUserId());
            }else {
                activityComment.setToUserId(activityCommentDao.selectActivityCommentById(activityComment.getEntityId()).getUserId());
            }
        }
        activityCommentDao.addActivityComment(activityComment);

        EventModel eventModel = new EventModel();//向消息队列中添加评论事件
        String entityIdOfMessage;
        int entityOwnerIdOfMessage;
        if(activityComment.getEntityType()==1){  //对评论的评论
            entityOwnerIdOfMessage = activityComment.getToUserId();
            ActivityComment toActivityComment = activityCommentDao.selectActivityCommentById(entityId);
            Activity activity = activityDao.selectActivityById(toActivityComment.getEntityId());
            entityIdOfMessage = String.valueOf(activity.getId());
        }else {  //对问题的评论
            Activity activity = activityDao.selectActivityById(entityId);
            entityOwnerIdOfMessage = activity.getUserId();
            entityIdOfMessage = String.valueOf(activity.getId());
        }
        eventModel.setAuthorId(hostHolder.getUser().getId()) //评论人
                .setEntityId(entityId)//实体id
                .setEntityOwnerId(entityOwnerIdOfMessage)//对谁的帖子或者评论进行评论
                .setEntityType(entityType)//对问题评论还是对评论评论
                .setEventType(EventType.COMMENT)//评论事件
                .setExt("questionId",entityIdOfMessage)//评论事件的技术帖id
                .setExt("entityType","3");//2代表是对技术帖模块的评论
        eventProducer.fireEvent(eventModel);



        EventModel addEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        addEventModel.setEntityOwnerId(activityComment.getUserId())//发起评论人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(2);//评论加分
        eventProducer.fireEvent(addEventModel);

        int userId=0;
        if(activityComment.getEntityType()==0){
            userId=activityDao.selectActivityById(activityComment.getEntityId()).getUserId();
        }
        if(activityComment.getEntityType()==1){
            userId=activityComment.getToUserId();
        }

        addEventModel.setEntityOwnerId(userId)//被评论人或者被回复的人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(2);//评论加分
        eventProducer.fireEvent(addEventModel);



        return ResultInfo.ok();
    }
    //点赞方法，输入点赞的类型，id，用户id，返回当前点赞的数量
    @Override
    public ResultInfo addActivityLike(int entityType,int entityId,int userId){
        jedisClient.sadd(RedisKeyUtil.getActivityLikeKey(entityType,entityId),String.valueOf(userId));
        jedisClient.srem(RedisKeyUtil.getActivityDislikeKey(entityType,entityId),String.valueOf(userId));

        EventModel eventModel = new EventModel();//向消息队列中添加点赞事件
        String entityIdOfMessage;
        int entityOwnerIdOfMessage;
        Activity activity = activityDao.selectActivityById(entityId);
        entityOwnerIdOfMessage = activity.getUserId();
        entityIdOfMessage = String.valueOf(activity.getId());

        eventModel.setAuthorId(hostHolder.getUser().getId()) //点赞人
                .setEntityId(entityId)//实体id
                .setEntityOwnerId(entityOwnerIdOfMessage)//对谁的帖子或者评论进行点赞
                .setEntityType(entityType)//对问题点赞还是对评论点赞，0代表对问题点赞，1代表对评论点赞
                .setEventType(EventType.LIKE)//点赞事件
                .setExt("questionId",entityIdOfMessage)//点赞事件的技术帖id/活动id
                .setExt("entityType","1");//1代表是对活动模块的点赞
        eventProducer.fireEvent(eventModel);





        EventModel likeEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        likeEventModel.setEntityOwnerId(hostHolder.getUser().getId())//点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(6);//点赞加分
        eventProducer.fireEvent(likeEventModel);

        int userIdTemp=0;
        if(entityType==0){
            userIdTemp=activityDao.selectActivityById(entityId).getUserId();//给发布活动的人加分
        }
        if(entityType==1){
            userIdTemp=activityCommentDao.selectActivityCommentById(entityId).getUserId();//给发布评论的人加分
        }

        likeEventModel.setEntityOwnerId(userIdTemp)//被点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(6);//点赞加分
        eventProducer.fireEvent(likeEventModel);




        //活动页面不需要计算热度
//        EventModel hotEventModel = new EventModel();//向消息队列中添加计算热度的事件
//        hotEventModel.setEntityId(entityId)//活动或技术贴的id
//                .setEventType(EventType.CALCULATE)//计算事件
//                .setExt("calculateType","3");//3代表是对活动模块的热度进行计算
//        eventProducer.fireEvent(hotEventModel);

        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getActivityLikeKey(entityType,entityId)));
    }


    //取消点赞
    @Override
    public ResultInfo addActivityDislike(int entityType,int entityId,int userId){
        jedisClient.sadd(RedisKeyUtil.getActivityDislikeKey(entityType,entityId),String.valueOf(userId));
        jedisClient.srem(RedisKeyUtil.getActivityLikeKey(entityType,entityId),String.valueOf(userId));





        EventModel disLikeEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        disLikeEventModel.setEntityOwnerId(hostHolder.getUser().getId())//点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(7);//取消点赞减分
        eventProducer.fireEvent(disLikeEventModel);

        int userIdTemp=0;
        if(entityType==0){
            userIdTemp=activityDao.selectActivityById(entityId).getUserId();//给发布活动的人减分
        }
        if(entityType==1){
            userIdTemp=activityCommentDao.selectActivityCommentById(entityId).getUserId();//给发布评论的人减分
        }

        disLikeEventModel.setEntityOwnerId(userIdTemp)//被点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(7);//取消点赞减分
        eventProducer.fireEvent(disLikeEventModel);



        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getActivityLikeKey(entityType,entityId)));
    }


    @Override
    public ResultInfo addActivityViews(int entityId,int userId,String date){
        jedisClient.sadd(RedisKeyUtil.getActivityViewKey(entityId),String.valueOf(userId)+date);

        //活动页面不用计算热度
//        EventModel hotEventModel = new EventModel();//向消息队列中添加计算热度的事件
//        hotEventModel.setEntityId(entityId)//活动或技术贴的id
//                .setEventType(EventType.CALCULATE)//计算事件
//                .setExt("calculateType","3");//3代表是对活动模块的热度进行计算
//        eventProducer.fireEvent(hotEventModel);

        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getActivityViewKey(entityId)));
    }

    @Override
    public int getActivityViewsCount(int entityId){
        try {
            return (int) jedisClient.scard(RedisKeyUtil.getActivityViewKey(entityId));
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int getActivityLikeStatus(int entityType,int entityId,int userId){
        if(jedisClient.sismember(RedisKeyUtil.getActivityLikeKey(entityType,entityId),String.valueOf(userId))){
            return 1;
        }else if(jedisClient.sismember(RedisKeyUtil.getActivityDislikeKey(entityType,entityId),String.valueOf(userId))) {
            return -1;
        }else{
            return 0;
        }
    }

    @Override
    public ResultInfo deleteActivity(int entityType,int entityId, int userId) {
        EventModel eventModel = new EventModel();//向消息队列中添加删除事件
        if(entityType==0){
            eventModel.setEntityOwnerId(activityDao.selectActivityById(entityId).getUserId());//删除对象的作者的id
        }else{
            eventModel.setEntityOwnerId(activityCommentDao.selectActivityCommentById(entityId).getUserId());//删除对象的作者的id
        }
        eventModel.setAuthorId(userId) //删除的操作人id
                .setEntityId(entityId)//删除对象的id
                .setEntityType(3)//1代表是对活动模块的删除
                .setEventType(EventType.DELETE)//删除事件
                .setExt("entityType", String.valueOf(entityType));//0代表删除活动，1代表删除评论
        eventProducer.fireEvent(eventModel);


        if(entityType==0){
            EventModel deleteEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            deleteEventModel.setEntityOwnerId(activityDao.selectActivityById(entityId).getUserId())//发布活动人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(1);//删除活动减分
            eventProducer.fireEvent(deleteEventModel);
        }else if(entityType==1){
            EventModel deleteEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            deleteEventModel.setEntityOwnerId(activityCommentDao.selectActivityCommentById(entityId).getUserId())//发布评论人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(5);//删除评论减分
            eventProducer.fireEvent(deleteEventModel);
        }



        return ResultInfo.ok();
    }

    @Override
    public ResultInfo updateIsTop(int status,int id) {
        try {
            activityDao.updateIsTop(status,id);
            return ResultInfo.ok();
        }catch (Exception e){
            return null;
        }

    }
}
