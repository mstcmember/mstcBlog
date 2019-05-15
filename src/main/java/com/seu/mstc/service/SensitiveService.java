package com.seu.mstc.service;

public interface SensitiveService {
    public void afterPropertiesSet(String name);//读取预设的敏感词
    public void addTree(String lineTxt);//用读取的敏感词构建字典树
    public String filter(String text);//敏感词筛选
}
