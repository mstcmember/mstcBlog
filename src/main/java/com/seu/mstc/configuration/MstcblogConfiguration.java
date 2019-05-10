package com.seu.mstc.configuration;

import com.seu.mstc.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Created by lk on 2018/5/1.
 */
@SpringBootConfiguration
public class MstcblogConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;


    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);//在系统初始化时把拦截器加入到整个链路上
        super.addInterceptors(registry);
    }
}
