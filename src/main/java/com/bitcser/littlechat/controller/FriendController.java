package com.bitcser.littlechat.controller;

import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.entity.Friend;
import com.bitcser.littlechat.service.FriendService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Resource
    private FriendService friendService;

    @PostMapping("/add")
    public Result add(@RequestBody Friend friend) {
        friendService.add(friend);
        return Result.success();
    }

    @DeleteMapping("/deleteById")
    public Result delete(@RequestBody Friend friend) {
        friendService.deleteById(friend.getUserId(), friend.getFriendId());
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Friend friend) {
        friendService.update(friend);
        return Result.success();
    }

    @GetMapping("/findAll")
    public Result findAll(@RequestParam("userId") String userId) {
        List<Friend> friendList = friendService.findAll(Integer.valueOf(userId));
        List<Map<String, Object>> friendInfoList = new ArrayList<>();
        for (Friend friend : friendList) {
            Map<String, Object> friendInfo = new HashMap<>();
            friendInfo.put("status", friend.getStatus());
            friendInfo.put("id", friend.getUser().getId());
            friendInfo.put("username", friend.getUser().getUsername());
            friendInfo.put("avatar", friend.getUser().getAvatar());
            friendInfo.put("email", friend.getUser().getEmail());
            friendInfo.put("phone", friend.getUser().getPhone());
            friendInfo.put("gender", friend.getUser().getGender());
            friendInfo.put("online", friend.getUser().getOnline());
            friendInfoList.add(friendInfo);
        }

        // 排序（在线为第一优先，用户名为第二优先）
        Collections.sort(friendInfoList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String one = String.valueOf(o1.get("username").toString());
                String two = String.valueOf(o2.get("username").toString());
                return one.compareTo(two);
            }
        });
//        Collections.sort(friendInfoList, new Comparator<Map<String, Object>>() {
//            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//                Integer one = Integer.valueOf(o1.get("online").toString());
//                Integer two = Integer.valueOf(o2.get("online").toString());
//                return -one.compareTo(two);
//            }
//        });
        return Result.success(friendInfoList);
    }
}
