package com.zhu.service;

import com.zhu.dao.CommentMapper;
import com.zhu.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    public int findCommnetCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }
}
