package com.yen.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户请求登录DTO
 *
 * @author Yhx
 * @date 2024/2/14 16:15
 */
@Data
public class LoginRequestDTO implements Serializable {
    private String username;
    private String password;
    private static final long serialVersionUID = 1L;
}
