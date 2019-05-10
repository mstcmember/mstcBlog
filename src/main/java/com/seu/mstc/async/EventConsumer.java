package com.seu.mstc.async;

import com.alibaba.fastjson.JSON;
import com.seu.mstc.controller.UserController;
import com.seu.mstc.jedis.JedisClient;
import com.seu.mstc.utils.RedisKeyUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XC on 2018/9/8
 */

//事件的消费者，负责将事件从队列中取出，并调用相关的Handler处理事件,注意，处理事件应另起一个线程，否则就失去了异步队列的意义
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    JedisClient jedisClient;

    private Map<EventType,List<EventHandler>> config= new HashMap<>();   //建立EventType与EventHandler的关系，即确定每一种EventType有哪些Handler处理它，这种对应关系可以通过注册的bean在spring启动时加载，需要该类继承InitalnizingBean接口

    private ApplicationContext applicationContext;





    //初始化时会调用
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);//获得当前上下文中所有EventHandler注册的bean
        if(beans != null){
            for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventType();
                for(EventType type : eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //这个线程负责一直从队列取事件，由于采用的是阻塞操作（brpop），所以直到有事件被取出，否则该线程会一直阻塞
                while(true){
                    String key = RedisKeyUtil.getBizEventKey();
                    List<String> event = jedisClient.brpop(0,key);//将事件从队列中取出，由于redis的实现原因，会先输出key，再输出value
                    for(String msg : event){
                        if(msg.contains(key)){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(msg,EventModel.class); //由于是将转成json串的格式存入redis的，所以这里要将其反射为类,取出之后根据其类型（EventType）取对应关系（EventConsumer中定义的类型与处理函数对应的Map，也就是config）中寻找处理它的Handler
                        if(!config.containsKey(eventModel.getEventType())){
                            logger.error("不存在类型的事件！");
                            continue;
                        }
                        for(EventHandler eventHandler : config.get(eventModel.getEventType())){
                            eventHandler.doHandler(eventModel);
                        }

                    }

                }
            }
        });
        thread.start();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
