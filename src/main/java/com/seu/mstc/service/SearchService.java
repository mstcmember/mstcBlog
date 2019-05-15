package com.seu.mstc.service;

import com.seu.mstc.result.ResultInfo;

/**
 * Created by lk on 2018-10-12.
 */
public interface SearchService {
    public ResultInfo searchByTypeAndKeyword(int type, String keyword, int start, int num);//搜索内容接口
}
