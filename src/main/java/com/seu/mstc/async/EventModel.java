package com.seu.mstc.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XC on 2018/9/8
 */

//本类用于构造事件的实体
public class EventModel {
    private EventType eventType;//事件的类型，用于判断这是何种事件
    private int authorId;//事件发起者
    private int entityId;//事件实体的id
    private int entityType;//事件实体的类型，（0代表算法题，1代表博文，2代表技术贴，3代表活动，4代表新闻）
    private int entityOwnerId;//事件实体的拥有者，例如该事件为对评论的点赞事件，则该变量可表示发表该评论的用户

    private Map<String,String> exts = new HashMap<>();//定义扩展的字段，方便今后向事件的实体中添加新的字段

    public EventModel(){

    }

    public EventModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getAuthorId() {
        return authorId;
    }

    public EventModel setAuthorId(int authorId) {
        this.authorId = authorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
