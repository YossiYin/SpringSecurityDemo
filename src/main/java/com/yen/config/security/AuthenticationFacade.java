package com.yen.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 身份信息组件
 *
 * 优点：
 * 1.对比注解@AuthenticationPrincipal可以在任何地方使用该类，形参列表也比较干净（不会被apifox进行无效扫描）
 * 2.使用了spring依赖注入的特性，优雅
 * 外观设计模式
 * 为了充分利用 Spring 依赖注入的功能，
 * 并能够在任何地方获取认证信息（不仅仅是在 @Controller Bean 中），
 * 需要将静态访问封装在一个简单的 Facade（外观设计模式）后面：
 *
 * @author Yhx
 * @date 2024/3/3 23:48
 */
@Component
public class AuthenticationFacade {
    /**
     * 获取身份验证信息
     *
     * @return {@link Authentication}
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
