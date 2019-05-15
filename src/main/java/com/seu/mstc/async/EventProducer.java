package com.seu.mstc.async;

import com.alibaba.fastjson.JSONObject;
import com.seu.mstc.jedis.JedisClient;
import com.seu.mstc.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by XC on 2018/9/8
 */
//本类用于将事件推入队列中
@Service
public class EventProducer {
    @Autowired
    JedisClient jedisClient;

    //此处用redis的list结构
    public boolean fireEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getBizEventKey();
            jedisClient.lpush(key,json);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
