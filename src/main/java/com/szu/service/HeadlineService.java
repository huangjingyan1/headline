package com.szu.service;

import com.szu.pojo.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.szu.pojo.vo.PortalVo;
import com.szu.utils.Result;

/**
* @author Dell
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2023-10-15 13:33:47
*/
public interface HeadlineService extends IService<Headline> {

    Result findNewsPage(PortalVo portalVo);

    Result showHeadlineDetail(Integer hid);

    Result publish(Headline headline, String token);

    Result updateDate(Headline headline);
}
