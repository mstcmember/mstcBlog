package com.seu.mstc.service.impl;

import com.seu.mstc.async.EventModel;
import com.seu.mstc.async.EventProducer;
import com.seu.mstc.async.EventType;
import com.seu.mstc.dao.ProgrammingCommentDao;
import com.seu.mstc.dao.ProgrammingDao;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.jedis.JedisClient;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.model.Programming;
import com.seu.mstc.model.ProgrammingComment;
import com.seu.mstc.pojo.ReturnPojo;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.CollectionService;
import com.seu.mstc.service.ProgrammingService;
import com.seu.mstc.service.SensitiveService;
import com.seu.mstc.utils.QuestionUtils;
import com.seu.mstc.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ProgrammingServiceImpl implements ProgrammingService{

    @Autowired
    ProgrammingDao programmingDao;

    @Autowired
    ProgrammingCommentDao programmingCommentDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserDao userDao;

    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    JedisClient jedisClient;

    @Autowired
    ProgrammingService programmingService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CollectionService collectionService;

    @Override
    public ResultInfo addProgramming(Programming programming) {
        ResultInfo result = null;
        programming.setCreateTime(new Date());
        programming.setLikeCount(0);
        programming.setDislikeCount(0);                           //添加时赞踩都为0
        programming.setIsTop(0);                                  //添加时默认不置顶
        programming.setAnswer("");                                //暂时没有答案
        if(hostHolder.getUser()==null){
            programming.setUserId(QuestionUtils.ANONYMOUS_USERID);
        }else{
            programming.setUserId(hostHolder.getUser().getId());
        }
        //此处做敏感词过滤
        programming.setTitle(HtmlUtils.htmlEscape(programming.getTitle()));
        programming.setTitle(sensitiveService.filter(programming.getTitle()));
        //programming.setIdeas(HtmlUtils.htmlEscape(programming.getIdeas()));
        programming.setIdeas(sensitiveService.filter(programming.getIdeas()));



        EventModel addEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        addEventModel.setEntityOwnerId(hostHolder.getUser().getId())//发布编程题人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(0);//发布编程题加分
        eventProducer.fireEvent(addEventModel);




        if(programmingDao.addProgramming(programming)>0)
        {
            result=ResultInfo.ok();
        }                                                         //若添加成功则返回状态200
        return result;
    }

    @Override
    public ResultInfo getLatestProgramming(int start,int num,int flag) {
        ResultInfo result = null;
        List<Programming> latestProgramming = new ArrayList<>();
        int programmingCount = 0;
        if(flag==0) {
            programmingCount = programmingDao.selectProgrammingNum();
            if (start >= programmingCount) {
                return ResultInfo.build(999, "end");
            }
            latestProgramming = programmingDao.selectLatestProgrammingByOrder(start, num);
        }else if(flag==1||flag==2||flag==3||flag==4||flag==5||flag==6){
            programmingCount = programmingDao.selectProgrammingNumByFlag(flag);
            if (start >= programmingCount) {
                return ResultInfo.build(999, "end");
            }
            latestProgramming = programmingDao.selectLatestProgrammingByFlagByOrder(start, num,flag);
        }else{
            return ResultInfo.build(999, "wrongFlag");
        }

//        List<Question> latestQuestion = new ArrayList<>();
//        int i =0;
//        if(limit>=latestQuestionTemp.size()){
//            limit=latestQuestionTemp.size()-1;
//        }
//        for(i=offset;i<=limit;i++){
//            latestQuestion.add(latestQuestionTemp.get(i));
//        }
        List<Map<String,Object>> latestProgrammingAdded = new ArrayList<>();               //返回前端时可额外加字段，这里加了问题发布者的昵称
        for(Programming programming : latestProgramming){
            ReturnPojo returnPojo = new ReturnPojo(programming);
            returnPojo.getResultMap().put("nickname",userDao.selectById(programming.getUserId()).getNickname());
            returnPojo.getResultMap().put("headUrl",userDao.selectById(programming.getUserId()).getHeadUrl());
            returnPojo.getResultMap().put("commentCount",programmingCommentDao.selectProgrammingCommentNumByIdType(programming.getId(),0));
            returnPojo.getResultMap().put("collectionCount",collectionService.getCountOfCollection(0,programming.getId()));//entityType为0代表算法题

            returnPojo.getResultMap().put("viewCount",programmingService.getProgrammingViewsCount(programming.getId()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(programming.getCreateTime());
//            ParsePosition pos = new ParsePosition(8);
//            Date currentTime_2 = formatter.parse(dateString, pos);
            returnPojo.getResultMap().put("createTime",dateString);

            latestProgrammingAdded.add(returnPojo.getResultMap());
        }

        result = ResultInfo.build(200,String.valueOf(programmingCount),latestProgrammingAdded);
        return result;
    }

    @Override
    public ResultInfo getProgrammingDetail(int programmingId){
        ResultInfo result = null;
        Programming programming = programmingDao.selectProgrammingById(programmingId);
        if(programming==null){
            return ResultInfo.build(404,"notFound");
        }
        ReturnPojo returnPojo = new ReturnPojo(programming);
        returnPojo.getResultMap().put("nickname",userDao.selectById(programming.getUserId()).getNickname());
        returnPojo.getResultMap().put("headUrl",userDao.selectById(programming.getUserId()).getHeadUrl());
        returnPojo.getResultMap().put("commentCount",programmingCommentDao.selectProgrammingCommentNumByIdType(programming.getId(),0));
        returnPojo.getResultMap().put("questionLikeCount",getProgrammingLikeCount(0,programming.getId()));
        if(hostHolder.getUser()!=null) {
            returnPojo.getResultMap().put("isLike", getProgrammingLikeStatus(0, programming.getId(), hostHolder.getUser().getId()));
            returnPojo.getResultMap().put("isCollection",collectionService.isCollection(hostHolder.getUser().getId(),0,programmingId));//0代表编程题
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(programming.getCreateTime());
        returnPojo.getResultMap().put("createTime",dateString);

        Date nowTime=new Date();
        String timeYmd = formatter.format(nowTime).substring(0,10);
        if(hostHolder.getUser()!=null)
            programmingService.addProgrammingViews(programmingId,hostHolder.getUser().getId(),timeYmd);
        result = ResultInfo.ok(returnPojo.getResultMap());

        return result;
    }

    @Override
    public ResultInfo addProgrammingComment(int entityType,int entityId,int userId,String content,int toCommentId){
        ProgrammingComment programmingComment = new ProgrammingComment();
        programmingComment.setEntityId(entityId);
        programmingComment.setEntityType(entityType);
        programmingComment.setUserId(userId);
        programmingComment.setContent(content);
        if(entityType==1){                //如果是回复评论，需要加上是回复哪一个人的评论
            if(toCommentId>0){
                programmingComment.setToUserId(programmingCommentDao.selectProgrammingCommentById(toCommentId).getUserId());
            }else {
                programmingComment.setToUserId(programmingCommentDao.selectProgrammingCommentById(entityId).getUserId());
            }
        }
        programmingComment.setCreateTime(new Date());
        programmingComment.setStatus(0);
        programmingComment.setLikeCount(0);
        programmingComment.setDislikeCount(0);
        //programmingComment.setContent(HtmlUtils.htmlEscape(programmingComment.getContent()));
        programmingComment.setContent(sensitiveService.filter(programmingComment.getContent()));
        programmingCommentDao.addProgrammingComment(programmingComment);

        EventModel eventModel = new EventModel();//向消息队列中添加评论事件
        String entityIdOfMessage;
        int entityOwnerIdOfMessage;
        if(entityType==1){  //对评论的评论
            entityOwnerIdOfMessage = programmingComment.getToUserId();
            ProgrammingComment toProgrammingComment = programmingCommentDao.selectProgrammingCommentById(entityId);
            Programming programming = programmingDao.selectProgrammingById(toProgrammingComment.getEntityId());
            entityIdOfMessage = String.valueOf(programming.getId());
        }else {  //对问题的评论
            Programming programming = programmingDao.selectProgrammingById(entityId);
            entityOwnerIdOfMessage = programming.getUserId();
            entityIdOfMessage = String.valueOf(programming.getId());
        }
        eventModel.setAuthorId(hostHolder.getUser().getId()) //评论人
                .setEntityId(entityId)//实体id
                .setEntityOwnerId(entityOwnerIdOfMessage)//对谁的帖子或者评论进行评论
                .setEntityType(entityType)//对算法题评论还是对评论评论
                .setEventType(EventType.COMMENT)//评论事件
                .setExt("questionId",entityIdOfMessage)//评论事件的算法题id
                .setExt("entityType","0");//2代表是对算法题的评论
        eventProducer.fireEvent(eventModel);



        EventModel addEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        addEventModel.setEntityOwnerId(programmingComment.getUserId())//发起评论人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(2);//评论加分
        eventProducer.fireEvent(addEventModel);

        int userIdTemp=0;
        if(programmingComment.getEntityType()==0){
            //对发布编程题的人加分
            userIdTemp=programmingDao.selectProgrammingById(programmingComment.getEntityId()).getUserId();
        }
        if(programmingComment.getEntityType()==1){
            //对被回复评论的人加分
            userIdTemp=programmingComment.getToUserId();
        }

        addEventModel.setEntityOwnerId(userIdTemp)//被评论人或者被回复的人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(2);//评论加分
        eventProducer.fireEvent(addEventModel);





        return ResultInfo.ok();
    }

    @Override
    public ResultInfo getLatestProgrammingComment(int start,int num,int entityId,int entityType){
        ResultInfo result = null;
        int programmingCommentCount = programmingCommentDao.selectProgrammingCommentNumByIdType(entityId,entityType);
        if(start>=programmingCommentCount){
            return ResultInfo.build(999,"end");
        }
        List<ProgrammingComment> latestProgrammingCommentList = programmingCommentDao.selectProgrammingCommentByIdTypeLimit(entityId,entityType,start,num);
//        List<QuestionComment> latestquestionCommentList = new ArrayList<>();
//        int i =0;
//        if(limit>=questionCommentList.size()){
//            limit=questionCommentList.size()-1;
//        }
//        for(i=offset;i<=limit;i++){
//            latestquestionCommentList.add(questionCommentList.get(i));
//        }
        List<Map<String,Object>> latestProgrammingCommentListAdded = new ArrayList<>();
        for(ProgrammingComment programmingComment : latestProgrammingCommentList){
            ReturnPojo returnPojo = new ReturnPojo(programmingComment);
            returnPojo.getResultMap().put("nickname",userDao.selectById(programmingComment.getUserId()).getNickname());
            returnPojo.getResultMap().put("headUrl",userDao.selectById(programmingComment.getUserId()).getHeadUrl());
            returnPojo.getResultMap().put("likeCount",programmingService.getProgrammingLikeCount(1,programmingComment.getId()));//此处输入的entityType为1是因为要查询的是对评论的点赞状态
            if(hostHolder.getUser()!=null) {
                returnPojo.getResultMap().put("isLike", programmingService.getProgrammingLikeStatus(1, programmingComment.getId(), hostHolder.getUser().getId()));//此处输入的entityType为1是因为要查询的是对评论的点赞状态
                returnPojo.getResultMap().put("hostHolderId",hostHolder.getUser().getId());
                returnPojo.getResultMap().put("hostHolderFlag",hostHolder.getUser().getFlag());
            }
            returnPojo.getResultMap().put("replyCount",programmingCommentDao.selectProgrammingCommentNumByIdType(programmingComment.getId(),1));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(programmingComment.getCreateTime());
            returnPojo.getResultMap().put("createTime",dateString);

            List<ProgrammingComment> programmingCommentReplyList = programmingCommentDao.selectProgrammingCommentByIdType(programmingComment.getId(),1);
            List<Map<String,Object>> programmingCommentReplyListAdded = new ArrayList<>();
            for(ProgrammingComment programmingCommentReply : programmingCommentReplyList){
                ReturnPojo returnPojoReply = new ReturnPojo(programmingCommentReply);
                returnPojoReply.getResultMap().put("nickname",userDao.selectById(programmingCommentReply.getUserId()).getNickname());
                returnPojoReply.getResultMap().put("headUrl",userDao.selectById(programmingCommentReply.getUserId()).getHeadUrl());
                returnPojoReply.getResultMap().put("likeCount",programmingService.getProgrammingLikeCount(1,programmingCommentReply.getId()));//此处输入的entityType为1是因为要查询的是对评论的点赞状态
                if(hostHolder.getUser()!=null) {
                    returnPojoReply.getResultMap().put("isLike", programmingService.getProgrammingLikeStatus(1, programmingCommentReply.getId(), hostHolder.getUser().getId()));//此处输入的entityType为1是因为要查询的是对评论的点赞状态
                    returnPojo.getResultMap().put("hostHolderId",hostHolder.getUser().getId());
                }
                returnPojoReply.getResultMap().put("replyCount",programmingCommentDao.selectProgrammingCommentNumByIdType(programmingCommentReply.getId(),1));
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString1 = formatter.format(programmingCommentReply.getCreateTime());
                returnPojoReply.getResultMap().put("createTime",dateString1);
                returnPojoReply.getResultMap().put("toNickname",userDao.selectById(programmingCommentReply.getToUserId()).getNickname());
                programmingCommentReplyListAdded.add(returnPojoReply.getResultMap());
            }
            returnPojo.getResultMap().put("questionCommentReplyList",programmingCommentReplyListAdded);
            latestProgrammingCommentListAdded.add(returnPojo.getResultMap());
        }
        result = ResultInfo.build(200,String.valueOf(programmingCommentCount),latestProgrammingCommentListAdded);
        return  result;
    }

    //点赞方法，输入点赞的类型，id，用户id，返回当前点赞的数量
    @Override
    public ResultInfo addProgrammingLike(int entityType,int entityId,int userId){
        if(!jedisClient.sismember(RedisKeyUtil.getProgrammingLikeKey(entityType,entityId),String.valueOf(userId))){
            EventModel eventModel = new EventModel();//向消息队列中添加点赞事件
            String entityIdOfMessage;
            int entityOwnerIdOfMessage;
            if(entityType==1){  //对评论的点赞
                ProgrammingComment programmingComment = programmingCommentDao.selectProgrammingCommentById(entityId);
                Programming programming = programmingDao.selectProgrammingById(programmingComment.getEntityId());
                entityIdOfMessage = String.valueOf(programming.getId());
                entityOwnerIdOfMessage = programmingComment.getUserId();
            }else {  //对编程题的点赞
                Programming programming = programmingDao.selectProgrammingById(entityId);
                entityOwnerIdOfMessage = programming.getUserId();
                entityIdOfMessage = String.valueOf(programming.getId());
            }
            eventModel.setAuthorId(hostHolder.getUser().getId()) //点赞人
                    .setEntityId(entityId)//实体id
                    .setEntityOwnerId(entityOwnerIdOfMessage)//对谁的帖子或者评论进行点赞
                    .setEntityType(entityType)//对问题点赞还是对评论点赞
                    .setEventType(EventType.LIKE)//点赞事件
                    .setExt("questionId",entityIdOfMessage)//点赞事件的技术帖id
                    .setExt("entityType","0");//0代表是对编程题模块的点赞
            eventProducer.fireEvent(eventModel);



            EventModel likeEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            likeEventModel.setEntityOwnerId(hostHolder.getUser().getId())//点赞人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(6);//点赞加分
            eventProducer.fireEvent(likeEventModel);

            int userIdTemp=0;
            if(entityType==0){
                userIdTemp=programmingDao.selectProgrammingById(entityId).getUserId();//给发布编程题的人加分
            }
            if(entityType==1){
                userIdTemp=programmingCommentDao.selectProgrammingCommentById(entityId).getUserId();//给发布评论的人加分
            }

            likeEventModel.setEntityOwnerId(userIdTemp)//被点赞人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(6);//点赞加分
            eventProducer.fireEvent(likeEventModel);


        }

        long a = jedisClient.sadd(RedisKeyUtil.getProgrammingLikeKey(entityType,entityId),String.valueOf(userId));
        jedisClient.srem(RedisKeyUtil.getProgrammingDislikeKey(entityType,entityId),String.valueOf(userId));

        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getProgrammingLikeKey(entityType,entityId)));
    }

    @Override
    public ResultInfo addProgrammingDislike(int entityType,int entityId,int userId){
        jedisClient.sadd(RedisKeyUtil.getProgrammingDislikeKey(entityType,entityId),String.valueOf(userId));
        jedisClient.srem(RedisKeyUtil.getProgrammingLikeKey(entityType,entityId),String.valueOf(userId));

        EventModel disLikeEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        disLikeEventModel.setEntityOwnerId(hostHolder.getUser().getId())//点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(7);//取消点赞减分
        eventProducer.fireEvent(disLikeEventModel);

        int userIdTemp=0;
        if(entityType==0){
            userIdTemp=programmingDao.selectProgrammingById(entityId).getUserId();//给发布编程题的人减分
        }
        if(entityType==1){
            userIdTemp=programmingCommentDao.selectProgrammingCommentById(entityId).getUserId();//给发布评论的人减分
        }

        disLikeEventModel.setEntityOwnerId(userIdTemp)//被点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(7);//取消点赞减分
        eventProducer.fireEvent(disLikeEventModel);


        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getProgrammingLikeKey(entityType,entityId)));
    }

    @Override
    public int getProgrammingLikeStatus(int entityType,int entityId,int userId){
        if(jedisClient.sismember(RedisKeyUtil.getProgrammingLikeKey(entityType,entityId),String.valueOf(userId))){
            return 1;
        }else if(jedisClient.sismember(RedisKeyUtil.getProgrammingDislikeKey(entityType,entityId),String.valueOf(userId))) {
            return -1;
        }else{
            return 0;
        }
    }

    @Override
    public int getProgrammingLikeCount(int entityType,int entityId){
        try {
            return (int) jedisClient.scard(RedisKeyUtil.getProgrammingLikeKey(entityType, entityId));
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public ResultInfo deleteProgramming(int entityType,int entityId, int userId) {
        EventModel eventModel = new EventModel();//向消息队列中添加删除事件
        if(entityType==0){
            eventModel.setEntityOwnerId(programmingDao.selectProgrammingById(entityId).getUserId());//删除对象的作者的id
        }else{
            eventModel.setEntityOwnerId(programmingCommentDao.selectProgrammingCommentById(entityId).getUserId());//删除对象的作者的id
        }
        eventModel.setAuthorId(userId) //删除的操作人id
                .setEntityId(entityId)//删除对象的id
                .setEntityType(0)//2代表是对编程题模块的删除
                .setEventType(EventType.DELETE)//删除事件
                .setExt("entityType",String.valueOf(entityType));//0代表删除编程题，1代表删除评论
        eventProducer.fireEvent(eventModel);


        if(entityType==0){
            EventModel deleteEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            deleteEventModel.setEntityOwnerId(programmingDao.selectProgrammingById(entityId).getUserId())//发布编程题人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(1);//删除编程题减分
            eventProducer.fireEvent(deleteEventModel);
        }else if(entityType==1){
            EventModel deleteEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            deleteEventModel.setEntityOwnerId(programmingCommentDao.selectProgrammingCommentById(entityId).getUserId())//发布评论人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(5);//删除评论减分
            eventProducer.fireEvent(deleteEventModel);
        }




        return ResultInfo.ok();
    }

    @Override
    public ResultInfo addProgrammingViews(int entityId, int userId, String date) {
        jedisClient.sadd(RedisKeyUtil.getProgrammingViewKey(entityId),String.valueOf(userId)+date);

        EventModel hotEventModel = new EventModel();//向消息队列中添加计算热度的事件
        hotEventModel.setEntityId(entityId)//博客或技术贴的id
                .setEventType(EventType.CALCULATE)//计算事件
                .setExt("calculateType","0");//0代表是对编程题模块的热度进行计算
        eventProducer.fireEvent(hotEventModel);

        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getProgrammingViewKey(entityId)));
    }

    @Override
    public int getProgrammingViewsCount(int entityId) {
        try {
            return (int) jedisClient.scard(RedisKeyUtil.getProgrammingViewKey(entityId));
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public ResultInfo getHotProgrammingDetail() {
        ResultInfo result = null;
        List<Programming> ListProgramming = new ArrayList<>();
        ListProgramming = programmingDao.selectHotProgramming();
        List<Map<String,Object>> FinalProgrammings = new ArrayList<>();
        for (Programming programming : ListProgramming) {
            Map<String,Object> programmingmap = new HashMap<>();
            programmingmap=QuestionUtils.beanToMap(programming);
            programmingmap.put("nickname",userDao.selectById(programming.getUserId()).getNickname());
            programmingmap.put("headUrl", userDao.selectById(programming.getUserId()).getHeadUrl());
            programmingmap.put("commentCount", programmingCommentDao.selectProgrammingCommentNumByIdType(programming.getId(),0));
            programmingmap.put("collectionCount",collectionService.getCountOfCollection(1,programming.getId()));//entityType为1代表博客
            programmingmap.put("viewCount",programmingService.getProgrammingViewsCount(programming.getId()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = formatter.format(programming.getCreateTime());
            programmingmap.put("createTime",timeString);
            FinalProgrammings.add(programmingmap);
        }
        result = ResultInfo.ok(FinalProgrammings);
        return result;
    }

    @Override
    public ResultInfo updateIsTop(int status, int id) {
        try {
            programmingDao.updateIsTop(status,id);
            return ResultInfo.ok();
        }catch (Exception e){
            return null;
        }
    }
}
