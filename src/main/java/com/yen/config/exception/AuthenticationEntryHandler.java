package com.yen.config.exception;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * 处理认证失败后的异常
 * 注意这个是过滤器中的
 * 如未登录就调用保护的接口
 * 注意：
 * 此处实现了把 Handler 委托给该 Resolver（解析器）
 * 这样可以使用 Exception Handler 方法通过 Controller Advice 来处理此 Security 异常。
 *
 * @author Yhx
 * @date 2024/2/15 12:32
 */
@Component
public class AuthenticationEntryHandler implements AuthenticationEntryPoint {

    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    /**
     * 重写方法把 Handler 委托给该 Resolver
     * 现在可以使用全局异常处理器进行处理
     *
     * @param request       要求
     * @param response      回答
     * @param authException 身份验证异常
     * @throws IOException      IOException
     * @throws ServletException servlet异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        resolver.resolveException(request,response,null,authException);
    }
}
