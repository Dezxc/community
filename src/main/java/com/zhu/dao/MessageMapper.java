package com.zhu.dao;

import com.zhu.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {

    // 查询当前用户的会话列表,针对每个会话只返回一条最新的私信.
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量
    int selectConversationCount(int userId);

    // 查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);


    // userId是用户id 同时也是toId 要是别人发给我自己的私信未读数量
    // conversationId为null时，查询的是当前用户的所有的未读私信数量
    // 不为空时，读取的时当前跟某个用户的未读私信数量
    // 查询未读私信的数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 新增私信
    int insertMessage(Message message);

    // 设置已读
    int updateStatus(List<Integer> ids, int status);

    // 查询某个主题下的最新系统通知
    Message selectLatestNotice(int userId, String topic);

    // 查询某个主题的所有通知数量
    int selectNoticeCount(int userId, String topic);

    // 查询未读通知的数量 topic 为null时 查询总的未读系统通知数量
    int selectNoticeUnreadCount(int userId, String topic);

    List<Message> selectNotices(int userId, String topic, int offset, int limit);

}
