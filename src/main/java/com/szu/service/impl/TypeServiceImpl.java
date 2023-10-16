package com.szu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szu.pojo.Type;
import com.szu.service.TypeService;
import com.szu.mapper.TypeMapper;
import com.szu.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Dell
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2023-10-15 13:33:47
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

    @Autowired
    private TypeMapper typeMapper;

    // 查询所有类别数据
    @Override
    public Result findAllTypes() {
        List<Type> types = typeMapper.selectList(null);
        return Result.ok(types);
    }
}




