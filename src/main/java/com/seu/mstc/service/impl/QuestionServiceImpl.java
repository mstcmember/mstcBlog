package com.seu.mstc.service.impl;

import com.seu.mstc.async.EventModel;
import com.seu.mstc.async.EventProducer;
import com.seu.mstc.async.EventType;
import com.seu.mstc.dao.QuestionCommentDao;
import com.seu.mstc.dao.QuestionDao;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.jedis.JedisClient;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.model.Question;
import com.seu.mstc.model.QuestionComment;
import com.seu.mstc.pojo.ReturnPojo;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.CollectionService;
import com.seu.mstc.service.QuestionService;
import com.seu.mstc.service.SensitiveService;
import com.seu.mstc.utils.QuestionUtils;
import com.seu.mstc.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class QuestionServiceImpl implements QuestionService{
    @Autowired
    QuestionDao questionDao;

    @Autowired
    QuestionCommentDao questionCommentDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserDao userDao;

    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    JedisClient jedisClient;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CollectionService collectionService;

    @Override
    public ResultInfo addQuestion(Question question) {
        ResultInfo result = null;
        question.setCreateTime(new Date());
        question.setLikeCount(0);
        question.setDislikeCount(0);                           //添加时赞踩都为0
        question.setIsTop(0);                                  //添加时默认不置顶
        if(hostHolder.getUser()==null){
            question.setUserId(QuestionUtils.ANONYMOUS_USERID);
        }else{
            question.setUserId(hostHolder.getUser().getId());
        }
        //此处做敏感词过滤
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        //question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setContent(sensitiveService.filter(question.getContent()));


        EventModel addEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        addEventModel.setEntityOwnerId(hostHolder.getUser().getId())//发布技术讨论帖人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(0);//发布技术讨论帖加分
        eventProducer.fireEvent(addEventModel);


        if(questionDao.addQuestion(question)>0)
        {
            result=ResultInfo.ok();
        }                                                         //若添加成功则返回状态200
        return result;
    }

    @Override
    public ResultInfo getLatestQuestion(int start,int num,int flag) {
        ResultInfo result = null;
        List<Question> latestQuestion = new ArrayList<>();
        int questionCount = 0;
        if(flag==0) {
            questionCount = questionDao.selectQuestionNum();
            if (start >= questionCount) {
                return ResultInfo.build(999, "end");
            }
            latestQuestion = questionDao.selectLatestQuestionExtByOrder(start, num);
        }else if(flag==1||flag==2||flag==3||flag==4||flag==5||flag==6){
            questionCount = questionDao.selectQuestionNumByFlag(flag);
            if (start >= questionCount) {
                return ResultInfo.build(999, "end");
            }
            latestQuestion = questionDao.selectLatestQuestionExtByFlagByOrder(start, num,flag);
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
        for(Question question : latestQuestion){
            question.setCommentCount(String.valueOf(questionCommentDao.selectQuestionCommentNumByIdType(question.getId(),0)));
            question.setCollectionCount(String.valueOf(collectionService.getCountOfCollection(2,question.getId())));//entityType为2代表技术贴
//            question.setCollectionCount(String.valueOf(0));//entityType为2代表技术贴
//            question.setCommentCount(String.valueOf(0));

            question.setViewCount(questionService.getQuestionViewsCount(question.getId()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(question.getCreateTime());
//            ParsePosition pos = new ParsePosition(8);
//            Date currentTime_2 = formatter.parse(dateString, pos);
            question.setCreateTimeStr(dateString);
        }

        result = ResultInfo.build(200,String.valueOf(questionCount),latestQuestion);
        return result;
    }

    @Override
    public ResultInfo getQuestionDetail(int questionId){
        ResultInfo result = null;
        Question question = questionDao.selectQuestionById(questionId);
        if(question==null){
            return ResultInfo.build(404,"notFound");
        }
        ReturnPojo returnPojo = new ReturnPojo(question);
        returnPojo.getResultMap().put("nickname",userDao.selectById(question.getUserId()).getNickname());
        returnPojo.getResultMap().put("headUrl",userDao.selectById(question.getUserId()).getHeadUrl());
        returnPojo.getResultMap().put("commentCount",questionCommentDao.selectQuestionCommentNumByIdType(question.getId(),0));
        returnPojo.getResultMap().put("questionLikeCount",getQuestionLikeCount(0,question.getId()));
        if(hostHolder.getUser()!=null) {
            returnPojo.getResultMap().put("isLike", getQuestionLikeStatus(0, question.getId(), hostHolder.getUser().getId()));
            returnPojo.getResultMap().put("isCollection",collectionService.isCollection(hostHolder.getUser().getId(),2,questionId));//2代表技术帖
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(question.getCreateTime());
        returnPojo.getResultMap().put("createTime",dateString);
        Date nowTime=new Date();
        String timeYmd = formatter.format(nowTime).substring(0,10);
        if(hostHolder.getUser()!=null)
            questionService.addQuestionViews(questionId,hostHolder.getUser().getId(),timeYmd);
        result = ResultInfo.ok(returnPojo.getResultMap());

        return result;
    }

    @Override
    public ResultInfo addQuestionComment(int entityType,int entityId,int userId,String content,int toCommentId){
        QuestionComment questionComment = new QuestionComment();
        questionComment.setEntityId(entityId);
        questionComment.setEntityType(entityType);
        questionComment.setUserId(userId);
        questionComment.setContent(content);
        if(entityType==1){                //如果是回复评论，需要加上是回复哪一个人的评论
            if(toCommentId>0){
                questionComment.setToUserId(questionCommentDao.selectQuestionCommentById(toCommentId).getUserId());
            }else {
                questionComment.setToUserId(questionCommentDao.selectQuestionCommentById(entityId).getUserId());
            }
        }
        questionComment.setCreateTime(new Date());
        questionComment.setStatus(0);
        questionComment.setLikeCount(0);
        questionComment.setDislikeCount(0);
        //questionComment.setContent(HtmlUtils.htmlEscape(questionComment.getContent()));
        questionComment.setContent(sensitiveService.filter(questionComment.getContent()));
        questionCommentDao.addQuestionComment(questionComment);

        EventModel eventModel = new EventModel();//向消息队列中添加评论事件
        String entityIdOfMessage;
        int entityOwnerIdOfMessage;
        if(entityType==1){  //对评论的评论
            entityOwnerIdOfMessage = questionComment.getToUserId();
            QuestionComment toQuestionComment = questionCommentDao.selectQuestionCommentById(entityId);
            Question question = questionDao.selectQuestionById(toQuestionComment.getEntityId());
            entityIdOfMessage = String.valueOf(question.getId());
        }else {  //对问题的评论
            Question question = questionDao.selectQuestionById(entityId);
            entityOwnerIdOfMessage = question.getUserId();
            entityIdOfMessage = String.valueOf(question.getId());
        }
        eventModel.setAuthorId(hostHolder.getUser().getId()) //评论人
                .setEntityId(entityId)//实体id
                .setEntityOwnerId(entityOwnerIdOfMessage)//对谁的帖子或者评论进行评论
                .setEntityType(entityType)//对问题评论还是对评论评论
                .setEventType(EventType.COMMENT)//评论事件
                .setExt("questionId",entityIdOfMessage)//评论事件的技术帖id
                .setExt("entityType","2");//2代表是对技术帖模块的评论
        eventProducer.fireEvent(eventModel);




        EventModel addEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        addEventModel.setEntityOwnerId(questionComment.getUserId())//发起评论人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(2);//评论加分
        eventProducer.fireEvent(addEventModel);

        int userIdTemp=0;
        if(questionComment.getEntityType()==0){
            //对发布博客的人加分
            userIdTemp=questionDao.selectQuestionById(questionComment.getEntityId()).getUserId();
        }
        if(questionComment.getEntityType()==1){
            //对被回复评论的人加分
            userIdTemp=questionComment.getToUserId();
        }

        addEventModel.setEntityOwnerId(userIdTemp)//被评论人或者被回复的人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(2);//评论加分
        eventProducer.fireEvent(addEventModel);




        return ResultInfo.ok();
    }

    @Override
    public ResultInfo getLatestQuestionComment(int start,int num,int entityId,int entityType){
        ResultInfo result = null;
        int questionCommentCount = questionCommentDao.selectQuestionCommentNumByIdType(entityId,entityType);
        if(start>=questionCommentCount){
            return ResultInfo.build(999,"end");
        }
        List<QuestionComment> latestquestionCommentList = questionCommentDao.selectQuestionCommentByIdTypeLimit(entityId,entityType,start,num);
//        List<QuestionComment> latestquestionCommentList = new ArrayList<>();
//        int i =0;
//        if(limit>=questionCommentList.size()){
//            limit=questionCommentList.size()-1;
//        }
//        for(i=offset;i<=limit;i++){
//            latestquestionCommentList.add(questionCommentList.get(i));
//        }
        List<Map<String,Object>> latestquestionCommentListAdded = new ArrayList<>();
        for(QuestionComment questionComment : latestquestionCommentList){
            ReturnPojo returnPojo = new ReturnPojo(questionComment);
            returnPojo.getResultMap().put("nickname",userDao.selectById(questionComment.getUserId()).getNickname());
            returnPojo.getResultMap().put("headUrl",userDao.selectById(questionComment.getUserId()).getHeadUrl());
            returnPojo.getResultMap().put("likeCount",questionService.getQuestionLikeCount(1,questionComment.getId()));//此处输入的entityType为1是因为要查询的是对评论的点赞状态
            if(hostHolder.getUser()!=null) {
                returnPojo.getResultMap().put("isLike", questionService.getQuestionLikeStatus(1, questionComment.getId(), hostHolder.getUser().getId()));//此处输入的entityType为1是因为要查询的是对评论的点赞状态
            }
            returnPojo.getResultMap().put("replyCount",questionCommentDao.selectQuestionCommentNumByIdType(questionComment.getId(),1));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(questionComment.getCreateTime());
            returnPojo.getResultMap().put("createTime",dateString);

            List<QuestionComment> questionCommentReplyList = questionCommentDao.selectQuestionCommentByIdType(questionComment.getId(),1);
            List<Map<String,Object>> questionCommentReplyListAdded = new ArrayList<>();
            for(QuestionComment questionCommentReply : questionCommentReplyList){
                ReturnPojo returnPojoReply = new ReturnPojo(questionCommentReply);
                returnPojoReply.getResultMap().put("nickname",userDao.selectById(questionCommentReply.getUserId()).getNickname());
                returnPojoReply.getResultMap().put("headUrl",userDao.selectById(questionCommentReply.getUserId()).getHeadUrl());
                returnPojoReply.getResultMap().put("likeCount",questionService.getQuestionLikeCount(1,questionCommentReply.getId()));//此处输入的entityType为1是因为要查询的是对评论的点赞状态
                if(hostHolder.getUser()!=null) {
                    returnPojoReply.getResultMap().put("isLike", questionService.getQuestionLikeStatus(1, questionCommentReply.getId(), hostHolder.getUser().getId()));//此处输入的entityType为1是因为要查询的是对评论的点赞状态
                }
                returnPojoReply.getResultMap().put("replyCount",questionCommentDao.selectQuestionCommentNumByIdType(questionCommentReply.getId(),1));
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString1 = formatter.format(questionCommentReply.getCreateTime());
                returnPojoReply.getResultMap().put("createTime",dateString1);
                returnPojoReply.getResultMap().put("toNickname",userDao.selectById(questionCommentReply.getToUserId()).getNickname());
                questionCommentReplyListAdded.add(returnPojoReply.getResultMap());
            }
            returnPojo.getResultMap().put("questionCommentReplyList",questionCommentReplyListAdded);
            latestquestionCommentListAdded.add(returnPojo.getResultMap());
        }
        result = ResultInfo.build(200,String.valueOf(questionCommentCount),latestquestionCommentListAdded);
        return  result;
    }

    //点赞方法，输入点赞的类型，id，用户id，返回当前点赞的数量
    @Override
    public ResultInfo addQuestionLike(int entityType,int entityId,int userId){
        if(!jedisClient.sismember(RedisKeyUtil.getQuestionLikeKey(entityType,entityId),String.valueOf(userId))){
            EventModel eventModel = new EventModel();//向消息队列中添加点赞事件
            String entityIdOfMessage;
            int entityOwnerIdOfMessage;
            if(entityType==1){  //对评论的点赞
                QuestionComment questionComment = questionCommentDao.selectQuestionCommentById(entityId);
                Question question = questionDao.selectQuestionById(questionComment.getEntityId());
                entityIdOfMessage = String.valueOf(question.getId());
                entityOwnerIdOfMessage = questionComment.getUserId();
            }else {  //对问题的点赞
                Question question = questionDao.selectQuestionById(entityId);
                entityOwnerIdOfMessage = question.getUserId();
                entityIdOfMessage = String.valueOf(question.getId());
            }
            eventModel.setAuthorId(hostHolder.getUser().getId()) //点赞人
                    .setEntityId(entityId)//实体id
                    .setEntityOwnerId(entityOwnerIdOfMessage)//对谁的帖子或者评论进行点赞
                    .setEntityType(entityType)//对问题点赞还是对评论点赞
                    .setEventType(EventType.LIKE)//点赞事件
                    .setExt("questionId",entityIdOfMessage)//点赞事件的技术帖id
                    .setExt("entityType","2");//2代表是对技术帖模块的点赞
            eventProducer.fireEvent(eventModel);

            EventModel hotEventModel = new EventModel();//向消息队列中添加计算热度的事件
            hotEventModel.setEntityId(entityId)//博客或技术贴的id
                    .setEventType(EventType.CALCULATE)//计算事件
                    .setExt("calculateType", "2");//2代表是对技术帖模块的热度进行计算
            eventProducer.fireEvent(hotEventModel);





            EventModel likeEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            likeEventModel.setEntityOwnerId(hostHolder.getUser().getId())//点赞人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(6);//点赞加分
            eventProducer.fireEvent(likeEventModel);

            int userIdTemp=0;
            if(entityType==0){
                userIdTemp=questionDao.selectQuestionById(entityId).getUserId();//给发布技术讨论帖的人加分
            }
            if(entityType==1){
                userIdTemp=questionCommentDao.selectQuestionCommentById(entityId).getUserId();//给发布评论的人加分
            }

            likeEventModel.setEntityOwnerId(userIdTemp)//被点赞人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(6);//点赞加分
            eventProducer.fireEvent(likeEventModel);




        }

        long a = jedisClient.sadd(RedisKeyUtil.getQuestionLikeKey(entityType,entityId),String.valueOf(userId));
        jedisClient.srem(RedisKeyUtil.getQuestionDislikeKey(entityType,entityId),String.valueOf(userId));

        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getQuestionLikeKey(entityType,entityId)));
    }

    @Override
    public ResultInfo addQuestionDislike(int entityType,int entityId,int userId){
        jedisClient.sadd(RedisKeyUtil.getQuestionDislikeKey(entityType,entityId),String.valueOf(userId));
        jedisClient.srem(RedisKeyUtil.getQuestionLikeKey(entityType,entityId),String.valueOf(userId));


        EventModel disLikeEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
        disLikeEventModel.setEntityOwnerId(hostHolder.getUser().getId())//点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(7);//取消点赞减分
        eventProducer.fireEvent(disLikeEventModel);

        int userIdTemp=0;
        if(entityType==0){
            userIdTemp=questionDao.selectQuestionById(entityId).getUserId();//给发布技术讨论帖的人减分
        }
        if(entityType==1){
            userIdTemp=questionCommentDao.selectQuestionCommentById(entityId).getUserId();//给发布评论的人减分
        }

        disLikeEventModel.setEntityOwnerId(userIdTemp)//被点赞人的id
                .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                .setEntityType(7);//取消点赞减分
        eventProducer.fireEvent(disLikeEventModel);





        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getQuestionLikeKey(entityType,entityId)));
    }

    @Override
    public int getQuestionLikeStatus(int entityType,int entityId,int userId){
        if(jedisClient.sismember(RedisKeyUtil.getQuestionLikeKey(entityType,entityId),String.valueOf(userId))){
            return 1;
        }else if(jedisClient.sismember(RedisKeyUtil.getQuestionDislikeKey(entityType,entityId),String.valueOf(userId))) {
            return -1;
        }else{
            return 0;
        }
    }

    @Override
    public int getQuestionLikeCount(int entityType,int entityId){
        try {
            return (int) jedisClient.scard(RedisKeyUtil.getQuestionLikeKey(entityType, entityId));
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public ResultInfo deleteQuestion(int entityType,int entityId, int userId) {
        EventModel eventModel = new EventModel();//向消息队列中添加删除事件
        if(entityType==0){
            eventModel.setEntityOwnerId(questionDao.selectQuestionById(entityId).getUserId());//删除对象的作者的id
        }else{
            eventModel.setEntityOwnerId(questionCommentDao.selectQuestionCommentById(entityId).getUserId());//删除对象的作者的id
        }
        eventModel.setAuthorId(userId) //删除的操作人id
                .setEntityId(entityId)//删除对象的id
                .setEntityType(2)//2代表是对技术帖模块的删除
                .setEventType(EventType.DELETE)//删除事件
                .setExt("entityType", String.valueOf(entityType));//0代表删除问题，1代表删除评论
        eventProducer.fireEvent(eventModel);


        if(entityType==0){
            EventModel deleteEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            deleteEventModel.setEntityOwnerId(questionDao.selectQuestionById(entityId).getUserId())//发布技术讨论帖人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(1);//删除技术讨论帖减分
            eventProducer.fireEvent(deleteEventModel);
        }else if(entityType==1){
            EventModel deleteEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            deleteEventModel.setEntityOwnerId(questionCommentDao.selectQuestionCommentById(entityId).getUserId())//发布评论人的id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(5);//删除评论减分
            eventProducer.fireEvent(deleteEventModel);
        }





        return ResultInfo.ok();
    }

    @Override
    public ResultInfo getHotQuestionDetail() {
        ResultInfo result = null;
        List<Question> ListQuestion = new ArrayList<>();
        ListQuestion = questionDao.selectHotQuestion();
        List<Map<String,Object>> FinalQuestions = new ArrayList<>();
        for (Question question : ListQuestion) {
            Map<String,Object> questionmap = new HashMap<>();
            questionmap=QuestionUtils.beanToMap(question);
            questionmap.put("nickname",userDao.selectById(question.getUserId()).getNickname());
            questionmap.put("headUrl", userDao.selectById(question.getUserId()).getHeadUrl());
            questionmap.put("commentCount", questionCommentDao.selectQuestionCommentNumByIdType(question.getId(),0));
            questionmap.put("collectionCount",collectionService.getCountOfCollection(1,question.getId()));//entityType为1代表博客
            questionmap.put("viewCount",questionService.getQuestionViewsCount(question.getId()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = formatter.format(question.getCreateTime());
            questionmap.put("createTime",timeString);
            FinalQuestions.add(questionmap);
        }
        result = ResultInfo.ok(FinalQuestions);
        return result;
    }

    @Override
    public ResultInfo addQuestionViews(int entityId,int userId,String date){
        jedisClient.sadd(RedisKeyUtil.getQuestionViewKey(entityId),String.valueOf(userId)+date);

        EventModel hotEventModel = new EventModel();//向消息队列中添加计算热度的事件
        hotEventModel.setEntityId(entityId)//博客或技术贴的id
                .setEventType(EventType.CALCULATE)//计算事件
                .setExt("calculateType","2");//2代表是对技术帖模块的热度进行计算
        eventProducer.fireEvent(hotEventModel);

        return ResultInfo.ok(jedisClient.scard(RedisKeyUtil.getQuestionViewKey(entityId)));
    }

    @Override
    public int getQuestionViewsCount(int entityId){
        try {
            return (int) jedisClient.scard(RedisKeyUtil.getQuestionViewKey(entityId));
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public ResultInfo updateIsTop(int status,int id) {
        try {
            questionDao.updateIsTop(status,id);
            return ResultInfo.ok();
        }catch (Exception e){
            return null;
        }
    }
}
