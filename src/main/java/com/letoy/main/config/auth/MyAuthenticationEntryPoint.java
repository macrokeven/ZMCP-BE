package com.letoy.main.config.auth;

import com.letoy.main.entity.system.SystemLogInfo;
import com.letoy.main.service.SystemLogInfoService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import static com.letoy.main.utils.Constants.SYSTEM_WARNING_LOG;
import static com.letoy.main.utils.Constants.TOKEN_INVALID;

/**
 * 身份校验失败处理器，如 token 错误
 */
@Component
public class MyAuthenticationEntryPoint extends JSONAuthentication implements AuthenticationEntryPoint {
    @Resource
    SystemLogInfoService systemLogInfoService;
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        HashMap<String, Object> map = new HashMap<>();

        systemLogInfoService.createLog(
                new SystemLogInfo(0,
                        "匿名用户",
                        request.getRemoteAddr(),
                        "访问",
                        "",
                        "",
                        "收到来自 "+request.getRemoteAddr()+" 的非法访问请求 , 访问接口："+request.getRequestURI()+" , 结果：<div class='deny'>拒绝</div>",
                        "",
                        SYSTEM_WARNING_LOG,
                        0));

        map.put("status",TOKEN_INVALID);
        //输出
        this.WriteJSON(request, response, map);
    }
}
