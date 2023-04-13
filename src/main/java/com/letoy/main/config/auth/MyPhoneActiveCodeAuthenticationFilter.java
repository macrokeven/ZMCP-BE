package com.letoy.main.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letoy.main.entity.auth.PhoneNumberActiveCodeAuthenticationToken;
import com.letoy.main.exception.IllegalAuthenticationInputException;
import com.letoy.main.utils.auth.InputValidator;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.letoy.main.utils.Constants.*;


public class MyPhoneActiveCodeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /*
         * TODO
         *  这边对同一时间内登录次数做限制
         * */
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            ObjectMapper mapper = new ObjectMapper();
            PhoneNumberActiveCodeAuthenticationToken authRequest;
            // 初始化一个AuthenticationBean
            Map<String, String> authenticationBean;
            try (InputStream is = request.getInputStream()) {
                authenticationBean = mapper.readValue(is, Map.class);
            }
            if (authenticationBean != null) {
                String phoneNumber = authenticationBean.get(DEFAULT_PHONE_NUMBER);
                String activeCode = authenticationBean.get(DEFAULT_ACTIVE_CODE);
                if (!authenticationBean.get("loginMethod").equals("phone_code")) {
                    this.attemptAuthentication(request, response);
                }
                //对输入的字符串进行格式的校验
                if (!InputValidator.isMobile(phoneNumber) || !Pattern.matches("^\\d{6}$", activeCode)) {
                    HashMap<String, Object> resultMap = new HashMap<>();
                    resultMap.put("status", EXPECTED_ERROR);
                    resultMap.put("msg", "输入格式有误");
                    JSONAuthentication.WriteJSONS(request, response, resultMap);
                    throw new IllegalAuthenticationInputException("输入格式有误");
                }
                //根据传入的用户信息创建Security需要的内部验证的TOKEN
                authRequest = new PhoneNumberActiveCodeAuthenticationToken(phoneNumber, activeCode);
                authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }
        return this.attemptAuthentication(request, response);

    }
}
