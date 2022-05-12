package xyz.ziyublog.yxj.back.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.ziyublog.yxj.back.util.Response;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

@Controller
public class CodeController {
    @Autowired
    private Producer captchaProducer = null;


    @ApiOperation("获取验证图形")
    @CrossOrigin
    @GetMapping("/api/login/loginValidateCode")
    @ResponseBody
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成验证码
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @ApiOperation("检测图形验证码")
    @CrossOrigin
    @GetMapping("/api/login/checkVerify")
    @ResponseBody
    public Response checkLoginValidateCode(HttpSession httpSession, String validateCode) {
        Response response = new Response(0, "failed", null);
        String loginValidateCode = null;
        try{
            loginValidateCode = httpSession.getAttribute(Constants.KAPTCHA_SESSION_KEY).toString();
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