package com.seu.mstc.service;

import com.seu.mstc.model.MyCollection;
import com.seu.mstc.result.ResultInfo;


public interface CollectionService {

    public ResultInfo addCollection(int userId, MyCollection myCollection);//添加收藏
    public int isCollection(int userId,int entityType,int entityId);//判断某用户是否添加了某收藏
    public ResultInfo deleteCollection(int userId,int entityType,int entityId );//删除收藏
    public int getCountOfCollection(int entityType,int entityId );//查询某博客（新闻、帖子……）的收藏数

}
