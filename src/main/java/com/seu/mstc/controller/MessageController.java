package com.seu.mstc.controller;

import com.alibaba.fastjson.JSONObject;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.model.User;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.MessageService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value="/message")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;


    /**
     * 获取系统通知消息入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/getsystem",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo getSystemMessage(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        //添加业务逻辑和权限判断，调用service层
        return result;
    }


    /**
     * 获取个人收到消息入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/getuser",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo getUserActivity(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        //添加业务逻辑和权限判断，调用service层
        return result;
    }

    /**
     * 获取用户评论信息1(加载页面时获取初始信息:总页数，每页条数，共多少条数据；初始显示的消息)
     */
    @RequestMapping(value="/personalCenter/getUserComment1",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo getUserComment(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            List<List> list = new ArrayList<>();
            User user=hostHolder.getUser();
            int perPageAmount=12;
            list.add(messageService.systemMessagesAmount(user.getId(),perPageAmount));
            list.add(messageService.selectSystemMessages(user.getId(), 1, perPageAmount));
            result=ResultInfo.ok(list);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取用户评论信息1失败！");
        }

        return result;
    }
    /**
     * 获取用户评论信息2（点击页码时获取）
     */
    @RequestMapping(value="/personalCenter/getUserComment2",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo getUserComment2(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            List<Map> list = new ArrayList<>();
            User user=hostHolder.getUser();
            int perPageAmount=12;
            int commentPage = jsonObject.getInteger("commentPage");
            list = messageService.selectSystemMessages(user.getId(),(commentPage-1)*perPageAmount,perPageAmount);
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取用户评论信息2失败！");
        }

        return result;
    }
    /**
     * 用户已读评论
     */
    @RequestMapping(value="/personalCenter/readComment",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo setCommentRead(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        //User user=hostHolder.getUser();
        try {
            int commentId = jsonObject.getInteger("commentId");
            result = messageService.setMessagesRead(commentId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("用户已读评论失败！");
        }

        return result;
    }
    /**
     * 获取用户私信信息
     */
    @RequestMapping(value="/personalCenter/getUserMessage",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo getUserMessage(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        User user=hostHolder.getUser();



        return result;
    }

}
