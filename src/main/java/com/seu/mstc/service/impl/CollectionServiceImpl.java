package com.seu.mstc.service.impl;

import com.seu.mstc.async.EventModel;
import com.seu.mstc.async.EventProducer;
import com.seu.mstc.async.EventType;
import com.seu.mstc.dao.*;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.model.MyCollection;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CollectionServiceImpl implements CollectionService{
    @Autowired
    MyCollectionDao myCollectionDao;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    ProgrammingDao programmingDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    ActivityDao activityDao;

    @Autowired
    HostHolder hostHolder;

    @Override
    public ResultInfo addCollection(int userId, MyCollection myCollection) {
        if(isCollection(myCollection.getUserId(),myCollection.getEntityType(),myCollection.getEntityId())>0) {
            return ResultInfo.ok();
        }else{
            if (myCollectionDao.addMyCollection(myCollection) > 0) {

                int userIdTemp=0;
                if(myCollection.getEntityType()==0){
                    //算法编程题id
                    userIdTemp=programmingDao.selectProgrammingById(myCollection.getEntityId()).getUserId();
                }else if(myCollection.getEntityType()==1){
                    //博客的id
                    userIdTemp=blogDao.selectBlogById(myCollection.getEntityId()).getUserId();
                }else if(myCollection.getEntityType()==2){
                    //技术讨论帖的id
                    userIdTemp=questionDao.selectQuestionById(myCollection.getEntityId()).getUserId();
                }else if(myCollection.getEntityType()==3){
                    //活动的id
                    userIdTemp=activityDao.selectActivityById(myCollection.getEntityId()).getUserId();
                }



                EventModel collectionEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
                collectionEventModel.setEntityOwnerId(userIdTemp)//被收藏的内容的发布人id
                        .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                        .setEntityType(8);//收藏加分
                eventProducer.fireEvent(collectionEventModel);

                collectionEventModel.setEntityOwnerId(hostHolder.getUser().getId());
                eventProducer.fireEvent(collectionEventModel);//发起收藏的人也给加分


                return ResultInfo.ok();
            } else {
                return ResultInfo.build(999, "error");
            }
        }
    }

    @Override
    public int isCollection(int userId, int entityType, int entityId) {
        if(myCollectionDao.selectMyCollectionByUserIdEntityTypeEntityId(userId,entityType,entityId) != null){
            return 1;  //返回1代表收藏，返回0代表未收藏
        }else {
            return 0;
        }
    }

    @Override
    public ResultInfo deleteCollection(int userId, int entityType, int entityId) {
        if(myCollectionDao.deleteMyCollectionByUserIdEntityTypeEntityId(userId,entityType,entityId)>0){


            int userIdTemp=0;
            if(entityType==0){
                //算法编程题id
                userIdTemp=programmingDao.selectProgrammingById(entityId).getUserId();
            }else if(entityType==1){
                //博客的id
                userIdTemp=blogDao.selectBlogById(entityId).getUserId();
            }else if(entityType==2){
                //技术讨论帖的id
                userIdTemp=questionDao.selectQuestionById(entityId).getUserId();
            }else if(entityType==3){
                //活动的id
                userIdTemp=activityDao.selectActivityById(entityId).getUserId();
            }



            EventModel deleteCollectionEventModel = new EventModel();//向消息队列中添加改变用户贡献值的事件
            deleteCollectionEventModel.setEntityOwnerId(userIdTemp)//被取消收藏的内容的发布人id
                    .setEventType(EventType.RANKSCORE)//修改用户贡献值事件
                    .setEntityType(9);//取消收藏减分
            eventProducer.fireEvent(deleteCollectionEventModel);

            deleteCollectionEventModel.setEntityOwnerId(hostHolder.getUser().getId());
            eventProducer.fireEvent(deleteCollectionEventModel);//发起收藏的人也给减分

            return ResultInfo.ok();
        }else{
            return ResultInfo.build(999,"error");
        }
    }

    @Override
    public int getCountOfCollection(int entityType, int entityId) {
        return myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(entityType,entityId);
    }

}
