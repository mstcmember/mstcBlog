package com.seu.mstc.async;

import java.util.List;

/**
 * Created by XC on 2018/9/8
 */

//本接口用于定义对事件的各类处理器
public interface EventHandler {
    public void doHandler(EventModel eventModel);//处理事件具体的方法
    public List<EventType> getSupportEventType();//获取处理器能够处理的事件类型

}
