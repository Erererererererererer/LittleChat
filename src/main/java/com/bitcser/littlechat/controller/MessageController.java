package com.bitcser.littlechat.controller;

import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.entity.Friend;
import com.bitcser.littlechat.entity.Message;
import com.bitcser.littlechat.service.MessageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/add")
    public Result add(@RequestBody Message message) {
        messageService.add(message);
        return Result.success();
    }

    @DeleteMapping("/deleteById")
    public Result delete(@RequestBody Message message) {
        messageService.deleteById(message.getSenderId(), message.getReceiverId());
        return Result.success();
    }

    @PatchMapping("/update")
    public Result update(@RequestBody Message message) {
        messageService.update(message);
        return Result.success();
    }

    @GetMapping("/findAll")
    public Result findAll(@RequestParam Integer userId, @RequestParam Integer friendId) {
        List<Message> messageList = messageService.findAll(userId, friendId);

        List<Map<String, Object>> messageInfoList = new ArrayList<>();
        for (Message message : messageList) {
            Map<String, Object> messageInfo = new HashMap<>();
            messageInfo.put("senderId", message.getSenderId());
            messageInfo.put("receiverId", message.getReceiverId());
            messageInfo.put("content", message.getContent());
            String time = message.getTime().toString().split("\\.")[0];
            messageInfo.put("time", time);
            messageInfoList.add(messageInfo);
        }

        return Result.success(messageInfoList);
    }
}
