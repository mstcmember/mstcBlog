package com.seu.mstc.model;

import java.util.Date;

/**
 * Created by lk on 2018/4/30.
 */
public class User {
    private int id;
    private String nickname;//昵称
    private String email;
    private String password;
    private String salt;
    private int sex;//性别，1代表男生，2代表女生
    private String headUrl;//头像
    private String phone;
    private Date birthday;
    private String hometown;//地址
    private String school;
    private String department;//学院
    private String degree;//学历
    private String hobby;//兴趣爱好
    private String qq;
    private String wechat;//微信
    private Date registerTime;//注册时间
    private String label;//个人标签
    private String token;//每个人固定的token
    private int follownCount;//粉丝数量
    private int flag;//0代表普通用户，1代表超级管理员，2代表普通管理员
    private double rankScore;//用户贡献值，经验值


    private double scoreRanking;//用户贡献值排名

    public double getScoreRanking() {
        return scoreRanking;
    }

    public void setScoreRanking(double scoreRanking) {
        this.scoreRanking = scoreRanking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getFollownCount() {
        return follownCount;
    }

    public void setFollownCount(int follownCount) {
        this.follownCount = follownCount;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public double getRankScore() {
        return rankScore;
    }

    public void setRankScore(double rankScore) {
        this.rankScore = rankScore;
    }
}
