package com.seu.mstc.async.handler;

import com.seu.mstc.async.EventHandler;
import com.seu.mstc.async.EventModel;
import com.seu.mstc.async.EventType;
import com.seu.mstc.dao.SystemMessageDao;
import com.seu.mstc.model.SystemMessage;
import com.seu.mstc.model.User;
import com.seu.mstc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by XC on 2018/10/3
 */
@Component
public class CommentHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommentHandler.class);

    @Autowired
    UserService userService;

    @Autowired
    SystemMessageDao systemMessageDao;

    @Override
    public void doHandler(EventModel eventModel) {
        try {
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setFromId(eventModel.getAuthorId());
            systemMessage.setToId(eventModel.getEntityOwnerId());
            systemMessage.setCreateTime(new Date());
            systemMessage.setEntityId(Integer.parseInt(eventModel.getExt("questionId")));
            systemMessage.setEntityType(Integer.parseInt(eventModel.getExt("entityType")));

            User user = userService.getUserInfoByUserId(systemMessage.getFromId());
            systemMessage.setContent("用户"+user.getNickname()+"评论了你的发言，快去看看吧");
            if(systemMessage.getFromId()!=systemMessage.getToId()){
                systemMessageDao.addSystemMessage(systemMessage);
            }
        }catch (Exception e){
            logger.error("异步评论操作出错" + e.getMessage());
        }



    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.COMMENT);
    }
}
