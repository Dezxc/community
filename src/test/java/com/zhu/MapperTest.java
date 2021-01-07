package com.zhu;


import com.zhu.dao.DiscussPostMapper;
import com.zhu.dao.UserMapper;
import com.zhu.entity.DiscussPost;
import com.zhu.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    DiscussPostMapper discussPostMapper;

    @Autowired
    UserMapper userMapper;

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
}
