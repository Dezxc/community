package com.zhu.dao;

import com.zhu.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {
    // 根据评论类型和评论的对象id 查找Comment列表
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    // 计算评论的数量
    int selectCountByEntity(int entityType, int entityId);

    // 新增帖子
    int insertComment(Comment comment);

    // 根据id 查询评论
    Comment selectCommentById(int id);

}
