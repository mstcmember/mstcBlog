package com.seu.mstc.service;

import com.seu.mstc.model.User;
import com.seu.mstc.result.ResultInfo;


public interface UserService {

    public ResultInfo register(String email, String password,String token);//注册
    public ResultInfo checkData(String email);//邮箱重复性检查
    public ResultInfo login(String email, String password);//登录
    public ResultInfo getUserPhoto(String username, int type);//获取用户头像
    public ResultInfo updateUserInfo(User user, String token);//更新用户个人信息
    public ResultInfo getUserByToken(String token);//根据Token获取用户信息
    public ResultInfo getUserSettingByUserId(Integer userId);
    public ResultInfo sinOut(String token);//退出登录
    public ResultInfo updatePasswordById(Integer id,String password);//修改用户密码
    public ResultInfo checkPasswordById(Integer id,String password);//验证密码
    public ResultInfo updateEmail(Integer id,String password,String token);//更换邮箱
    public ResultInfo updateBackgroundUrlById(User user,String token);
    public ResultInfo updateHeadUrlById(User user,String headUrl);//更新头像
    public User getUserInfoByUserId(Integer userId);//获取个人信息
    public ResultInfo getUserId();//获取当前用户的id
    public ResultInfo getUser();//获取当前用户
    public ResultInfo retrievePassword(String email, String password);//忘记密码时重新设定密码

    public ResultInfo getUserAllPost(Integer type,Integer userId,Integer start,Integer num);//获取某个用户发布的全部信息

    public ResultInfo getUserAllCollection(Integer type,Integer userId,Integer start,Integer num);//获取某个用户收藏的全部信息

}
