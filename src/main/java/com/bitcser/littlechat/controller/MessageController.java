package com.bitcser.littlechat.controller;

import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.entity.Message;
import com.bitcser.littlechat.service.MessageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result findAll(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
        List<Message> messageList = messageService.findAll(senderId, receiverId);
        return Result.success(messageList);
    }
}
