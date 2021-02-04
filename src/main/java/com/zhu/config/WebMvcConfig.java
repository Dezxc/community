package com.zhu.config;

import com.zhu.annotation.LoginRequired;
import com.zhu.controller.interceptor.AlphaInterceptor;
import com.zhu.controller.interceptor.LoginRequiredInterceptor;
import com.zhu.controller.interceptor.LoginTicketInterceptor;
import com.zhu.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //测试拦截器
//       registry.addInterceptor(alphaInterceptor)
//                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
//                .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginTicketInterceptor)
                  .excludePathPatterns("/css/**","/js/**","/img/**");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/css/**","/js/**","/img/**");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/**","/js/**","/img/**");
    }

}
