package com.example.common;
//定义返回结果类型

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
  /*public class Result {
  public static final String SUCCESS = "200";
    public static final String AUTH_ERROR = "401";
    public static final String SYS_ERROR = "500";

    private String code;
    private String msg;
    private Object data;



    public static Result success(){
        return new Result(SUCCESS, "请求成功", null);
    }
    public static Result success(Object data){
        return new Result(SUCCESS, "请求成功", data);
    }
    public static Result error(String msg){
        return new Result(SYS_ERROR, msg, null);
    }

    public static Result error(String code,String msg){
        return new Result(code, msg, null);
    }

    public static Result error(){
        return new Result(SYS_ERROR, "系统错误", null);
    }


}*/


public class Result {
    private static final String SUCCESS = "200";
    private static final String ERROR = "400";

    private String code;
    private String msg;
    private Object data;

    public static Result success(){
        Result result = new Result();
        result.setCode(SUCCESS);
        return result;
    }
    public static Result success(Object data){
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }
    public static Result error(String msg){
        Result result = new Result();
        result.setMsg(ERROR);
        result.setMsg(msg);
        return result;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
