package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.core.AbstractCoreController;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;

import entity.BCrypt;
import entity.JwtUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController extends AbstractCoreController<User>{

    private UserService  userService;

    @Autowired
    public UserController(UserService  userService) {
        super(userService, User.class);
        this.userService = userService;
    }

    //实现登录的功能
    //接收页面传递的请求 获取到用户名和密码 查询数据库 是否在正确 如果正确 返回 登录成功，如错误 返回错误
    @RequestMapping(value="/login",method = {RequestMethod.GET,RequestMethod.POST})
    public Result login(String username, String password, HttpServletResponse response){
        //1.判断是否为空
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return new Result(false, StatusCode.LOGINERROR,"用户名和密码不能为空");
        }
        //2.判断用户信息是否存在
        User user = userService.selectByPrimaryKey(username);
        if(user==null){
            return new Result(false, StatusCode.LOGINERROR,"用户名或密码错误");
        }
        //3.判断是否密码正确
        if(!BCrypt.checkpw(password,user.getPassword())){
            return new Result(false, StatusCode.LOGINERROR,"用户名或密码错误");
        }
        //4.如果正确 返回 成功true
        //4.1 生成令牌
        Map<String,Object> subObject = new HashMap<>();
        subObject.put("username",username);
        subObject.put("role","ROLE_ADMIN");
        String token = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(subObject), null);
        //4.2 将令牌信息存储到cookie中
        Cookie cookie = new Cookie("Authorization",token);
        cookie.setPath("/");
        response.addCookie(cookie);
        //4.3 将令牌信息返回给前端
        return new Result(true, StatusCode.OK,"登录成功",token);
    }

    public static void main(String[] args)  throws Exception{
        String encode = new BCryptPasswordEncoder().encode("changgou");
        System.out.println(encode);



        //byte[] str = Base64.getDecoder()
       //         .decode("$2a$10$njSeoI2fhKLb29QUJ1i0g.WCsDUZT/tSEM1fZEFxdjN30iPyaPU5.");
        //String s = new String(str, "utf-8");
       // System.out.println(s);

    }
}
