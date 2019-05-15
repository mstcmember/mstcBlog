package com.seu.mstc.service.impl;

import com.seu.mstc.dao.SystemMessageDao;
import com.seu.mstc.model.SystemMessage;
import com.seu.mstc.pojo.ReturnPojo;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    SystemMessageDao systemMessageDao;

    @Override
    public ResultInfo addMessage(int userId) {
        return null;
    }

    @Override
    public List<Map> selectSystemMessages(int userId, int limit, int offset){    //limit是从0开始的
        //List<List> list = new ArrayList<>();                           //返回的list
        List<SystemMessage> listMessage = new ArrayList<>();           //
        List<Map> listMap = new ArrayList<>();                         //加了日期string的messagelist
        // List<Integer> listAmount = new ArrayList<>();                   //当前显示的页目录
        //List<Integer> listActive = new ArrayList<>();                    //当前显示的页,前箭头，后箭头
        listMessage = systemMessageDao.selectSystemMessageByToId(userId, limit-1, offset);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(listMessage!=null) {
            for (int i = 0 ; i < listMessage.size() ; i++){
                String dateString = formatter.format(listMessage.get(i).getCreateTime());
                ReturnPojo returnPojo = new ReturnPojo(listMessage.get(i));
                returnPojo.getResultMap().put("createTimeString", dateString);
                listMap.add(returnPojo.getResultMap());
            }
            //listAmount=systemMessagesAmount(userId,limit);
            //listActive.add(limit+1);               //当前页
            //listActive.add(listAmount.get(0));    //前箭头
            //listActive.add(listAmount.get(listAmount.size()-1));    //后箭头
            //listAmount.remove(listAmount.size()-1);
            //listAmount.remove(0);
            //list.add(listAmount);
            //list.add(listMap);
            //list.add(listActive);
            return listMap;            //返回一个元素是map的list
        }
        else {
            Map<Integer, String> map = new HashMap<>();
            map.put(999, "读取动态失败");
            listMap.add(map);
            return listMap;
        }
    }
    @Override
    public List<Integer> systemMessagesAmount(int userId,int perPageAmount ){
        int messageAmount = systemMessageDao.systemMessageAmount(userId);
        int pageTotal = messageAmount/perPageAmount+1;        //一共amount页
        List<Integer> result = new ArrayList<Integer>();
        result.add(pageTotal);
        result.add(perPageAmount);
        result.add(messageAmount);
        return result;
//        int activePage = limit + 1;
//        if(activePage == 1 || activePage == 2 || activePage == 3 ){
//            result.add(0);       //前向箭头不可点
//            for(int i=1 ; i<= 5; i++){
//                if (i<=amount)
//                    result.add(i);
//            }
//        }
//        else {
//            result.add(1);
//            for (int i = activePage - 2; i <= activePage + 2; i++) {
//                if (i<=amount)
//                    result.add(i);
//            }
//        }
//        if (result.size()==6 && result.get(5)<amount)
//            result.add(1);    //后向箭头可点
//        else
//            result.add(0);
//        return result;
    }

    @Override
    public ResultInfo setMessagesRead(int messageId){
        SystemMessage systemMessage =systemMessageDao.selectSystemMessageById(messageId);
        systemMessageDao.setMessagesRead(systemMessage);
        return ResultInfo.ok();
    }

}
