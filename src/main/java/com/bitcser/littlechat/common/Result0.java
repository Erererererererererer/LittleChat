package com.bitcser.littlechat.common;

/**
 * 统一返回的包装类（适用于旧版HandlerWebSocket1）
 */
public class Result0 {

    private String senderId;
    private String receiverId;
    private String code;
    private String msg;
    private Object data;

    public Result0() {

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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public static Result0 success() {
        Result0 result = new Result0();
        result.setCode("200");
        result.setMsg("请求成功");
        return result;
    }

    public static Result0 success(Object data) {
        Result0 result = success();
        result.setData(data);
        return result;
    }

    public static Result0 error() {
        Result0 result = new Result0();
        result.setCode("500");
        result.setMsg("系统错误");
        return result;
    }

    public static Result0 error(String code, String msg) {
        Result0 result = new Result0();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

}
