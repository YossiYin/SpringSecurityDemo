package com.yen.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yen.config.security.AuthenticationFacade;
import com.yen.config.security.SecurityUser;
import com.yen.pojo.dto.LoginRequestDTO;
import com.yen.pojo.dto.LoginResponseDTO;
import com.yen.pojo.dto.Result;
import com.yen.pojo.dto.UserRegisterDTO;
import com.yen.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author Yhx
 * @date 2024/2/14 15:36
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private AuthenticationFacade authenticationFacade;

    /**
     * 用户登录
     *
     * @param loginRequestDTO 登录请求dto
     * @param response        响应
     * @return {@link Result}<{@link LoginResponseDTO}>
     * @throws JsonProcessingException json处理异常
     */
    @PostMapping("/login")
    Result<LoginResponseDTO> userLogin(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) throws JsonProcessingException {
        LoginResponseDTO loginResponseDTO = userService.login(loginRequestDTO);

        // 约定：在响应中设置请求头的Authorization的值为token
        response.addHeader("Authorization", loginResponseDTO.getToken());


        return Result.success(loginResponseDTO);
    }

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册dto
     * @return {@link Result}<{@link String}>
     */
    @PostMapping("/register")
    Result<String> userRegister(UserRegisterDTO userRegisterDTO) {
        Boolean flag = userService.userRegister(userRegisterDTO);

        return flag ? Result.success("注册成功") : Result.failed("注册失败");
    }

    /**
     * 获取登录用户信息(使用注解)
     *
     * @param user 使用者
     * @return {@link Result}<{@link SecurityUser}>
     */
    @GetMapping("/info1")
    Result<SecurityUser> getUserInfo(@AuthenticationPrincipal SecurityUser user) {

        return Result.success(user);
    }

    /**
     * 获取登录用户信息(使用外观设计模式)
     * 类似工具类，充分使用了spring的依赖注入
     * 需要getPrincipal()强转
     *
     * 比较推荐这种方式，因为参数列表看起来不会那么难受，也不会被apifox扫描出来
     *
     * @return {@link Result}<{@link SecurityUser}>
     */
    @GetMapping("/info2")
    Result<SecurityUser> getUserInfo2() {
        SecurityUser user = (SecurityUser) authenticationFacade.getAuthentication().getPrincipal();
        return Result.success(user);
    }

    /**
     * 管理员身份校验(Config设置权限)
     *
     * @param user 使用者
     * @return {@link Result}<{@link String}>
     */
    @GetMapping("/is-admin")
//    @PreAuthorize("hasRole('admin')")
    Result<String> isAdmin(@AuthenticationPrincipal SecurityUser user) {
        return Result.success("只有role是admin的才能访问该接口");
    }

    /**
     * 用户身份校验(方法级限制权限)
     * 直接在Controller使用注解进行权限限制
     *
     * @param user 使用者
     * @return {@link Result}<{@link String}>
     */
    @GetMapping("/is-user")
    @PreAuthorize("hasRole('user')")
    Result<String> isUser(@AuthenticationPrincipal SecurityUser user) {
        return Result.success("只有role是user的才能访问该接口");
    }
}
