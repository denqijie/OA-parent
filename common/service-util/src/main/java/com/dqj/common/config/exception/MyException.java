package com.dqj.common.config.exception;

import com.dqj.common.result.ResultCodeEnum;
import lombok.Data;

/**
 *  自定义全局异常类
 */
@Data
public class MyException extends RuntimeException {
    //  状态码
    private Integer code;
    //  返回信息
    private String msg;

    public MyException(Integer code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     *  接受枚举类对象
     */
    public MyException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "MyException{" +
                "code=" + code +
                ", msg='" + this.getMessage() +
                '}';
    }
}
