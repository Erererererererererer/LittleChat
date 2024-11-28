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

    // 登录
    @GetMapping("/login")
    public Result login(@RequestParam("id") String userId, @RequestParam("password") String password) {
        // 登录验证
        if (userService.findById(Integer.valueOf(userId)) == null) {
            return Result.error("400", "用户不存在");
        }
        if (userService.checkLogin(Integer.valueOf(userId), password)) {
            // 登录成功，修改为在线
            userService.updateOnline(Integer.valueOf(userId), true);
            return Result.success();
        } else {
            // 登录失败
            return Result.error("400", "密码不正确");
        }
    }

    // 注册
    @GetMapping("/register")
    public Result register(@RequestParam("username") String username, @RequestParam("password") String password,
                           @RequestParam(value = "email", required = false) String email,
                           @RequestParam("phone") String phone,
                           @RequestParam("gender") String gender) {
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

        User user = new User(null, username, password, null, email, phone, Integer.valueOf(gender), 0);
        userService.add(user);
        // 根据username查询该用户的ID并返回
        String userID = userService.findByUsername(username).getId().toString();
        return Result.success(userID);
    }

    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    @DeleteMapping("/deleteById")
    public Result delete(@RequestParam Integer id) {
        userService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }

    @GetMapping("/findAll")
    public Result findAll() {
        List<User> userList = userService.findAll();
        return Result.success(userList);
    }

    @GetMapping("/findById")
    public Result findById(@RequestParam Integer id) {
        User user = userService.findById(id);
        return Result.success(user);
    }

}
