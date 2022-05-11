package xyz.ziyublog.yxj.back.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.ziyublog.yxj.back.util.RandomValidateCodeUtil;
import xyz.ziyublog.yxj.back.util.Response;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class PicVerifyAction {

    @Resource
    private DefaultKaptcha captchaProducer;
    /**
     * 登录验证码SessionKey
     */
    public static final String LOGIN_VALIDATE_CODE = "login_validate_code";
    /**
     * 登录验证码图片
     */
    @RequestMapping(value = {"/api/login/loginValidateCode"})
    public void loginValidateCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
        RandomValidateCodeUtil.validateCode(request,response,captchaProducer,LOGIN_VALIDATE_CODE);
    }

//    @RequestMapping(value = {"/api/login/checkVerify"})
    @ApiOperation("通过笔记id删除笔记")
    @CrossOrigin
    @GetMapping("/api/login/checkVerify")
    @ResponseBody
    public Response checkLoginValidateCode(HttpServletRequest request,String validateCode) {
        Response response = new Response(0, "failed", null);
        String loginValidateCode = null;
        try{
            loginValidateCode = request.getSession().getAttribute(LOGIN_VALIDATE_CODE).toString();
        }catch (Exception e){

        }

        if(loginValidateCode == null){
            response.setResponse(305,"验证码过期",null);
        }else if(loginValidateCode.equals(validateCode)){
            response.setResponse(200,"验证通过",true);
        }else if(!loginValidateCode.equals(validateCode)){
            response.setResponse(308,"验证码不正确",false);
        }
        return response;
    }

}