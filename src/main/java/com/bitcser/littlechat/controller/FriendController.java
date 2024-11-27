package com.bitcser.littlechat.controller;

import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.entity.Friend;
import com.bitcser.littlechat.service.FriendService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result findAll(@RequestParam Integer userId) {
        List<Friend> friendList = friendService.findAll(userId);
        return Result.success(friendList);
    }
}
