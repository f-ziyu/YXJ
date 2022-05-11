package xyz.ziyublog.yxj.back.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import xyz.ziyublog.yxj.back.util.Response;
import xyz.ziyublog.yxj.back.service.UserService;

@Slf4j
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation("用户注册")
    @CrossOrigin
    @PostMapping("api/user/register")
    @ResponseBody
    public Response Register(@RequestBody User user){
        String username =user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        String password = user.getPassword();
        boolean isExist = userService.isExist(username);
        if(isExist){
            return new Response(201,"该用户（"+username+"）已经存在",null);
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithm = "md5";
        String pwdAfterHash = new SimpleHash(algorithm,password,salt,times).toString();
        user.setSalt(salt);
        user.setPassword(pwdAfterHash);
        userService.addUser(user);
        return new Response(200,"注册成功",null);
    }
    @ApiOperation("用户登录")
    @CrossOrigin
    @PostMapping("api/user/login")
    @ResponseBody
    public Response login(@RequestBody User user){
        try {
            String username = user.getUsername();
            int times = 2;
            String algorithm = "md5";
            if(userService.isExist(username)){
                // 验证旧密码
                User oldUser = userService.getUserByUsername(username);
                String oldSalt = oldUser.getSalt();
                String oldPassword = oldUser.getPassword();
                String PwdAfterHash = new SimpleHash(algorithm,user.getPassword(),oldSalt,times).toString();
                if(oldPassword.equals(PwdAfterHash)){
                    Subject subject = SecurityUtils.getSubject();
                    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, user.getPassword());
                    subject.login(usernamePasswordToken);
                    return new Response(200, "success", usernamePasswordToken);
                }else {
                    return new Response(500,"failure","密码不正确");
                }
            }else {
                return new Response(500,"failure","用户不存在");
            }
        }catch (AuthenticationException e){
            log.info("<< 登陆失败:\n {}", e);
            return new Response(500,"failure",e);
        }
    }
    @ApiOperation("用户修改密码")
    @CrossOrigin
    @PostMapping("api/user/modifyPassword")
    @ResponseBody
    public Response modifyPassword(@RequestBody User user){
        try {
            String username = user.getUsername();
            int times = 2;
            String algorithm = "md5";
            String newPassword = user.getSalt();
            // 验证旧密码
            User oldUser = userService.getUserByUsername(username);
            String oldSalt = oldUser.getSalt();
            String oldPassword = oldUser.getPassword();
            String OldPwdAfterHash = new SimpleHash(algorithm,user.getPassword(),oldSalt,times).toString();
            if(oldPassword.equals(OldPwdAfterHash)){
                // 设置新密码
                String salt = new SecureRandomNumberGenerator().nextBytes().toString();
                String pwdAfterHash = new SimpleHash(algorithm,newPassword,salt,times).toString();
                oldUser.setSalt(salt);
                oldUser.setPassword(pwdAfterHash);
                userService.addUser(oldUser);
                Subject subject = SecurityUtils.getSubject();
                subject.logout();
                return new Response(200, "success", null);
            }else {
                return new Response(500,"failure","旧密码不正确");
            }

        }catch (AuthenticationException e){
            log.info("<< 登陆失败:\n {}", e);
            return new Response(500,"failure",e);
        }
    }

    @ApiOperation("退出登录")
    @CrossOrigin
    @GetMapping("api/logout")
    @ResponseBody
    public Response logout(){
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return new Response(200,"退出登录",null);
        }catch (AuthenticationException e){
            log.info("<< 退出失败:\n {}", e);
            return new Response(500,"failure",e);
        }

    }
}
