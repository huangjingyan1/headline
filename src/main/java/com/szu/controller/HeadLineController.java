package com.szu.controller;

import com.szu.pojo.Headline;
import com.szu.service.HeadlineService;
import com.szu.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadLineController {
    @Autowired
    private HeadlineService headlineService;

    // 登录以后才能访问，需要检查token，由于headline的每个操作都要检查token，故将检查操作写在拦截器中
    @PostMapping("publish")
    public Result publish(@RequestBody Headline headline,
                          @RequestHeader String token){
        Result result = headlineService.publish(headline, token);
        return result;
    }

    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(Integer hid){
        Headline headline = headlineService.getById(hid);   // 直接调用service层的方法，不用再到mapper层中去
        Map data = new HashMap();
        data.put("headline", headline);
        return Result.ok(data);
    }

    @PostMapping("update")
    public Result update(@RequestBody Headline headline){
        Result result = headlineService.updateDate(headline);
        return result;
    }

    @PostMapping("removeByHid")
    public Result RemoveByHid(Integer hid){
        headlineService.removeById(hid);
        return Result.ok(null);
    }
}
