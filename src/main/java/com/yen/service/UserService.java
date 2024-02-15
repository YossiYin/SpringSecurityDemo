package com.yen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yen.pojo.dto.LoginRequestDTO;
import com.yen.pojo.dto.LoginResponseDTO;
import com.yen.pojo.dto.UserRegisterDTO;
import com.yen.pojo.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Yossi
* @description 针对表【user】的数据库操作Service
* @createDate 2024-02-14 16:07:58
*/
public interface UserService extends IService<User> {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws JsonProcessingException;

    Boolean userRegister(UserRegisterDTO userRegisterDTO);
}
