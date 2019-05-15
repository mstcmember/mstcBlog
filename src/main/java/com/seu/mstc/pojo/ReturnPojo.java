package com.seu.mstc.pojo;

import com.seu.mstc.utils.QuestionUtils;

import java.util.HashMap;
import java.util.Map;

public class ReturnPojo {
    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Object object) {
        this.resultMap = QuestionUtils.beanToMap(object);
    }

    public ReturnPojo(Object object){
        this.resultMap = QuestionUtils.beanToMap(object);
    }

    Map<String,Object> resultMap = new HashMap<>();

}
