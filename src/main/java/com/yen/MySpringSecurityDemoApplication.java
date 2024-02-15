package com.yen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 配置mapper地址
@MapperScan("com.yen.mapper")
public class MySpringSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringSecurityDemoApplication.class, args);
    }

}
