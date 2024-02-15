package com.yen.controller;

import com.yen.pojo.dto.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基本控制器
 * 本控制器以下的所有接口均开放
 *
 * @author Yhx
 * @date 2024/2/14 15:02
 */
@RestController()
@RequestMapping("/base")
public class BaseController {


    /**
     * 你好世界
     *
     * @return {@link Result}<{@link String}>
     */
    @GetMapping("/hello")
    Result<String> hello() {
        return Result.success("成功调用BaseController下的无阻碍接口");
    }


}
