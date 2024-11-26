package com.bitcser.littlechat.exception;
import com.bitcser.littlechat.common.Result;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常处理
@ControllerAdvice("com.bitcser.littlechat.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e) {
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMsg());
    }
}
