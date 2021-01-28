package com.zhu.service;

import com.zhu.dao.LoginTicketMapper;
import com.zhu.dao.UserMapper;
import com.zhu.entity.LoginTicket;
import com.zhu.entity.User;
import com.zhu.util.CommunityConstant;
import com.zhu.util.CommunityUtil;
import com.zhu.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    //用户注册
    public Map<String,Object> register(User user) {
        //map 用来传递消息
        Map<String,Object> map = new HashMap<>();
        //空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        //common3中的判空函数
        if(StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg","账号名不能为空");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg","密码不能为空");
            return map;
        }

        if(StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg","邮箱不能为空");
            return map;
        }



        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg","该用户名已经注册");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null) {
            map.put("emailMsg","该邮箱已经注册");
            return map;
        }

        //用户注册
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //邮件发送
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //"http://localhost:8080/community/activation/101/code"
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);


        return map;
    }

    public int activation(int id, String code) {
        User user = userMapper.selectById(id);
        if(user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(id, 1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String,Object> login(String username, String password, int expiredSeconds) {
        Map<String,Object> map = new HashMap<>();

        // 空值判断
        if(StringUtils.isBlank(username)) {
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)) {
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        // 验证账号
        User user = userMapper.selectByName(username);
        if(user == null) {
            map.put("usernameMsg","用户名不存在");
            return map;
        }

        // 验证状态
        if(user.getStatus() == 0) {
            map.put("usernameMsg","该账号未被激活");
            return map;
        }

        // 验证密码
        password  = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码错误");
            return map;
        }

        // 生成凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        // 返回ticket
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket,1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId,headerUrl);
    }

    public int updatePassword(int userId, String password) {
        return userMapper.updatePassword(userId,password);
    }

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }
}
