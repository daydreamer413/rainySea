package com.example.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "com.example.controller")
public class GlobalExceptionHandler {
    private static final Logger log =  LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //统一异常处理,全局异常捕获器
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request,Exception e){
        log.error("异常信息：",e);
     return Result.error("系统异常");
    }
    //捕获自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result Custom(HttpServletRequest request,CustomException e){
        //System.out.println(e.getMsg());
        return Result.error(e.getMsg());
    }


}
