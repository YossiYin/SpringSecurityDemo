package com.yen.config.security;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yen.mapper.UserMapper;
import com.yen.pojo.po.User;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义用户详细Service
 * 实现在数据库中查询用户及其权限
 *
 * @author Yhx
 * @date 2024/2/14 20:30
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    /**
     * 重写SpringSecurity登录验证方法
     * 从数据库中查询登录密码
     * AuthenticationManager提供了多种认证方法，在默认策略下，只需要通过一个AuthenticationProvider的认证，即可被认为是登录成功。
     * 此处重写的是用户名+密码（UsernamePasswordAuthenticationToken）方法
     *
     * @param username 用户名
     * @return {@link UserDetails} 此处返回的是我们自己定义的用户类,框架会自动调用相应的方法
     * @throws UsernameNotFoundException 找不到用户名异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.查询用户信息,根据用户名查询是否有对应用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(wrapper);

        // 2.如果没有查询到用户信息就抛出异常
        if (ObjectUtil.isNull(user)){
            throw new RuntimeException("用户名或者密码错误");
        }

        // 3.设置用户权限(当前写死,后续在数据库查即可)
        List<String> list = new ArrayList<>(Arrays.asList("system:user:add","ROLE_admin"));

        // 4.把数据封装成SecurityUser返回
        return SecurityUser.builder().user(user).permissions(list).build();
    }
}
