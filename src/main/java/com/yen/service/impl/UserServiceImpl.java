package com.yen.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yen.config.security.SecurityUser;
import com.yen.mapper.UserMapper;
import com.yen.pojo.dto.LoginRequestDTO;
import com.yen.pojo.dto.LoginResponseDTO;
import com.yen.pojo.dto.UserRegisterDTO;
import com.yen.pojo.po.User;
import com.yen.service.UserService;
import com.yen.util.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.yen.config.redis.RedisConstant.USER_CACHE_KEY;
import static com.yen.config.redis.RedisConstant.USER_CACHE_TTL_MINUTES;
import static com.yen.config.security.JWTConfig.EXPIRE_TIME;
import static com.yen.config.security.JWTConfig.KEY;

/**
 * 用户服务Service
 * 注意此处额外继承了SpringSecurity的UserDetailsService接口，是为了重写框架的登录查询逻辑
 *
 * @author Yossi
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-02-14 16:07:58
 * @date 2024/02/14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 密码编码器(已配置)
     */
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 注入校验管理器
     */
    @Resource
    private AuthenticationManager authenticationManager;


    /**
     * 用户登录
     * 1.支持多用户同时登录：使用集合存储redis
     *
     * @param loginRequestDTO 登录请求dto
     * @return {@link LoginResponseDTO}
     */
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws JsonProcessingException {
        // 1.使用框架自带方法(已重写)进行验证登录
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        // 2.开始验证：自动调用 MyUserDetailsService.loadUserByUsername() 方法
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (ObjectUtil.isNull(authenticate)) {
            throw new RuntimeException("登录失败,请检查账号或密码");
        }

        // 3.登录成功
        // 3.1强转为自定义用户类
        SecurityUser securityUser = (SecurityUser) authenticate.getPrincipal();
        String uuid = securityUser.getUser().getUuid();

        // 3.2生成Jwt
        // jwt到期时间（单位毫秒）
        long expireTime = System.currentTimeMillis() + 1000 * EXPIRE_TIME;
        String token = JWT.create()
                // jwt中存储用户的唯一id，此处特征用户表的UUID
                .setPayload("sub", uuid)
                // 设置jwt的过期时间("exp")的Payload值，这个过期时间必须要大于签发时间。校验时如果系统时间大于该值，则token无效
                .setExpiresAt(new Date(expireTime))
                // 设置签名/密钥
                .setKey(KEY.getBytes())
                .sign();

        // 4.将用户信息存入redis,存入的是框架所需的用户类(有相关字段)
        String redisKey = USER_CACHE_KEY + uuid;
        redisUtil.set(redisKey, securityUser, USER_CACHE_TTL_MINUTES, TimeUnit.MINUTES);

        // 5.返回token给用户
        return new LoginResponseDTO(token);
    }

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册dto
     * @return {@link Boolean}
     */
    @Override
    public Boolean userRegister(UserRegisterDTO userRegisterDTO) {
        // 1.查询用户名是否重复
        boolean exists = userMapper.exists(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, userRegisterDTO.getUsername()));
        if (exists) {
            throw new RuntimeException("该用户名重复");
        }
        // 2.给数据库中添加注册信息
        // 2.1加密密码
        String password = passwordEncoder.encode(userRegisterDTO.getPassword());

        // 3.添加新用户信息至数据库
        boolean save = save(User.builder()
                .username(userRegisterDTO.getUsername())
                // 生成用户唯一Id
                .uuid(IdUtil.fastSimpleUUID())
                .password(password).build());

        if (save) {
            return true;
        } else {
            throw new RuntimeException("注册失败");
        }

    }


}




