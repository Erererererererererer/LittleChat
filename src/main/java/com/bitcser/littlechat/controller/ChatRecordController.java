package com.bitcser.littlechat.controller;

import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.entity.ChatRecord;
import com.bitcser.littlechat.service.ChatRecordSevice;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/chatrecord")
public class ChatRecordController {
    @Resource
    private ChatRecordSevice chatrecordService;

    @GetMapping("/findAll")
    public Result findAll(@RequestParam("receiverId") Integer receiverId) {
        List<ChatRecord> chatRecordList = chatrecordService.findAll(receiverId);

        List<Map<String, Object>> chatRecordInfoList = new ArrayList<>();
        for (ChatRecord chatRecord : chatRecordList) {
            Map<String, Object> chatRecordInfo = new HashMap<>();
            chatRecordInfo.put("senderId", chatRecord.getSenderId());
            chatRecordInfo.put("lastMessage", chatRecord.getLastMessage());
            String updateAt = chatRecord.getUpdatedAt().toString().split("\\.")[0];
            chatRecordInfo.put("updateAt", updateAt);
            chatRecordInfo.put("unreadCount", chatRecord.getUnreadCount());
            chatRecordInfo.put("username", chatRecord.getSender().getUsername());
            chatRecordInfo.put("avatar", chatRecord.getSender().getAvatar());
            chatRecordInfoList.add(chatRecordInfo);
        }

        // 排序（时间倒序）
        Collections.sort(chatRecordInfoList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String one = String.valueOf(o1.get("updateAt").toString());
                String two = String.valueOf(o2.get("updateAt").toString());
                return -one.compareTo(two);
            }
        });

        return Result.success(chatRecordInfoList);
    }

    @PutMapping("/update")
    public Result update(@RequestBody ChatRecord chatrecord) {
        chatrecordService.update(chatrecord.getSenderId(), chatrecord.getReceiverId(), chatrecord.getLastMessage(), 1);
        return Result.success();
    }

}