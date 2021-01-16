package com.zhu;

import com.zhu.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    TemplateEngine templateEngine;

    @Test
    public void TestTextEmail(){
        mailClient.sendMail("1790591566@qq.com", "Test", "hello zhu");
    }

    /**
     * 使用thymeleaf模板引擎，来生成html的页面
     * 发送的邮件是html格式的
     */
    @Test
    public void TestHtmlEmail(){
        Context context = new Context();
        context.setVariable("username","David");

        String content = templateEngine.process("mail/demo",context);
        System.out.println(content);
        mailClient.sendMail("1790591566@qq.com", "Hello", content);
    }
}
