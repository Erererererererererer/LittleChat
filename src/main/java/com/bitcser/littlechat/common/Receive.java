package com.bitcser.littlechat.common;

import com.bitcser.littlechat.entity.ChatRecord;
import com.bitcser.littlechat.entity.Friend;
import com.bitcser.littlechat.entity.User;
import com.bitcser.littlechat.service.ChatRecordSevice;
import com.bitcser.littlechat.service.FriendService;
import com.bitcser.littlechat.service.MessageService;
import com.bitcser.littlechat.service.UserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// 控制服务调用，适用于HandlerWebSocket1
@Component
public class Receive {

    @Resource
    UserService userService;

    @Resource
    FriendService friendService;

    @Resource
    MessageService messageService;

    @Resource
    ChatRecordSevice chatRecordSevice;

    private static final Logger logger = LoggerFactory.getLogger(Receive.class);

    public String module;
    public String service;
    private String params;
    public Map<String, String> paramsMap = new HashMap<>();

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

    private void splitParams() {
        String[] paramList = this.params.split("&");
        for (String param : paramList) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                this.paramsMap.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public void splitText(String text) {
        String[] textList = text.split("/");
        this.module = textList[0];
        this.service = textList[1];
        this.params = textList[2];

        this.splitParams();
    }

    public Result runService() {
        try {
            switch (this.module) {
                case "user": {
                    switch (this.service) {
                        case "register": { // 注册,
                            String username = this.paramsMap.get("username");
                            String phone = this.paramsMap.get("phone");
                            User user = new User(null, username, this.paramsMap.get("password"),
                                    null, this.paramsMap.get("email"), phone,
                                    Integer.valueOf(this.paramsMap.get("gender")), 0);

                            // 验证
                            if (userService.findByUsername(username) != null) {
                                return Result.error("400", "注册失败，用户名重复");
                            }
                            if (phone.length() != 11 || phone.charAt(0) != '1') {
                                return Result.error("400", "注册失败，手机号错误");
                            }
                            if (userService.findByPhone(phone) != null) {
                                return Result.error("400", "注册失败，手机号重复");
                            }

                            userService.add(user);
                            // 根据username查询该用户的ID并返回
                            String userID = userService.findByUsername(username).getId().toString();
                            return Result.success(userID);
                        }

                        case "login": { // 登录
                            String userId = this.paramsMap.get("id");
                            String password = this.paramsMap.get("password");

                            // 登录验证
                            if (userService.checkLogin(Integer.valueOf(userId), password)) {
                                // 登录成功，修改为在线
                                userService.updateOnline(Integer.valueOf(userId), true);
                                return Result.success();
                            } else {
                                // 登录失败
                                return Result.error("400", "密码不正确");
                            }
                        }

                    }
                }
                case "chat": {
                    switch (this.service) {
                        case "chat": { // 聊天
                            String receiverId = this.paramsMap.get("receiverId");
                            String message = this.paramsMap.get("message");
                            Result result = Result.success();
                            result.setReceiverId(receiverId);
                            result.setData(message);
                            return result;
                        }
                    }
                }
            }

            return Result.error("404", "请求无法解析");
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Result.error("500", e.getMessage());
        }

    }
}
