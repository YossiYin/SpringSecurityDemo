package com.yen.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Yhx
 * @date 2024/2/14 16:21
 */
@Data
@AllArgsConstructor
public class LoginResponseDTO implements Serializable {
    private String token;
    private static final long serialVersionUID = 1L;

}
