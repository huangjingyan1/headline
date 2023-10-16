package com.szu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szu.pojo.User;
import com.szu.service.UserService;
import com.szu.mapper.UserMapper;
import com.szu.utils.JwtHelper;
import com.szu.utils.MD5Util;
import com.szu.utils.Result;
import com.szu.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author Dell
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2023-10-15 13:33:47
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;

    // 登录业务
    // 根据账号，查询用户对象
    // 若用户对象为null，查询失败，账号错误，501
    // 对比密码，若不一致，密码错误，503
    // 根据用户id生成一个token，装载到result中返回
    @Override
    public Result login(User user) {
        // 根据账号查询数据
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User loginUser = userMapper.selectOne(queryWrapper);

        if (loginUser == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        // 对比密码
        if (!StringUtils.isEmpty(user.getUserPwd())
                && MD5Util.encrypt(user.getUserPwd()).equals(loginUser.getUserPwd())){
            // 登录成功
            // 根据用户id生成token，并将token封装到result返回
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid())); // 注意要使用数据库中查到的user，登录的user只有账号密码
            Map data = new HashMap();
            data.put("token", token);
            return Result.ok(data);
        }

        // 密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }


    // 根据token获取用户数据
    // 先校验token是否在有效期
    // 根据token解析userId
    // 根据userId查询数据
    // 去掉密码，将用户封装到result返回
    @Override
    public Result getUserInfo(String token) {
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {   // 若过期，当做未登录看待
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        int userId = jwtHelper.getUserId(token).intValue();
        User user = userMapper.selectById(userId);
        user.setUserPwd("");
        Map data = new HashMap();
        data.put("loginUser", user);
        return Result.ok(data);
    }


    // 检查账号是否可用
    // 根据账号进行count查询，count==0 可用，count>0 不可用
    @Override
    public Result checkUserName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        Long count = userMapper.selectCount(queryWrapper);
        if (count == 0) {
            return Result.ok(null);
        }
        return Result.build(null, ResultCodeEnum.USERNAME_USED);
    }

    // 用户注册功能
    // 先检查账号是否存在，与上面一致，直接复用即可
    // 密码加密处理
    // 保存账号数据
    @Override
    public Result regist(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        userMapper.insert(user);
        return Result.ok(null);
    }
}




