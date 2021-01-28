package com.zhu;


import com.zhu.dao.DiscussPostMapper;
import com.zhu.dao.LoginTicketMapper;
import com.zhu.dao.MessageMapper;
import com.zhu.dao.UserMapper;
import com.zhu.entity.DiscussPost;
import com.zhu.entity.LoginTicket;
import com.zhu.entity.Message;
import com.zhu.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void TestDiscussPost(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for(DiscussPost post : discussPosts){
            System.out.println(post);
        }
        int i = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(i);
    }

    @Test
    public void TestSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);

    }

    @Test
    public void TestInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        // 由于配置mybatis.configuration.useGeneratedKeys=true
        // 所以插入对象之后 mybatis自动帮我们将id set到对象中了1
        System.out.println(user.getId());
    }

    @Test
    public void TestUpdateUser(){

        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150,"hello");
        System.out.println(rows);
    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(loginTicket.getId());
    }

    @Test
    public void selectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc",1);

        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

    }

    @Test
    public void selectMessageTest() {
//        List<Message> messages = messageMapper.selectConversations(111, 0, Integer.MAX_VALUE);
//        for(Message message : messages) {
//            System.out.println(message);
//        }
        int i = messageMapper.selectConversationCount(111);
        System.out.println(i);
    }

    @Test
    public void selectMessageTest2() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters( "111_112", 0, 10);
        for (Message message : list) {
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);

    }


}
