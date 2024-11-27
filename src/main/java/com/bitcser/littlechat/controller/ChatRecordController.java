package com.bitcser.littlechat.controller;

import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.entity.ChatRecord;
import com.bitcser.littlechat.service.ChatRecordSevice;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatrecord")
public class ChatRecordController {
    @Resource
    private ChatRecordSevice chatrecordService;

    @GetMapping("/findAll")
    public Result findAll(@RequestParam Integer senderId) {
        List<ChatRecord> messageList = chatrecordService.findAll(senderId);
        return Result.success(messageList);
    }

    @PutMapping("/update")
    public Result update(@RequestBody ChatRecord chatrecord) {
        chatrecordService.update(chatrecord.getSenderId(), chatrecord.getReceiverId(), chatrecord.getLastMessage());
        return Result.success();
    }

}