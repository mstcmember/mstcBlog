package com.seu.mstc.controller;

import com.alibaba.fastjson.JSONObject;
import com.seu.mstc.dao.*;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.model.MyCollection;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.CollectionService;
import com.seu.mstc.service.UserService;
import com.seu.mstc.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by lk on 2018/5/3.
 */
@RestController
@RequestMapping(value="/collection")
public class CollectionController {
    private static final Logger logger = LoggerFactory.getLogger(CollectionController.class);

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CollectionService collectionService;

    @Autowired
    ProgrammingDao programmingDao;

    @Autowired
    ActivityDao activityDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    BlogDao blogDao;


    /**
     * 收藏入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/add",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo addCollection(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int userId = jsonObject.getInteger("userId");
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            String title = "";
            if(entityType==0){
                title = programmingDao.selectProgrammingById(entityId).getTitle();
            }else if(entityType==1){
                title = blogDao.selectBlogById(entityId).getTitle();
            }else if(entityType==2){
                title = questionDao.selectQuestionById(entityId).getTitle();
            }else if(entityType==3){
                title = activityDao.selectActivityById(entityId).getTitle();
            }
            Date createTime = new Date();
            MyCollection myCollection = new MyCollection();
            myCollection.setTitle(title);
            myCollection.setCreateTime(createTime);
            myCollection.setEntityId(entityId);
            myCollection.setEntityType(entityType);
            myCollection.setUserId(userId);
            result= collectionService.addCollection(userId,myCollection);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("收藏入口失败！");
        }
        return  result;
    }


    /**
     * 取消收藏入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/cancel",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo cancelCollection(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int userId = jsonObject.getInteger("userId");
            int entityId = jsonObject.getInteger("entityId");
            int entityType = jsonObject.getInteger("entityType");
            result= collectionService.deleteCollection(userId,entityType,entityId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("取消收藏失败！");
        }
        return result;
    }



}
