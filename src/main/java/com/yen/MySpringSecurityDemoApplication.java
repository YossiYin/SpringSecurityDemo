package com.yen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
// 配置mapper地址
@MapperScan("com.yen.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MySpringSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringSecurityDemoApplication.class, args);
    }

}
