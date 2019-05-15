package com.seu.mstc.interceptor;

import com.seu.mstc.dao.UserDao;
import com.seu.mstc.model.HostHolder;
import com.seu.mstc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lk on 2018/5/1.
 */
//拦截器，对所有的访问用户进行拦截判断
@Component
public class PassportInterceptor implements HandlerInterceptor {


    @Autowired
    private UserDao userDao;

    @Autowired
    private HostHolder hostHolder;

    //所有请求执行之前,返回true才能进行下一步，如果返回false后续的Interceptor 和Controller 都不会再执行，直接结束
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=null;
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("token")){
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if(token!=null){
            User user=null;
            user= userDao.selectByToken(token);
            if(user!=null){
                hostHolder.setUser(user);
            }

        }
        return true;
    }

    //渲染之前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    //结束后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}

