package com.seu.mstc.controller;

import com.alibaba.fastjson.JSONObject;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lk on 2018-10-12.
 */
@RestController
@RequestMapping(value ="/search")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);


    @Autowired
    SearchService searchService;

    /**
     * 搜索的入口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/searchResult",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo searchByTypeAndKeyword(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        try {
            int type=jsonObject.getInteger("type");//搜索的内容类型，-1代表全部搜索，0代表算法题，1代表博文，2代表技术贴，3代表活动
            String keywordTemp= jsonObject.getString("keyword");//搜索的内容

            int start=jsonObject.getInteger("start");
            int num=jsonObject.getInteger("num");

            String keyword=keywordTemp.replaceAll(" ","");//去除所有内容中的空格
            result=searchService.searchByTypeAndKeyword(type,keyword,start,num);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("搜索出现异常");
        }
        return result;
    }

}
