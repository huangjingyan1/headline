package com.szu.service;

import com.szu.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szu.utils.Result;

/**
* @author Dell
* @description 针对表【news_type】的数据库操作Service
* @createDate 2023-10-15 13:33:47
*/
public interface TypeService extends IService<Type> {

    Result findAllTypes();
}
