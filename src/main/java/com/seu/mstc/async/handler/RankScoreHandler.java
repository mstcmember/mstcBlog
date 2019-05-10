package com.seu.mstc.async.handler;

import com.seu.mstc.async.EventHandler;
import com.seu.mstc.async.EventModel;
import com.seu.mstc.async.EventType;
import com.seu.mstc.dao.UserDao;
import com.seu.mstc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lk on 2018/10/23
 */
//修改用户贡献值的Handler
@Component
public class RankScoreHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(RankScoreHandler.class);

    @Autowired
    UserDao userDao;

    @Override
    public void doHandler(EventModel eventModel) {
        try {
            int userId=eventModel.getEntityOwnerId();//被改变分数的用户id
            int type=eventModel.getEntityType();//代表改变分数的类型，
           /*
            *0代表发布活动、技术讨论帖、博客、编程题等，加5分
            *1代表删除活动、技术讨论帖、博客、编程题等，减5分
            *2代表发布的内容被评论，加0.5分
            *3代表内容的被评论被删除，减0.5分
            *4代表发布评论，加0.5分
            *5代表删除自己发布的评论，减0.5分
            *6代表发布的内容被点赞，给点赞人和被点赞人加1分
            *7代表发布的内容点赞被取消，给点赞人和被点赞人减1分
            *8代表发布的内容被收藏，给收藏人和被收藏人加1分
            *9代表发布的内容收藏被取消，给收藏人和被收藏人减1分
            *
            */

            if(type==0){
            //0代表发布活动、技术讨论帖、博客、编程题等，加5分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore() + 5);
                userDao.updateUserScore(user);
            }
            if(type==1){
            //1代表删除活动、技术讨论帖、博客、编程题等，减5分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore() - 5);
                userDao.updateUserScore(user);
            }

            if(type==2){
            //2代表发布的内容被评论，加0.5分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore() + 0.5);
                userDao.updateUserScore(user);
            }
            if(type==3){
            //3代表内容的被评论被删除，减0.5分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore() - 0.5);
                userDao.updateUserScore(user);
            }
            if(type==4){
            //4代表发布评论，加0.5分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore() + 0.5);
                userDao.updateUserScore(user);
            }
            if(type==5){
            //5代表删除自己发布的评论，减0.5分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore() - 0.5);
                userDao.updateUserScore(user);
            }
            if(type==6){
            //6代表发布的内容被点赞，加1分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore() + 1);
                userDao.updateUserScore(user);
            }
            if(type==7){
            //7代表发布的内容点赞被取消，减1分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore() - 1);
                userDao.updateUserScore(user);
            }
            if(type==8){
            //8代表发布的内容被收藏，加1分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore() + 1);
                userDao.updateUserScore(user);
            }
            if(type==9){
            //9代表发布的内容收藏被取消，减1分
                User user=userDao.selectById(userId);
                user.setRankScore(user.getRankScore()-1);
                userDao.updateUserScore(user);
            }


        }catch (Exception e){
            logger.error("异步修改用户贡献值出错" + e.getMessage());
        }




    }
    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.RANKSCORE);
    }



}
