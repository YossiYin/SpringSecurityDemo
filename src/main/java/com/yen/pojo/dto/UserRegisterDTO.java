package com.yen.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册dto
 *
 * @author Yhx
 * @date 2024/2/14 17:30
 */
@Data
public class UserRegisterDTO implements Serializable {

    private String username;
    private String password;
    private static final long serialVersionUID = 1L;
}
