package com.yen.config.security;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yen.pojo.po.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 安全用户类
 * springSecurity框架所需的用户类，里面需要有框架所需的字段(账号、密码、权限、状态等)、方法。
 * 注意：
 * 1.重写的几个方法中有些方法需要返回true
 * 2.authorities字段不允许序列化会报错，所以要配置忽略序列化。（特别是在存入redis的场景中）
 * 3.@JsonIgnoreProperties是为了jackson正常序列化
 *
 * @author Yhx
 * @date 2024/2/14 16:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"authorities", "enabled", "password", "username", "accountNonLocked", "credentialsNonExpired", "accountNonExpired"})
public class SecurityUser implements UserDetails {

    /**
     * 用户表的实体类
     */
    private User user;

    /**
     * 权限字符串集合(连表查询得到)
     */
    private List<String> permissions;

    /**
     * 框架权限信息集合（里面权限都是字符串类型，要注意的是‘角色’字符串需要添加前缀”ROLE_“）
     */
    private List<SimpleGrantedAuthority> authorities;


    // 重写获取用户名和密码的方法，框架会自动调用以下方法
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    /**
     * 重写获取权限方法
     * 将自己的权限字段写成框架所需的对象
     *
     * @return {@link List}<{@link SimpleGrantedAuthority}>
     */
    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        // 1.检查是否已封装该对象优化速度
        if (authorities != null) {
            return authorities;
        }
        // 2.封装框架所需对象
        // 防止下面空指针
        if (ObjectUtil.isNull(permissions)) {
            return null;
        }
        authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return authorities;
    }

    // 重写的其他方法，暂时无用
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
