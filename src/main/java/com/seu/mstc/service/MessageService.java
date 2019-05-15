package com.seu.mstc.service;

import com.seu.mstc.result.ResultInfo;

import java.util.List;
import java.util.Map;


public interface MessageService {

    public ResultInfo addMessage(int userId);//
    public List<Map> selectSystemMessages(int userId, int limit, int offset); //根据to_id取出当前页
    public List<Integer> systemMessagesAmount(int userId ,int perPageAmount); //总页数，每页条数，总条数
    public ResultInfo setMessagesRead(int messageId);   //将对应message设为已读

}
