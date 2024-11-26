package com.bitcser.littlechat.controller;
import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.entity.User;
import com.bitcser.littlechat.service.UserService;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    @DeleteMapping("/deleteById")
    public Result delete(@RequestParam Integer uid) {
        userService.deleteById(uid);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }

    @GetMapping("/findAll")
    public Result findAll() {
        List<User> userList =  userService.findAll();
        return Result.success(userList);
    }

    @GetMapping("/selectById")
    public Result selectById(@RequestParam Integer uid) {
        User user =  userService.selectById(uid);
        return Result.success(user);
    }

}
