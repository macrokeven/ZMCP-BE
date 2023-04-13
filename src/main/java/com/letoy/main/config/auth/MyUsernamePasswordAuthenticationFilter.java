
package com.letoy.main.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letoy.main.entity.auth.MyUsernamePasswordAuthenticationToken;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println(request.getRequestURI());
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            ObjectMapper mapper = new ObjectMapper();
            MyUsernamePasswordAuthenticationToken authRequest;
            // 初始化一个AuthenticationBean
            Map<String, String> authenticationBean;
            try (InputStream is = request.getInputStream()) {
                authenticationBean =mapper.readValue(is, Map.class);
            }
            if (authenticationBean != null) {
                String username = authenticationBean.get("username");
                String password = authenticationBean.get("password");
                if(username.equals("")&&password.equals("")){
                    this.attemptAuthentication(request, response);
                }
                if(!authenticationBean.get("loginMethod").equals("name_pwd")){
                    this.attemptAuthentication(request, response);
                }
                authRequest = new MyUsernamePasswordAuthenticationToken(username, password);
                authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }
        return this.attemptAuthentication(request, response);
    }
}
