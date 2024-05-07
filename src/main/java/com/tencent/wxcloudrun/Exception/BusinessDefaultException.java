package com.tencent.wxcloudrun.Exception;

/**
 * 统一service异常, 其他自定义异常均继承
 *
 * @author dongdongxie
 * @date 2023/04/13
 */
public class BusinessDefaultException extends RuntimeException {

    private static final long serialVersionUID = -7681216209917365385L;

    public BusinessDefaultException() {
        this("系统调用异常");
    }

    public BusinessDefaultException(String message) {
        super(message);
    }

    public BusinessDefaultException(Throwable cause) {
        super(cause);
    }

    public BusinessDefaultException(String message, Throwable cause) {
        super(message, cause);
    }
}
