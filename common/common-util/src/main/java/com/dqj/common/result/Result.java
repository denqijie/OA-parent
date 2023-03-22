package com.dqj.common.result;

import lombok.Data;

@Data
public class Result<T> {
    //  状态码
    private Integer code;
    //  返回信息
    private String message;
    //  封装数据
    private T data;

    //  构造私有化
    private Result(){}

    //  封装返回数据
    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum){
        Result<T> result = new Result<>();
        //  封装数据
        if(data != null){
            result.setData(data);
        }
        //  状态码
        result.setCode(resultCodeEnum.getCode());
        //  返回信息
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    //  成功
    public static <T> Result<T> ok(){
        return build(null, ResultCodeEnum.SUCCESS);
    }

    //  成功 带数据参数
    public static <T> Result<T> ok(T data){
        return build(data, ResultCodeEnum.SUCCESS);
    }

    //  失败
    public static <T> Result<T> fail(){
        return build(null, ResultCodeEnum.FAIL);
    }

    //  失败 带数据参数
    public static <T> Result<T> fail(T data){
        return build(data, ResultCodeEnum.FAIL);
    }

    public Result<T> message(String message){
        this.setMessage(message);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

}
