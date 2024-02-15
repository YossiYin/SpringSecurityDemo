package com.yen.config.exception;

import com.yen.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.yen.enums.ResultCode.FORBIDDEN;
import static com.yen.enums.ResultCode.UNAUTHORIZED;


/**
 * 全局异常处理器
 *
 * @author Yhx
 * @date 2023/04/18
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 身份验证异常处理程序
     *
     * @param e e
     * @return {@link Result}<{@link String}>
     */
    @ExceptionHandler({AuthenticationException.class})
    public Result<String> authenticationExceptionHandler(AuthenticationException e) {
        log.info("用户认证失败");
        return Result.failed(UNAUTHORIZED, "用户认证失败");
    }

    /**
     * 拒绝访问异常处理程序 无权限
     *
     * @param e e
     * @return {@link Result}<{@link String}>
     */
    @ExceptionHandler({AccessDeniedException.class})
    public Result<String> accessDeniedExceptionHandler(AccessDeniedException e) {
        log.info("该用户无权限进行此操作");
        return Result.failed(FORBIDDEN, "无权限操作");
    }

    /**
     * 运行时异常处理器
     *
     * @param e e
     * @return {@link Result}<{@link String}>
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> runtimeExceptionHandler(RuntimeException e) {
        log.error("出现运行时异常RuntimeException:{}", e.getMessage());
        return Result.failed(e.getMessage(), "服务器出现异常");
    }


    /**
     * 异常处理器
     * 兜底
     *
     * @param e e
     * @return {@link Result}<{@link String}>
     */
    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception e) {
        log.error("出现Exception异常:{}", e.getMessage());
        return Result.failed(e.getMessage(), "服务器出现异常");
    }

}
