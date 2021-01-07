package com.zhu.dao;
import com.zhu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper 让spring容器接管并实例化该接口
@Mapper
@Repository
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id,int status);

    int updateHeader(int id, String HeaderUrl);

    int updatePassword(int id, String password);

}
