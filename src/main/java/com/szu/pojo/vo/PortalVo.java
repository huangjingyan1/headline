package com.szu.pojo.vo;

import lombok.Data;

// 用于接收前端传过来的参数的实体类
@Data
public class PortalVo {
    private String keyWords;
    private int type = 0;
    private int pageNum = 1;
    private int pageSize = 10;
}
