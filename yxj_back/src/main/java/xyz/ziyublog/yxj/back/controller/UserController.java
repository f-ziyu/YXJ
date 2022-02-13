package xyz.ziyublog.yxj.back.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import xyz.ziyublog.yxj.back.pojo.User;
import xyz.ziyublog.yxj.back.response.Response;
import xyz.ziyublog.yxj.back.service.UserService;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping("api/register")
    @ResponseBody
    public String Register(@RequestBody User user){
        String username =user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        System.out.println("-----------------------"+username);
        String password = user.getPassword();

        boolean isExist = userService.isExist(username);
        if(isExist){
            return "该用户（"+username+"）名已经存在";
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithm = "md5";
        String pwdAfterHash = new SimpleHash(algorithm,password,salt,times).toString();
        user.setSalt(salt);
        user.setPassword(pwdAfterHash);
        userService.addUser(user);
        return "注册成功";
    }

    @CrossOrigin
    @PostMapping("api/login")
    @ResponseBody
    public Response login(@RequestBody User user){
        String username = user.getUsername();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, user.getPassword());

        try {
            subject.login(usernamePasswordToken);
            return new Response(200, "success", usernamePasswordToken);
        }catch (AuthenticationException e){
            return new Response(500,"failure",null);
        }
    }
}
