package com.vonader.tender_info_platform.exception;

import com.vonader.tender_info_platform.common.ErrorCode;
import com.vonader.tender_info_platform.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 负责捕获项目中抛出的各种异常，并进行统一处理，返回统一的 Result 格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 专门捕获并处理 BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        // 直接从自定义异常中获取错误码和错误信息，并封装到 Result 中
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 可选：捕获其他未预料到的异常（如系统异常、数据库异常等）
     * 避免将详细的错误堆栈信息直接返回给前端，暴露系统细节
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleOtherException(Exception e) {
        // 记录详细的错误日志到服务器，便于排查问题
        e.printStackTrace(); // 在实际项目中，应使用日志框架如 SLF4J 来记录
        // 给前端返回一个通用的、友好的错误信息
        return Result.fail(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMessage());
    }
}