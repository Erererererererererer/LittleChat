package com.bitcser.littlechat.controller;

import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.entity.ChatRecord;
import com.bitcser.littlechat.service.ChatRecordSevice;
import jakarta.annotation.Resource;
import jakarta.websocket.OnClose;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chatrecord")
public class ChatRecordController {
    @Resource
    private ChatRecordSevice chatrecordService;

    // 获取某用户的所有会话列表
    @GetMapping("/findAll")
    public Result findAll(@RequestParam("receiverId") Integer receiverId) {
        // 这里参数里的"receiverId"应改名为"userId"
        // 作为接收者
        List<ChatRecord> chatRecordListAsReceiver = chatrecordService.findAllByReceiverId(receiverId);

        List<Map<String, Object>> chatRecordInfoList = new ArrayList<>();
        for (ChatRecord chatRecord : chatRecordListAsReceiver) {
            Map<String, Object> chatRecordInfo = new HashMap<>();
            chatRecordInfo.put("senderId", chatRecord.getSenderId());  // 这里"senderId"应改名为"friendId"
            chatRecordInfo.put("lastMessage", chatRecord.getLastMessage());
            String updateAt = chatRecord.getUpdatedAt().toString().split("\\.")[0];
            chatRecordInfo.put("updateAt", updateAt);
            chatRecordInfo.put("unreadCount", chatRecord.getUnreadCount());
            chatRecordInfo.put("username", chatRecord.getFriendId().getUsername());
            chatRecordInfo.put("avatar", chatRecord.getFriendId().getAvatar());
            chatRecordInfoList.add(chatRecordInfo);
        }

        // 作为发送者
        List<ChatRecord> chatRecordListAsSender = chatrecordService.findAllBySenderId(receiverId);

        for (ChatRecord chatRecord : chatRecordListAsSender) {
            // friendId若重复，留下updateAt最新的
            List<Map<String, Object>> filteredList = chatRecordInfoList.stream().filter(map ->
                    chatRecord.getReceiverId().equals(map.get("senderId"))).toList();
            if (!filteredList.isEmpty()) {
                String updateAt_new = chatRecord.getUpdatedAt().toString().split("\\.")[0];
                String updateAt_exist = (String) filteredList.get(0).get("updateAt");
                if (updateAt_exist.compareTo(updateAt_new) < 0) {
                    // 当前的时间更新，修改之前的；否则直接舍弃当前项
                    chatRecordInfoList.stream().filter(map -> chatRecord.getReceiverId().equals(map.get("senderId")))
                            .forEach(map -> {
                                map.put("lastMessage", chatRecord.getLastMessage());
                                map.put("updateAt", updateAt_new);
                                map.put("unreadCount", 0);  // “我”作为发送者，“我”的未读始终是0
                            });
                }
                continue;
            }

            // friendId未重复
            Map<String, Object> chatRecordInfo = new HashMap<>();
            chatRecordInfo.put("senderId", chatRecord.getReceiverId());  // 这里"senderId"应改名为"friendId"
            chatRecordInfo.put("lastMessage", chatRecord.getLastMessage());
            String updateAt = chatRecord.getUpdatedAt().toString().split("\\.")[0];
            chatRecordInfo.put("updateAt", updateAt);
            chatRecordInfo.put("unreadCount", 0);  // “我”作为发送者，“我”的未读始终是0
            chatRecordInfo.put("username", chatRecord.getFriendId().getUsername());
            chatRecordInfo.put("avatar", chatRecord.getFriendId().getAvatar());
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