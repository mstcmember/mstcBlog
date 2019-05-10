package com.seu.mstc.service.impl;

import com.seu.mstc.dao.*;
import com.seu.mstc.jedis.JedisClient;
import com.seu.mstc.model.*;
import com.seu.mstc.pojo.ReturnPojo;
import com.seu.mstc.result.ResultInfo;
import com.seu.mstc.service.*;
import com.seu.mstc.utils.Md5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    //session 的过期时间
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Autowired
    UserDao userDao;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    ActivityDao activityDao;

    @Autowired
    ProgrammingDao programmingDao;

    @Autowired
    BlogDao blogDao;

    @Autowired
    QuestionDao questionDao;

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


    //邮箱重复性检查
    @Override
    public ResultInfo checkData(String email) {
        User user=null;
        user=userDao.selectByEmail(email);
        if(user==null){
            return ResultInfo.ok(false);
        }
        return ResultInfo.ok(true);
    }

    //用户注册
    @Override
    public ResultInfo register(String email, String password,String token) {
        if(StringUtils.isBlank(email)|| StringUtils.isBlank(password))
            return ResultInfo.build(400, "用户数据不完整，注册失败");

        User user=userDao.selectByEmail(email);
        if(user!=null){
            return ResultInfo.build(400,"此邮箱已经被注册");
        }

        Date date=new Date();
        user=new User();
        user.setNickname(email);
        user.setHometown("江苏省 南京市");
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));//随机生成一段盐存入数据库
        user.setEmail(email);
        user.setPassword(Md5Util.MD5(password + user.getSalt()));//存入密码加盐后的加密密文
        user.setRegisterTime(date);
        user.setToken(token);
        user.setHeadUrl("images/niming.jpg");
        userDao.addUser(user);

        return ResultInfo.ok(user.getId());
    }



    //登录
    @Override
    public ResultInfo login(String email, String password) {
        User user=null;
        user=userDao.selectByEmail(email);

        if(user==null){
            return ResultInfo.build(400,"用户名或密码错误！！");
        }
        if(!Md5Util.MD5(password+user.getSalt()).equals(user.getPassword())){
            String str = Md5Util.MD5(password+user.getSalt());
            return ResultInfo.build(400,"用户名或密码错误！");
        }

        return ResultInfo.ok();
    }

    @Override
    public ResultInfo getUserPhoto(String username, int type) {
        return null;
    }

    @Override
    public ResultInfo updateUserInfo(User user, String token) {
        if(userDao.updateUserInfoByToken(user)>0){
            return ResultInfo.ok();
        }
        return null;
    }

    @Override
    public ResultInfo getUserId(){
        int userId = -1;
        if(hostHolder.getUser()!=null){
            userId = hostHolder.getUser().getId();
        }
        return ResultInfo.ok(userId);
    }

    @Override
    public ResultInfo getUser(){
        User user = null;
        if(hostHolder.getUser()!=null){
            user = hostHolder.getUser();
            ReturnPojo returnPojo = new ReturnPojo(user);

            if (user.getBirthday()!=null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(user.getBirthday());
                returnPojo.getResultMap().put("birthdayString", dateString);
            }
            else{
                returnPojo.getResultMap().put("birthdayString", "1995-03-11");
            }
            return ResultInfo.ok(returnPojo.getResultMap());
        }
        return ResultInfo.build(999,"not login in");
    }

    //根据Token获取个人信息
    @Override
    public ResultInfo getUserByToken(String token) {
        return null;
    }

    @Override
    public ResultInfo getUserSettingByUserId(Integer userId) {
        return null;
    }

    //退出登录
    @Override
    public ResultInfo sinOut(String token) {
        return null;
    }

    //更新密码
    @Override
    public ResultInfo updatePasswordById(Integer id, String password) { return null; }


    //检查密码的正确性
    @Override
    public ResultInfo checkPasswordById(Integer id, String password) {
        return null;
    }

    //更新邮箱
    @Override
    public ResultInfo updateEmail(Integer id, String password, String token) {
        return null;
    }

    @Override
    public ResultInfo updateBackgroundUrlById(User user, String token) {
        return null;
    }

    //更新头像
    @Override
    public ResultInfo updateHeadUrlById(User user,String headUrl) {
        user.setHeadUrl(headUrl);
        if (userDao.updateHeadUrl(user)==1)
            return new ResultInfo().ok();
        return null;
    }

    @Override
    public User getUserInfoByUserId(Integer userId) {
        return userDao.selectById(userId);
    }

    @Override
    public ResultInfo retrievePassword(String email, String password){
        if(StringUtils.isBlank(email)|| StringUtils.isBlank(password))
            return ResultInfo.build(400, "用户数据不完整，更改密码失败");

        User user=userDao.selectByEmail(email);
        if(user==null){
            return ResultInfo.build(400,"此邮箱未注册");
        }
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));//随机生成一段盐存入数据库
        user.setPassword(Md5Util.MD5(password + user.getSalt()));//存入密码加盐后的加密密文
        userDao.updatePassword(user);
        return ResultInfo.ok(user.getId());
    }


    //获取某个用户发布的全部信息
    @Override
    public ResultInfo getUserAllPost(Integer type, Integer userId, Integer start, Integer num) {

        try{
            int isLoginUserflag=0;//传进来的id 是否是登录的用户标志，为0则是，为1则不是
            if(userId!=hostHolder.getUser().getId()){
                isLoginUserflag=1;
            }

            if(type==0){
                //返回算法题
                List<Programming> programmingList=programmingDao.selectProgrammingByUserId(userId, start, num);
                int totalCount=programmingDao.getProgrammingCountByUserId(userId);
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

                    resultList.add(programming);
                }
                result.setStatus(200);
                result.setDataList(resultList);
                result.setMsg(String.valueOf(totalCount));

                return result;
            }

            if(type==1){
                //返回博文
                //加上是否私密判断
                List<Blog> blogList=null;
                int totalCount=0;

                if(isLoginUserflag==0){
                    //返回所有的，即查看个人的发布信息
                    blogList=blogDao.selectBlogByUserId(userId,start,num);
                    totalCount=blogDao.getBlogCountByUserId(userId);
                }

                if(isLoginUserflag==1){
                    //返回不带私密的，即查看他人的发布信息,取出status=0的，即公开的博客
                    blogList=blogDao.selectNoPrivateBlogByUserId(userId,start,num,0);
                    totalCount=blogDao.getNoPrivateBlogCountByUserId(userId,0);
                }


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

                    resultList.add(blog);
                }

                result.setStatus(200);
                result.setDataList(resultList);
                result.setMsg(String.valueOf(totalCount));

                return result;
            }

            if(type==2){
                //返回技术讨论帖

                List<Question> questionList=questionDao.selectQuestionByUserId(userId,start,num);
                int totalCount=questionDao.getQuestionCountByUserId(userId);

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
                    int  questionCommentNum=questionCommentDao.selectQuestionCommentNumByIdType(questionId,0);//评论数量
                    //收藏数量
                    int collectionNum=myCollectionDao.selectMyCollectionCountByEntityTypeEntityId(2,questionId);

                    //创建时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeString = formatter.format(question.getCreateTime());

                    //浏览量
                    int viewCount=questionService.getQuestionViewsCount(questionId);

                    question.setCommentCount(String.valueOf(questionCommentNum));
                    question.setCollectionCount(String.valueOf(collectionNum));
                    question.setCreateTimeStr(timeString);
                    question.setViewCount(viewCount);
                    question.setHeadUrl(user.getHeadUrl());
                    question.setNickname(user.getNickname());

                    resultList.add(question);
                }

                result.setStatus(200);
                result.setDataList(resultList);
                result.setMsg(String.valueOf(totalCount));

                return result;

            }

            if(type==3){
                //返回活动

                List<Activity> activityList=activityDao.selectActivityByUserId(userId, start, num);
                int totalCount=activityDao.getActivityCountByUserId(userId);
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

                    resultList.add(activity);
                }

                result.setStatus(200);
                result.setDataList(resultList);
                result.setMsg(String.valueOf(totalCount));

                return result;

            }


        }catch (Exception e){
            logger.error("获取某个用户发布的全部信息出现异常");
            return ResultInfo.build(500,"服务器内部出现错误！");
        }


        return ResultInfo.build(201,"给的查询范围不对，type值不在指定范围内");
    }


    //获取某个用户收藏的全部信息
    @Override
    public ResultInfo getUserAllCollection(Integer type, Integer userId, Integer start, Integer num) {
        ResultInfo result=new ResultInfo();

        List<MyCollection> myCollectionList=myCollectionDao.selectMyCollectionByUserIdEntityType(userId,type,start,num);
        int totalCount=myCollectionDao.getMyCollectionCountByUserIdEntityType(userId, type);
        List<Object> resultList=new ArrayList<>();

        for (int i = 0; i <myCollectionList.size() ; i++) {
            //创建时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = formatter.format(myCollectionList.get(i).getCreateTime());
            myCollectionList.get(i).setTimeView(timeString);
            resultList.add(myCollectionList.get(i));
        }
        result.setStatus(200);
        result.setDataList(resultList);
        result.setMsg(String.valueOf(totalCount));

        return result;
    }
}
