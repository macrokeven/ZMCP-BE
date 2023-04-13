package com.letoy.main.config.auth;


import com.letoy.main.dao.ActiveCodeMapper;
import com.letoy.main.dao.TokenInfoMapper;
import com.letoy.main.dao.UserInfoMapper;
import com.letoy.main.entity.auth.AuthUser;
import com.letoy.main.entity.auth.PhoneNumberActiveCodeAuthUser;
import com.letoy.main.entity.auth.TokenInfo;
import com.letoy.main.entity.auth.UserInfo;
import com.letoy.main.exception.SystemException;
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

import static com.letoy.main.utils.Constants.LOGIN_ACTIVE_CODE;
import static com.letoy.main.utils.Constants.SUCCESS;

/**
 * 登录成功操作
 */
@Component
public class MyPhoneActiveCodeAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {

    @Resource
    UserInfoService userService;

    @Resource
    UserInfoMapper userInfoMapper;

    @Resource
    ActiveCodeMapper activeCodeMapper;

    @Resource
    TokenInfoMapper tokenInfoMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //取得账号信息
        PhoneNumberActiveCodeAuthUser phoneNumberActiveCodeAuthUser = (PhoneNumberActiveCodeAuthUser) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserInfo userInfo = userInfoMapper.getUserInfoByPhone(phoneNumberActiveCodeAuthUser.getPhoneNumber());
        if (userInfo == null) {
            if (userService.createNewUserInfoByPhoneNumber(phoneNumberActiveCodeAuthUser.getPhoneNumber()) == SUCCESS) {
                userInfo = userInfoMapper.getUserInfoByPhone(phoneNumberActiveCodeAuthUser.getPhoneNumber());
            } else {
                System.out.println("创建用户：" + phoneNumberActiveCodeAuthUser.getPhoneNumber() + " , 失败");
            }
        }
        String token = "";
        AuthUser authUser = new AuthUser();
        authUser.setPhoneNumber(phoneNumberActiveCodeAuthUser.getPhoneNumber());
        assert userInfo != null;
        authUser.setId(userInfo.getUid());
        try {
            token = createNewToken(authUser);
        } catch (SystemException e) {
            System.out.println(e.getMessage());
        }

        HashMap<String, Object> map = new HashMap<>();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("--------用户：" + userInfo.getUsername() + " 登录时间：" + dateFormat.format(date) + "------");
        map.put("status", SUCCESS);
        map.put("username", authUser.getUsername());
        map.put("msg", "登陆成功！");
        map.put("role", userInfo.getRole());
        map.put("token", token);
        activeCodeMapper.delActiveCodeByPhoneNumberAndType(phoneNumberActiveCodeAuthUser.getPhoneNumber(),LOGIN_ACTIVE_CODE);
        //输出
        this.WriteJSON(request, response, map);
    }

    String createNewToken(AuthUser authUser) throws SystemException {
        TokenInfo tokenInfo = new TokenInfo();
        jwtTokenUtil = new JwtTokenUtil();
        String token = jwtTokenUtil.generateToken(authUser);
        //把新的token存储到缓存中
        tokenInfo.setUid(authUser.getId());
        tokenInfo.setToken(token);
        tokenInfoMapper.updateTokenInfo(tokenInfo);
        return token;
    }
}
