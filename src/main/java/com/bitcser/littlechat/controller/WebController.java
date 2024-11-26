package com.bitcser.littlechat.controller;
import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.exception.CustomException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/hello")
    public Result hello() {
        //throw new CustomException("400", "禁止请求");
        Result result = Result.success("Hello world!");
        return result;
    }

}
