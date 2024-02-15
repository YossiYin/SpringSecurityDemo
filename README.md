# 环境/依赖介绍

+ JDK 17.0.10
+ Springboot 3.0.2
+ SpringSecurity 6.0.1
+ redis
+ mysql8
+ mybatis-plus 3.0
+ lombok



# SpringSecurity

+ 使用了RBAC权限模型，既基于角色的权限控制
+ 配置了默认使用**BCrypt算法**进行密码的加解密
+ 关闭了默认的csrf和session功能
+ 只使用JWT进行身份校验
+ 使用了redis存储用户信息（key:uuid）
+ 异常处理：将原本的AccessDeniedHandler和AuthenticationEntryPoint过滤器中的异常处理委托给HandlerExceptionResolver，实现了全局异常处理器也能进行处理
+ 使用了SpringSecurity的全新写法，无使用废弃方法。

```
默认用户账号名：user
密码：123456
```



# 初始化

库名：springsecurity_demo

user表：

```
/*
 Navicat Premium Data Transfer

 Source Server         : MySQL8.0.30
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : 127.0.0.1:3306
 Source Schema         : springsecurity_demo

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 15/02/2024 12:13:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `uuid` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '唯一Id',
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '账号名（不允许重复）',
  `nick_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `phone` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `user_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户类型（数字）',
  `sex` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别(0-女，1-男，2-其他)',
  `age` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '年龄',
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '用户描述',
  `icon` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '头像链接',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段(0-未删除，1-被删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '8c9ac9048dd74112bc37dba498c6dd6c', 'user', '', '$2a$10$sPTroodpqOQCXCt9kWS7wOPoy5FkuI879I19Xd5IDztfOFo.H.Kl6', '', 0, 0, 0, '', '', '', '2024-02-15 12:12:39', '2024-02-15 12:12:39', 0);

SET FOREIGN_KEY_CHECKS = 1;

```

