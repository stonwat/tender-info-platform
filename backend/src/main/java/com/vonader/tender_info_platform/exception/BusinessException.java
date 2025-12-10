package com.vonader.tender_info_platform.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    // 错误码
    private int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}