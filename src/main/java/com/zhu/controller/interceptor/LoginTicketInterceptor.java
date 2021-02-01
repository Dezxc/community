package com.zhu.controller.interceptor;

import com.zhu.entity.LoginTicket;
import com.zhu.entity.User;
import com.zhu.service.UserService;
import com.zhu.util.CookieUtil;
import com.zhu.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookies中的 ticket凭证
        String ticket = CookieUtil.getValue(request,"ticket");

        if(ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 验证凭证
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询出用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 将user放入ThreadLocal中 ThreadLocal中的变量是线程隔离的，不同线程有不同线程的ThreadLocal
                // 在本次请求中持有用户
                hostHolder.setUser(user);
            }



        }

        return true;
    }

    // 在controller方法结束后执行
    // 在模板引擎渲染前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
