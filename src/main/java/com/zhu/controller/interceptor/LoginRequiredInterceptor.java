package com.zhu.controller.interceptor;

import com.zhu.annotation.LoginRequired;
import com.zhu.util.HostHolder;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 拦截那些加@LoginRequired注解的
 * 判断是否已经登录
 */

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 指拦截方法
        if(handler instanceof HandlerMethod){

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 通过反射获取方法 handlerMethod是class类型的对象
            Method method = handlerMethod.getMethod();
            // 通过反射获取注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            // 如果该controller方法有LoginRequired注解 且没有登录 重定向到登录页面
            if(loginRequired != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }

        }

        return true;
    }
}
