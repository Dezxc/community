package com.zhu.dao;

import com.zhu.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DiscussPostMapper {
    /**
     *
     * @param userId   = 0 时输出所有的帖子  不为0时输出该id用户的所有帖子
     * @param offset  分页的第一个参数  表示从总数的第几个开始输出
     * @param limit   分页的第二个参数  表示每页的个数
     * @return
     */
    List<DiscussPost> selectDiscussPosts (int userId, int offset,int limit);

    //计算帖子的总数  userId = 0 时，计算所有的帖子总数1
    //@Param 用于给参数起别名
    //如果只有一个参数，而且在动态sql<if> 里使用，则必须起别名
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost post);

    DiscussPost selectDiscussPostById(int id);

    // 设置一个帖子的评论数
    int updateCommentCount(int id,int commentCount);
}
