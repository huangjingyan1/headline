package com.szu.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szu.utils.JwtHelper;
import com.szu.utils.Result;
import com.szu.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// 登录保护拦截器，检查请求头是否包含有效token
@Component
public class LoginProtectedInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        boolean expiration = jwtHelper.isExpiration(token);
        if (!expiration) {
            return true;
        }
        Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);

        // 自己组装json返回
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().print(json);
        return false;
    }
}
