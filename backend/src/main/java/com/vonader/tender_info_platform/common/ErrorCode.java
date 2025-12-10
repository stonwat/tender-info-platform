package com.vonader.tender_info_platform.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200, "成功"),
    PARAMS_ERROR(400, "请求参数错误"),
    NOT_FOUND_ERROR(404, "请求数据不存在"),
    CONFLICT_ERROR(409, "数据冲突"),
    SYSTEM_ERROR(500, "系统内部异常");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}