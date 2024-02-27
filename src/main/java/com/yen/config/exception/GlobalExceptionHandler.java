package com.yen.config.exception;

import cn.hutool.core.exceptions.ValidateException;
import com.yen.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.yen.pojo.enums.ResultCode.FORBIDDEN;
import static com.yen.pojo.enums.ResultCode.UNAUTHORIZED;


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
     * JWT校验错误
     * 1.验证算法(默认使用hs256算法),会校验密钥/签名,验证失败会抛出异常
     * 2.验证时间(由于只定义了失效时间("exp")，因此只检查失效时间)
     *
     * @param e e
     * @return {@link Result}<{@link String}>
     */
    @ExceptionHandler({ValidateException.class})
    public Result<String> validateExceptionHandler(ValidateException e) {
        log.info("token解析错误:{}",e.getMessage());
        return Result.failed("用户登录信息错误");
    }


    /**
     * 身份验证异常处理程序
     * 出现原因：请求未携带token
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
