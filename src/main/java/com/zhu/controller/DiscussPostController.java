package com.zhu.controller;

import com.zhu.entity.Comment;
import com.zhu.entity.DiscussPost;
import com.zhu.entity.Page;
import com.zhu.entity.User;
import com.zhu.service.CommentService;
import com.zhu.service.DiscussPostService;
import com.zhu.service.UserService;
import com.zhu.util.CommunityConstant;
import com.zhu.util.CommunityUtil;
import com.zhu.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    /**
     * 处理ajax请求  新添帖子
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(String title, String content) {
        // 先判断有没有登录
        User user = hostHolder.getUser();
        if(user == null) {
            return CommunityUtil.getJSONString(403,"您还没有登录哦！");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());

        discussPostService.addDiscussPost(post);

        // 报错的情况，之后统一处理
        return CommunityUtil.getJSONString(0,"发布成功");
    }

    // 显示帖子详情
    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

        // 设置page分页信息
        page.setPath("/discuss/detail/" + discussPostId);
        page.setLimit(5);
        page.setRows(post.getCommentCount());
        // 评论：给帖子的评论
        // 回复：给评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        for(Comment comment : commentList) {
            Map<String,Object> commentVo = new HashMap<>();
            // 放入评论
            commentVo.put("comment",comment);
            // 放入作者
            commentVo.put("user",userService.findUserById(comment.getUserId()));

            // 评论的回复
            List<Comment> replyList = commentService.findCommentsByEntity(
                    ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
            // 回复Vo列表
            List<Map<String,Object>> replyVoList = new ArrayList<>();
            for(Comment reply : replyList) {
                Map<String,Object> replyVo = new HashMap<>();
                // 放入回复
                replyVo.put("reply",reply);
                // 放入作者信息
                replyVo.put("user",userService.findUserById(reply.getUserId()));

                // 回复目标
                User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                replyVo.put("target",target);

                replyVoList.add(replyVo);
            }
            commentVo.put("replys",replyVoList);
            // 回复数量
            int replyCount = commentService.findCommnetCount(ENTITY_TYPE_COMMENT,comment.getId());
            commentVo.put("replyCount",replyCount);

            commentVoList.add(commentVo);
        }

        model.addAttribute("comments",commentVoList);

        return "site/discuss-detail";
    }
}
