package com.example.controller;


import com.example.common.CaptureConfig;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图形验证码
 *
 */
@CrossOrigin//解决跨域问题注解
@RestController
//@RequestMapping()
public class CaptchaController {

    @RequestMapping("captcha")
    public void captcha(@RequestParam String key, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // png类型 100宽度 48高度 5验证码长度
/*        SpecCaptcha captcha = new SpecCaptcha(135,40,1);
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        CaptureConfig.CAPTURE_MAP.put(key,captcha.text().toLowerCase());
        // 输出验证码
        CaptchaUtil.out(captcha,request,response);*/

        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(135, 40);
        captcha.setLen(2);  // 几位数运算，默认是两位
        captcha.getArithmeticString();  // 获取运算的公式
        captcha.text();// 获取运算的结果
        CaptureConfig.CAPTURE_MAP.put(key,captcha.text().toLowerCase());
        CaptchaUtil.out(captcha,request,response);
    }

}

