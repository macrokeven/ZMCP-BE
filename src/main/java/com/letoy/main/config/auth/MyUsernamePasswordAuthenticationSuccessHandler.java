package com.letoy.main.config.auth;


import com.letoy.main.dao.TokenInfoMapper;
import com.letoy.main.entity.auth.AuthUser;
import com.letoy.main.entity.auth.TokenInfo;
import com.letoy.main.entity.auth.UserInfo;
import com.letoy.main.entity.system.SystemLogInfo;
import com.letoy.main.exception.SystemException;
import com.letoy.main.service.SystemLogInfoService;
import com.letoy.main.service.UserInfoService;
import com.letoy.main.utils.auth.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.letoy.main.utils.Constants.*;

/**
 * 登录成功操作
 */
@Component
public class MyUsernamePasswordAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {

    @Resource
    SystemLogInfoService systemLogInfoService;

    @Resource
    UserInfoService userInfoService;

    @Resource
    TokenInfoMapper tokenInfoMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //取得账号信息
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenInfo tokenInfo = tokenInfoMapper.getTokenInfoByUid(authUser.getId());
        tokenInfo.setUid(authUser.getId());
        String token = tokenInfo.getToken();
        if (token == null) {
            try {
                token = createNewToken(tokenInfo, authUser);
            } catch (SystemException e) {
                System.out.println(e.getMessage());
            }
        } else {
            Boolean validate = false;
            if (!token.equals("")) {
                try {
                    validate = jwtTokenUtil.validateToken(token, authUser);
                } catch (Exception e) {
                    System.out.println("验证token无效:" + e.getMessage());
                }
            }
            if (token.equals("") | !validate) {
                try {
                    token = createNewToken(tokenInfo, authUser);
                } catch (SystemException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        HashMap<String, Object> map = new HashMap<>();
        UserInfo userInfo = userInfoService.getUserInfoByUid(authUser.getId());
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("--------用户：" + userInfo.getUsername() + " 登录时间：" + dateFormat.format(date) + "------");

        systemLogInfoService.createLog(
                new SystemLogInfo(0,
                        userInfo.getUsername(),
                        userInfo.getUid(),
                        "访问",
                        "",
                        "登录",
                        "用户："+userInfo.getUsername()+" 进行登录 , 结果：<div class='allow'>通过</div>",
                        "",
                        SYSTEM_ACCESS_LOG,
                        USER_INFO_MODULE));
        map.put("status", SUCCESS);
        map.put("msg", "登陆成功");
        map.put("username", authUser.getUsername());
        map.put("role", userInfo.getRole());
        map.put("token", token);
        //输出
        this.WriteJSON(request, response, map);
    }

    String createNewToken(TokenInfo tokenInfo, AuthUser authUser) throws SystemException {
        jwtTokenUtil = new JwtTokenUtil();
        String token = jwtTokenUtil.generateToken(authUser);
        //把新的token存储到缓存中
        tokenInfo.setToken(token);
        tokenInfoMapper.updateTokenInfo(tokenInfo);
        return token;
    }
}
