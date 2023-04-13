package com.letoy.main.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
import static com.letoy.main.utils.Constants.NO_AUTHENTICATION;

/**
 * 权限校验处理器
 */
@Component
public class MyAccessDeniedHandler extends JSONAuthentication implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", NO_AUTHENTICATION);
        map.put("msg","Request not permitted!");
        //输出
        this.WriteJSON(request, response, map);
    }
}
