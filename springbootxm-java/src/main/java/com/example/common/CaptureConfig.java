package com.example.common;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
//定义全局验证码Map
@Component
public class CaptureConfig {
    public static Map<String, String> CAPTURE_MAP = new HashMap<>();

}

