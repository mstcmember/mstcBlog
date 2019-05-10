package com.seu.mstc.service;

import com.seu.mstc.model.News;
import com.seu.mstc.result.ResultInfo;

/**
 * Created by lk on 2018/5/3.
 */
public interface NewsService {

    public ResultInfo addNews(int userId, News news);//发布新闻

}
