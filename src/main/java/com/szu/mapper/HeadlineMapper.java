package com.szu.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.szu.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szu.pojo.vo.PortalVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author Dell
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2023-10-15 13:33:47
* @Entity com.szu.pojo.Headline
*/
public interface HeadlineMapper extends BaseMapper<Headline> {

    IPage<Map> selectMyPage(IPage page, @Param("portalVo") PortalVo portalVo);

    Map queryDetailMap(Integer hid);
}




