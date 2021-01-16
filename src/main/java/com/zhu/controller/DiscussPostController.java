package com.zhu.controller;

import com.zhu.entity.DiscussPost;
import com.zhu.entity.User;
import com.zhu.service.DiscussPostService;
import com.zhu.service.UserService;
import com.zhu.util.CommunityUtil;
import com.zhu.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     * 处理ajax请求  新添帖子
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(String title, String content) {
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

    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);
        return "site/discuss-detail";
    }
}
