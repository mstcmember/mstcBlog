package com.seu.mstc.async.handler;

import com.seu.mstc.async.EventHandler;
import com.seu.mstc.async.EventModel;
import com.seu.mstc.async.EventType;
import com.seu.mstc.dao.BlogDao;
import com.seu.mstc.dao.ProgrammingDao;
import com.seu.mstc.dao.QuestionDao;
import com.seu.mstc.jedis.JedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ZL on 2018/10/7
 */
//处理计算热度事件的Handler
@Component
public class CalculateHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(CalculateHandler.class);

    @Autowired
    JedisClient jedisClient;

    @Autowired
    BlogDao blogDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    ProgrammingDao programmingDao;

    @Override
    public void doHandler(EventModel eventModel) {

        try {
            int calculateType =Integer.parseInt(eventModel.getExt("calculateType"));
            if(calculateType==1) {//表示需要计算的是blog的热度
                int entityId=eventModel.getEntityId();
                String BlogLikeKey="BlogLike:0:"+entityId;//0表示是只考虑对博客或技术贴的点赞情况
                String BlogViewKey="BlogView:"+entityId;
                long LikeCounts=jedisClient.scard(BlogLikeKey);
                long ViewCounts=jedisClient.scard(BlogViewKey);
                double blog_score=calcu(LikeCounts,ViewCounts);
                blogDao.updateHotScore(blog_score,entityId);
            }else if(calculateType==2) {//表示需要计算的是技术帖的热度
                int entityId=eventModel.getEntityId();
                String QuestionLikeKey="QuestionLike:0:"+entityId;//0表示是只考虑对博客或技术贴的点赞情况
                String QuestionViewKey="QuestionView:"+entityId;
                long LikeCounts=jedisClient.scard(QuestionLikeKey);
                long ViewCounts=jedisClient.scard(QuestionViewKey);
                double question_score=calcu(LikeCounts,ViewCounts);
                questionDao.updateHotScore(question_score,entityId);
            }else if(calculateType==0) {//表示需要计算的是算法编程题的热度
                int entityId=eventModel.getEntityId();
                String ProgrammingLikeKey="ProgrammingLike:0:"+entityId;//0表示是只考虑对博客或技术贴的点赞情况
                String ProgrammingViewKey="ProgrammingView:"+entityId;
                long LikeCounts=jedisClient.scard(ProgrammingLikeKey);
                long ViewCounts=jedisClient.scard(ProgrammingViewKey);
                double programming_score=calcu(LikeCounts,ViewCounts);
                programmingDao.updateHotScore(programming_score,entityId);
            }
        }catch (Exception e){
            logger.error("异步计算热度事件操作出错" + e.getMessage());
        }


    }
    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.CALCULATE);
    }

    //帖子的热度=点赞数*2+浏览数*0.5
    private double calcu(Long likecount,Long viewcount){   //分表表示点赞数量和浏览数量
        double score=likecount*2+viewcount*0.5;
        return score;
    }

}
