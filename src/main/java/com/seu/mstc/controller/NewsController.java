package com.seu.mstc.controller;

import com.alibaba.fastjson.JSONObject;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.result.ResultInfo;
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

/**
 * Created by lk on 2018/5/3.
 */
@RestController
@RequestMapping(value="/news")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;


    /**
     * 新闻活动入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/public",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo publicNews(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        //添加业务逻辑和权限判断，调用service层
        return result;
    }


    /**
     * 评论新闻入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/comment",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo commentNews(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        //添加业务逻辑和权限判断，调用service层
        return result;
    }

}
