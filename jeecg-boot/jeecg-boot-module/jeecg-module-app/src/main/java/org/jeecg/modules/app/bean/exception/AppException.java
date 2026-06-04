package org.jeecg.modules.app.bean.exception;

import lombok.Getter;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;

/**
 * 应用自定义异常类
 * 使用枚举定义的错误码和消息
 */
@Getter
public class AppException extends RuntimeException {

    private int code;

    private String msg;

    /**
     * 使用构造异常
     *
     * @param resultCode 错误码枚举
     */
    public AppException(ExceptionEnum resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 使用和自定义消息构造异常
     *
     * @param resultCode 错误码枚举
     * @param customMsg 自定义消息
     */
    public AppException(ExceptionEnum resultCode, String customMsg) {
        super(customMsg);
        this.code = resultCode.getCode();
        this.msg = customMsg;
    }

    /**
     * 使用和异常原因构造异常
     *
     * @param resultCode 错误码枚举
     * @param cause 异常原因
     */
    public AppException(ExceptionEnum resultCode, Throwable cause) {
        super(resultCode.getMsg(), cause);
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 使用自定义消息和异常原因构造异常
     *
     * @param resultCode 错误码枚举
     * @param customMsg 自定义消息
     * @param cause 异常原因
     */
    public AppException(ExceptionEnum resultCode, String customMsg, Throwable cause) {
        super(customMsg, cause);
        this.code = resultCode.getCode();
        this.msg = customMsg;
    }

}