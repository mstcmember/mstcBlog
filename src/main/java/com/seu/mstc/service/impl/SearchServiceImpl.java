package com.seu.mstc.service.impl;

import com.seu.mstc.dao.*;
import com.seu.mstc.model.*;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lk on 2018-10-12.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    ActivityDao activityDao;

    @Autowired
    ProgrammingDao programmingDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ActivityCommentDao activityCommentDao;

    @Autowired
    ProgrammingCommentDao programmingCommentDao;

    @Autowired
    BlogCommentDao blogCommentDao;

    @Autowired
    QuestionCommentDao questionCommentDao;

    @Autowired
    MyCollectionDao myCollectionDao;

    @Autowired
    ProgrammingService programmingService;

    @Autowired
    ActivityService activityService;

    @Autowired
    QuestionService questionService;

    @Autowired
    BlogService blogService;




    //搜索内容，根据type类型返回不同的内容
    @Override
    public ResultInfo searchByTypeAndKeyword(int type, String keyword, int start, int num) {
        try {
            if(type==-1){
                //全部搜索
                List<Question> questionList=questionDao.selectQuestionByKeywordAll(keyword);//查询所有的
                List<Blog> blogList=blogDao.selectBlogByKeywordAll(keyword);//查询所有的
                List<Programming> programmingList=programmingDao.selectProgrammingByKeywordAll(keyword);//查询所有的
                List<Activity> activityList=activityDao.selectActivityByKeywordAll(keyword);//查询所有的
                int totalCount=questionList.size()+blogList.size()+programmingList.size()+activityList.size();

                if(activityList.size()==0&&programmingList.size()==0&&blogList.size()==0&&questionList.size()==0){
                    //没有搜索到相关结果，返回空，状态码为201
                    return ResultInfo.build(201,"0");
                }

                ResultInfo result=new ResultInfo();

                //最先返回技术讨论帖的结果，再返回博客的结果，再返回编程题的结果，最后返回活动的结果
                List<Object> allListTemp=new ArrayList<>();
                for (int i = 0; i <questionList.size() ; i++) {
                    //加入头像  浏览量   昵称 评论数 创建时间
                    Question question=questionList.get(i);
                    User user=userDao.selectById(question.getUserId());
                    int questionId=question.getId();
                    int  blogCommentNum=blogCommentDao.selectBlogCommentNumByIdType(questionId,0);//评论数量
                    //收藏数量
                    int collectionNum=myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(2,questionId);

                    //创建时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(question.getCreateTime());

                    //浏览量
                    int viewCount=programmingService.getProgrammingViewsCount(questionId);

                    question.setCommentCount(String.valueOf(blogCommentNum));
                    question.setCollectionCount(String.valueOf(collectionNum));
                    question.setCreateTimeStr(timeString);
                    question.setViewCount(viewCount);
                    question.setHeadUrl(user.getHeadUrl());
                    question.setNickname(user.getNickname());
                    question.setEntityType(2);

                    allListTemp.add(question);
                }
                for (int i = 0; i <blogList.size() ; i++) {
                    //加入头像  浏览量   昵称 评论数 创建时间
                    Blog blog=blogList.get(i);
                    User user=userDao.selectById(blog.getUserId());
                    int blogId=blog.getId();
                    int  blogCommentNum=blogCommentDao.selectBlogCommentNumByIdType(blogId,0);//评论数量
                    //收藏数量
                    int collectionNum=myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(1,blogId);

                    //创建时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(blog.getCreateTime());

                    //浏览量
                    int viewCount=programmingService.getProgrammingViewsCount(blogId);

                    blog.setCommentCount(String.valueOf(blogCommentNum));
                    blog.setCollectionCount(String.valueOf(collectionNum));
                    blog.setCreateTimeStr(timeString);
                    blog.setViewCount(viewCount);
                    blog.setHeadUrl(user.getHeadUrl());
                    blog.setNickname(user.getNickname());
                    blog.setEntityType(1);

                    allListTemp.add(blog);
                }
                for (int i = 0; i <programmingList.size() ; i++) {

                    //加入头像  浏览量   昵称 评论数 创建时间
                    Programming programming=programmingList.get(i);
                    User user=userDao.selectById(programming.getUserId());
                    int programmingId=programming.getId();
                    int  programmingCommentNum=programmingCommentDao.selectProgrammingCommentNumByIdType(programmingId,0);//评论数量
                    //收藏数量
                    int collectionNum=myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(0,programmingId);

                    //创建时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(programming.getCreateTime());

                    //浏览量
                    int viewCount=programmingService.getProgrammingViewsCount(programmingId);

                    programming.setCommentCount(String.valueOf(programmingCommentNum));
                    programming.setCollectionCount(String.valueOf(collectionNum));
                    programming.setCreateTimeStr(timeString);
                    programming.setViewCount(viewCount);
                    programming.setHeadUrl(user.getHeadUrl());
                    programming.setNickname(user.getNickname());
                    programming.setEntityType(0);

                    allListTemp.add(programming);

                }
                for (int i = 0; i <activityList.size() ; i++) {
                    //加入头像  浏览量   昵称 评论数 创建时间
                    Activity activity=activityList.get(i);
                    User user=userDao.selectById(activity.getUserId());
                    int activityId=activity.getId();
                    int  blogCommentNum=blogCommentDao.selectBlogCommentNumByIdType(activityId,0);//评论数量
                    //收藏数量
                    int collectionNum=myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(3,activityId);

                    //创建时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(activity.getCreateTime());

                    //浏览量
                    int viewCount=programmingService.getProgrammingViewsCount(activityId);

                    activity.setCommentCount(String.valueOf(blogCommentNum));
                    activity.setCollectionCount(String.valueOf(collectionNum));
                    activity.setCreateTimeStr(timeString);
                    activity.setViewCount(viewCount);
                    activity.setHeadUrl(user.getHeadUrl());
                    activity.setNickname(user.getNickname());
                    activity.setEntityType(3);

                    allListTemp.add(activityId);
                }

                List<Object> resultList=new ArrayList<>();
                if(allListTemp.size()>=start+num){
                    for (int i = start; i <start+num ; i++) {
                        resultList.add(allListTemp.get(i));
                    }
                }else {
                    for (int i = start; i <allListTemp.size() ; i++) {
                        resultList.add(allListTemp.get(i));
                    }
                }


                result.setStatus(200);
                result.setDataList(resultList);
                result.setMsg(String.valueOf(totalCount));//搜索出来的总数量
                return result;
            }

            if(type==0){
                //代表搜索算法题
                List<Programming> programmingList=programmingDao.selectProgrammingByKeyword(keyword,start,num);
                int totalCount=programmingDao.selectProgrammingByKeywordAll(keyword).size();
                if(programmingList.size()==0){
                    //没有搜索到相关结果，返回空，状态码为201
                    return ResultInfo.build(201,"0");
                }

                ResultInfo result=new ResultInfo();
                List<Object> resultList=new ArrayList<>();
                for (int i = 0; i <programmingList.size();  i++) {
                    //加入头像  浏览量   昵称 评论数 创建时间
                    Programming programming=programmingList.get(i);
                    User user=userDao.selectById(programming.getUserId());
                    int programmingId=programming.getId();
                    int  programmingCommentNum=programmingCommentDao.selectProgrammingCommentNumByIdType(programmingId,0);//评论数量
                    //收藏数量
                    int collectionNum=myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(0,programmingId);

                    //创建时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(programming.getCreateTime());

                    //浏览量
                    int viewCount=programmingService.getProgrammingViewsCount(programmingId);

                    programming.setCommentCount(String.valueOf(programmingCommentNum));
                    programming.setCollectionCount(String.valueOf(collectionNum));
                    programming.setCreateTimeStr(timeString);
                    programming.setViewCount(viewCount);
                    programming.setHeadUrl(user.getHeadUrl());
                    programming.setNickname(user.getNickname());
                    programming.setEntityType(0);

                    resultList.add(programming);
                }
                result.setStatus(200);
                result.setDataList(resultList);
                result.setMsg(String.valueOf(totalCount));

                return result;
            }

            if(type==1){
                //代表搜索博文博客
                List<Blog> blogList = blogDao.selectBlogByKeyword(keyword,start,num);
                int totalCount=blogDao.selectBlogByKeywordAll(keyword).size();
                if(blogList.size()==0){
                    //没有搜索到相关结果，返回空，状态码为201
                    return ResultInfo.build(201,"0");
                }

                ResultInfo result=new ResultInfo();
                List<Object> resultList=new ArrayList<>();
                for (int i = 0; i <blogList.size();  i++) {
                    //加入头像  浏览量   昵称 评论数 创建时间
                    Blog blog=blogList.get(i);
                    User user=userDao.selectById(blog.getUserId());
                    int blogId=blog.getId();
                    int  blogCommentNum=blogCommentDao.selectBlogCommentNumByIdType(blogId,0);//评论数量
                    //收藏数量
                    int collectionNum=myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(1,blogId);

                    //创建时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(blog.getCreateTime());

                    //浏览量
                    int viewCount=blogService.getBlogViewsCount(blogId);

                    blog.setCommentCount(String.valueOf(blogCommentNum));
                    blog.setCollectionCount(String.valueOf(collectionNum));
                    blog.setCreateTimeStr(timeString);
                    blog.setViewCount(viewCount);
                    blog.setHeadUrl(user.getHeadUrl());
                    blog.setNickname(user.getNickname());
                    blog.setEntityType(1);

                    resultList.add(blog);
                }

                result.setStatus(200);
                result.setDataList(resultList);
                result.setMsg(String.valueOf(totalCount));

                return result;

            }

            if(type==2){
                //代表搜索技术贴
                List<Question> questionList=questionDao.selectQuestionByKeyword(keyword,start,num);
                int totalCount=questionDao.selectQuestionByKeywordAll(keyword).size();
                if(questionList.size()==0){
                    //没有搜索到相关结果，返回空，状态码为201
                    return ResultInfo.build(201,"0");
                }

                ResultInfo result=new ResultInfo();
                List<Object> resultList=new ArrayList<>();
                for (int i = 0; i <questionList.size();  i++) {
                    //加入头像  浏览量   昵称 评论数 创建时间
                    Question question=questionList.get(i);
                    User user=userDao.selectById(question.getUserId());
                    int questionId=question.getId();
                    int  questionNum=questionCommentDao.selectQuestionCommentNumByIdType(questionId,0);//评论数量
                    //收藏数量
                    int collectionNum=myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(2,questionId);

                    //创建时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(question.getCreateTime());

                    //浏览量
                    int viewCount=questionService.getQuestionViewsCount(questionId);

                    question.setCommentCount(String.valueOf(questionNum));
                    question.setCollectionCount(String.valueOf(collectionNum));
                    question.setCreateTimeStr(timeString);
                    question.setViewCount(viewCount);
                    question.setHeadUrl(user.getHeadUrl());
                    question.setNickname(user.getNickname());
                    question.setEntityType(2);

                    resultList.add(question);
                }

                result.setStatus(200);
                result.setDataList(resultList);
                result.setMsg(String.valueOf(totalCount));

                return result;
            }

            if(type==3){
                //代表搜索活动


                List<Activity> activityList=activityDao.selectActivityByKeyword(keyword,start,num);
                int totalCount=activityDao.selectActivityByKeywordAll(keyword).size();
                if(activityList.size()==0){
                    //没有搜索到相关结果，返回空，状态码为201
                    return ResultInfo.build(201,"0");
                }

                ResultInfo result=new ResultInfo();
                List<Object> resultList=new ArrayList<>();
                for (int i = 0; i <activityList.size();  i++) {
                    //加入头像  浏览量   昵称 评论数 创建时间
                    Activity activity=activityList.get(i);
                    User user=userDao.selectById(activity.getUserId());
                    int activityId=activity.getId();
                    int  activityCommentNum=activityCommentDao.selectActivityCommentNumByIdType(activityId,0);//评论数量
                    //收藏数量
                    int collectionNum=myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(3,activityId);

                    //创建时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(activity.getCreateTime());

                    //浏览量
                    int viewCount=activityService.getActivityViewsCount(activityId);

                    activity.setCommentCount(String.valueOf(activityCommentNum));
                    activity.setCollectionCount(String.valueOf(collectionNum));
                    activity.setCreateTimeStr(timeString);
                    activity.setViewCount(viewCount);
                    activity.setHeadUrl(user.getHeadUrl());
                    activity.setNickname(user.getNickname());
                    activity.setEntityType(3);

                    resultList.add(activity);
                }

                result.setStatus(200);
                result.setDataList(resultList);
                result.setMsg(String.valueOf(totalCount));

                return result;

            }


        }catch (Exception e){
            e.printStackTrace();
            logger.error("搜索出现异常");
            return ResultInfo.build(500,"服务器内部出现错误！");
        }

        return ResultInfo.build(201,"给的搜索范围不对，type值不在指定范围内");
    }

}
