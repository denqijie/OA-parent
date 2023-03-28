package com.dqj.common.config.exception;

import com.dqj.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    //  全局异常处理的方法
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行全局异常处理");
    }

    //  特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("特定异常处理");
    }

    //  自定义异常处理
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result error(MyException e){
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMsg());
    }
}
