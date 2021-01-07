package com.zhu.service;

import com.zhu.dao.UserMapper;
import com.zhu.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

}
