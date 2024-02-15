package com.yen.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yen.config.security.SecurityUser;
import com.yen.pojo.dto.LoginRequestDTO;
import com.yen.pojo.dto.LoginResponseDTO;
import com.yen.pojo.dto.Result;
import com.yen.pojo.dto.UserRegisterDTO;
import com.yen.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
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
     * 获取登录用户信息
     *
     * @param user 使用者
     * @return {@link Result}<{@link SecurityUser}>
     */
    @GetMapping("/info")
    Result<SecurityUser> getUserInfo(@AuthenticationPrincipal SecurityUser user) {

        return Result.success(user);
    }
}
