package com.szu.service;

import com.szu.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szu.utils.Result;

/**
* @author Dell
* @description 针对表【news_user】的数据库操作Service
* @createDate 2023-10-15 13:33:47
*/
public interface UserService extends IService<User> {

    Result login(User user);

    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result regist(User user);
}
