package com.zhu.controller;

import com.zhu.annotation.LoginRequired;
import com.zhu.entity.User;
import com.zhu.service.LikeService;
import com.zhu.service.UserService;
import com.zhu.util.CommunityUtil;
import com.zhu.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if(headerImage == null) {
            model.addAttribute("error","您还没有选择图片!");
            return "site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        // 获取文件的后缀格式名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)) {
            model.addAttribute("error","文件的格式不正确！");
            return "site/setting";
        }
        // 随机生成文件的名字
        fileName = CommunityUtil.generateUUID() + suffix;

        File dest = new File(uploadPath + "/" + fileName);

        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("文件上传失败 "  + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常" + e);
        }

        // 更新当前用户的头像路径（web访问路径）
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }

    // 获取头像
    @RequestMapping(value = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器文件存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 设置响应内容
        response.setContentType("image/" + suffix);

        try(
                OutputStream os = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
        )
        {
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b=fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }

        } catch (IOException e) {
            logger.error("读取头像失败" + e.getMessage());
        }


    }

    @RequestMapping(path = "/setPassword",method = RequestMethod.POST)
    public String setPassword(String password, String rePassword, String confimPassword, Model model) {
        User user = hostHolder.getUser();

        password = CommunityUtil.md5(password + user.getSalt());

        if(!user.getPassword().equals(password)) {
            model.addAttribute("passwordMsg","密码错误");
            return "site/setting";
        }
        if(!rePassword.equals(confimPassword)) {
            model.addAttribute("confimPasswordMsg","两次输入的密码不一致");
            return "site/setting";
        }
        rePassword = CommunityUtil.md5(rePassword + user.getSalt());
        userService.updatePassword(user.getId(),rePassword);
        return "redirect:/index";

    }

    @RequestMapping(path = "/profile/{userId}",method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        // 用户信息
        User user = userService.findUserById(userId);
        model.addAttribute("user",user);
        
        // 用户点赞信息
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);

        return "/site/profile";

    }

}
