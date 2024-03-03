package com.yen.config.security;

import com.yen.config.exception.AccessDeniedHandlerImpl;
import com.yen.config.exception.AuthenticationEntryHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity框架配置类
 * 使用了5.7版本以上推荐的lambda写法
 *
 * @author yen
 * @date 2024/02/14
 */

@Configuration
@EnableWebSecurity    // 添加 security 过滤器
@EnableMethodSecurity // 开启方法级别的权限控制
public class SpringSecurityConfig {

    /**
     * 用来创建ProviderManager（AuthenticationManager的实现）
     */
    @Resource
    private AuthenticationConfiguration authenticationConfiguration;

    /**
     * 自定义的jwt身份验证过滤器
     */
    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 认证失败处理器
     */
    @Resource
    private AuthenticationEntryHandler authenticationEntryHandler;
    /**
     * 权限校验失败处理器
     */
    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;

    /**
     * Http过滤链配置
     *
     * @param http http
     * @return {@link SecurityFilterChain}
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1.关闭csrf
        http.csrf().disable()
                // 2.关闭session:不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 3.配置请求认证机制
        http.authorizeHttpRequests()
                .requestMatchers("/", "/base/**", "/error", "/user/login", "/user/register").permitAll()
                // 放行Knife4j相关请求
                .requestMatchers("/doc.html","/webjars/**","/v3/api-docs/**","/swagger-ui.html","/swagger-ui/**").permitAll()
                // 权限控制 TODO 如何通过？
                .requestMatchers("/user/is-admin").hasRole("admin")
                // 除以上地址以外都需要认证
                .anyRequest().authenticated();

        // 4.配置自定义过滤器
        // 将jwt校验过滤器配置在UsernamePasswordAuthenticationFilter前
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 5.配置异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryHandler)
                .accessDeniedHandler(accessDeniedHandler);

        return http.build();

    }

    /**
     * 配置默认密码编码器
     * 这样默认就会使用这种加密方式，数据库密码字段的记录中也不需要大括号了
     * 可以注入使用该类进行加解密了
     *
     * @return {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager实现，用于管理所有AuthenticationProvider实现的一个管理器。
     * 使用自定义密码登录时需要的一个Bean
     *
     * @return {@link AuthenticationManager}
     * @throws Exception 例外
     */
    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
