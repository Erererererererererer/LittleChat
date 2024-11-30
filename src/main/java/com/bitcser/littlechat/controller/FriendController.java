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

    // 请求添加好友（通过用户ID）
    @GetMapping("/requestById")
    public Result request(@RequestParam("userId") Integer userId, @RequestParam("friendId") Integer friendId) {
        friendService.add(userId, friendId, 0);
        return Result.success();
    }

    // 请求添加好友（通过用户phone）
    @GetMapping("/request")
    public Result request(@RequestParam("userId") Integer userId, @RequestParam("phone") String friendPhone) {
        friendService.addByPhone(userId, friendPhone, 0);
        return Result.success();
    }

    @GetMapping("/deleteById")
    public Result delete(@RequestParam("userId") Integer userId, @RequestParam("friendId") Integer friendId) {
        friendService.deleteById(userId, friendId);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Friend friend) {
        friendService.update(friend);
        return Result.success();
    }

    // 回应好友申请
    @GetMapping("/answer")
    public Result answer(@RequestParam("userId") Integer userId, @RequestParam("friendId") Integer friendId,
                         @RequestParam("answer") String answer) {
        if (answer.equals("0")) {
            // 不同意，删除请求
            friendService.deleteById(friendId, userId);
            return Result.success();
        } else {
            // 同意，添加好友
            friendService.updateStatus(friendId, userId);
            friendService.add(userId, friendId, 1);
            return Result.success();
        }
    }

    // 显示所有好友
    @GetMapping("/findAll")
    public Result findAll(@RequestParam("userId") String userId) {
        List<Friend> friendList = friendService.findAll(Integer.valueOf(userId));

        List<Map<String, Object>> friendInfoList = new ArrayList<>();
        for (Friend friend : friendList) {
            Map<String, Object> friendInfo = new HashMap<>();
            if (friend.getStatus() == 0) { // 排除未通过的好友
                continue;
            }
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

    // 显示好友申请列表
    @GetMapping("/findRequest")
    public Result findRequest(@RequestParam("userId") String userId) {
        List<Friend> friendList = friendService.findAllByFriendId(Integer.valueOf(userId));

        List<Map<String, Object>> friendInfoList = new ArrayList<>();
        for (Friend friend : friendList) {
            Map<String, Object> friendInfo = new HashMap<>();
            if (friend.getStatus() == 1) { // 排除已通过的好友
                continue;
            }
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

        // 排序（以用户名升序）
        Collections.sort(friendInfoList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String one = String.valueOf(o1.get("username").toString());
                String two = String.valueOf(o2.get("username").toString());
                return one.compareTo(two);
            }
        });
        return Result.success(friendInfoList);
    }
}
