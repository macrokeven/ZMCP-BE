package com.letoy.main.config.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.letoy.main.utils.Constants.EXPECTED_ERROR;
import static com.letoy.main.utils.Constants.LOGIN_FAILED;

/**
 * 登录失败操作
 */
@Component
public class MyAuthenticationFailureHandler extends JSONAuthentication implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("MyAuthenticationFailureHandler:"+ e);
        System.out.println("--------操作时间：" + dateFormat.format(date) + " 请求URI： " + request.getRequestURI() + "------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", LOGIN_FAILED);
        map.put("msg",e.getMessage());
        //输出
        this.WriteJSON(request, response, map);
    }
}