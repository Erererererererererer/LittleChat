package com.bitcser.littlechat.common;

import com.bitcser.littlechat.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Receive {

    @Resource
    UserService userService;

    public String module;
    public String service;
    private String params;
    public Map<String, String> paramsMap = new HashMap<>();

    public void splitText(String text) {
        String[] textList = text.split("/");
        this.module = textList[0];
        this.service = textList[1];
        this.params = textList[2];

        this.splitParams();
    }

    private void splitParams() {
        String[] paramList = this.params.split("&");
        for (String param : paramList) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                this.paramsMap.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public Result runService() {
        if (this.module.equals("user")) {
            if (this.service.equals("login")) {
                String userId = this.paramsMap.get("id");
                String password = this.paramsMap.get("password");

                // 登录验证
                // 修改为在线
                userService.updateOnline(Integer.valueOf(userId), true);
                return Result.success();

            }
        } else if (this.module.equals("chat")) {
            if (this.service.equals("chat")) {
                String receiverId = this.paramsMap.get("receiverId");
                String message = this.paramsMap.get("message");
                Result result = Result.success();
                result.setReceiverId(receiverId);
                result.setData(message);
                return result;
            }

        }
        return Result.error();
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }
}
