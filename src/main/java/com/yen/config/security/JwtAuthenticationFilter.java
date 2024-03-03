package com.yen.config.security;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.yen.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static com.yen.config.redis.RedisConstant.USER_CACHE_KEY;
import static com.yen.config.security.JWTConfig.KEY;

/**
 * 自定义Jwt验证过滤器
 * OncePerRequestFilter：该过滤器只会经过一次，spring提供的过滤器，用法和以前类似
 * 默认的过滤器接口可能因为servlet版本问题被多次调用：存在问题
 *
 * @author Yhx
 * @date 2024/2/14 21:28
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 通过该类的resolveException方法抛出自定义异常交给全局异常处理器处理
     */
    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    /**
     * 自定义过滤器方法
     * 访问接口时校验请求中的token
     * 此处实现：
     * 校验JWT
     * 从Redis中获取最新用户信息
     * 注意：
     * 此处进行了约定：从请求头中的authorization获取token（如果需要从cookie中获取token需要修改方法）
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 过滤链
     * @throws ServletException servlet异常
     * @throws IOException      IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1.从请求头中的Authorization获取token
        String token = request.getHeader("Authorization");
        if (StrUtil.isEmpty(token)) {
            // 没有token直接放行,后续过滤器中该请求会被拦截
            filterChain.doFilter(request, response);
            return;
        }
        // 2.校验token
        // 2.1 验证算法(默认使用hs256算法),会校验密钥/签名,验证失败会抛出异常
        // 2.2 验证时间(由于只定义了失效时间("exp")，因此只检查失效时间)
        try {
            JWTValidator.of(token)
                    .validateAlgorithm(JWTSignerUtil.hs256(KEY.getBytes()))
                    .validateDate(DateUtil.date());
            // 校验通过
        } catch (Exception e) {
            // JWT校验失败
            resolver.resolveException(request, response, null,e);
            return;
        }

        // 3.解析token获取用户UUID
        JWT jwt = JWT.of(token);
        // 默认在sub里存入用户UUID
        String uuid = (String) jwt.getPayload("sub");

        // 4.从redis中获取用户信息
        String redisKey = USER_CACHE_KEY + uuid;
        SecurityUser securityUser = redisUtil.get(redisKey, SecurityUser.class);
        if (securityUser == null) {
            // token通过校验但无法从Redis中获取用户信息，说明redis缓存已过期。这种情况只会出现在：1.Redis被清空; 2.token过期时间长于缓存时间
            // 那么需要用户进行重新登录
            filterChain.doFilter(request, response);
            return;
        }

        // 5.将用户信息（保护权限信息）存入SecurityContextHolder，完成验证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);

    }
}
