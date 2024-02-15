package com.yen.config.exception;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * 处理校验失败后的异常
 * 异常委托给HandlerExceptionResolver，之后可以由全局异常处理器进行处理
 *
 * @author Yhx
 * @date 2024/2/15 12:39
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;


    /**
     * 重写方法把 Handler 委托给该 Resolver
     * 现在可以使用全局异常处理器进行处理
     *
     * @param request               请求
     * @param response              响应
     * @param accessDeniedException 访问被拒绝异常
     * @throws IOException      IOException
     * @throws ServletException servlet异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        resolver.resolveException(request,response,null,accessDeniedException);
    }
}
