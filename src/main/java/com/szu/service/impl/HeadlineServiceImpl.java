package com.szu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szu.pojo.Headline;
import com.szu.pojo.vo.PortalVo;
import com.szu.service.HeadlineService;
import com.szu.mapper.HeadlineMapper;
import com.szu.utils.JwtHelper;
import com.szu.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Dell
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2023-10-15 13:33:47
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private HeadlineMapper headlineMapper;
    // 首页数据查询
    // 进行分页数据查询再将结果拼接到result中即可
    // 注意1，查询需要自定义mapper方法并携带分页
    // 注意2，返回的结果为List<Map>，因为没有对应的实体类可以接值
    @Override
    public Result findNewsPage(PortalVo portalVo) {
        IPage<Map> page = new Page<>(portalVo.getPageNum(), portalVo.getPageSize());
        headlineMapper.selectMyPage(page, portalVo);
        List<Map> records = page.getRecords();
        Map data = new HashMap();
        data.put("pageData", records);
        data.put("pageNum", page.getCurrent());
        data.put("pageSize", page.getSize());
        data.put("totalPage", page.getPages());
        data.put("totalSize", page.getTotal());

        Map pageInfo = new HashMap();
        pageInfo.put("pageInfo", data);
        return Result.ok(pageInfo);
    }

    // 根据id查询详情
    // 1.查询对应的数据即可（多表，头条和用户表，方法需要自定义，返回map即可）
    // 2.先修改阅读量+1
    @Override
    public Result showHeadlineDetail(Integer hid) {
        Map data = headlineMapper.queryDetailMap(hid);
        Map headlineMap = new HashMap();
        headlineMap.put("headline", data);

        // 修改阅读量
        Headline headline = new Headline();
        headline.setHid((Integer) data.get("hid"));
        headline.setVersion((Integer) data.get("version"));     // 乐观锁
        headline.setPageViews((Integer) data.get("pageViews")+1);
        headlineMapper.updateById(headline);

        return Result.ok(headlineMap);
    }

    // 发布头条
    // 补全数据再插入
    @Override
    public Result publish(Headline headline, String token) {
        // 根据token查询用户id
        int userId = jwtHelper.getUserId(token).intValue();
        //数据装配
        headline.setPublisher(userId);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());
        headlineMapper.insert(headline);
        return Result.ok(null);
    }


    // 修改头条
    // hid查询数据的最新version
    // 将修改时间设为当前时间
    @Override
    public Result updateDate(Headline headline) {
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();
        headline.setVersion(version);
        headline.setUpdateTime(new Date());

        headlineMapper.updateById(headline);
        return Result.ok(null);
    }
}




