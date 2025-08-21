package com.vonader.tender_info_platform.common;

import lombok.Data;

@Data
public class Result<T> {
    // 状态码（200 成功，400 参数错误，500 服务器错误等）
    private int code;
    // 提示信息（成功或错误描述）
    private String msg;
    // 响应数据（成功时返回业务数据）
    private T data;

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    // 失败响应（带错误码和错误信息）
    public static <T> Result<T> fail(int code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        result.data = null;
        return result;
    }
}