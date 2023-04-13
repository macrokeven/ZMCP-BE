package com.letoy.main.config.auth;

import com.letoy.main.entity.auth.MyUsernamePasswordAuthenticationToken;
import com.letoy.main.exception.PasswordNotMatchException;
import com.letoy.main.service.MyAuthUserService;
import com.letoy.main.utils.auth.BCryptPasswordEncoderUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUsernamePasswordProvider implements AuthenticationProvider {

    MyAuthUserService myAuthUserService;

    public MyUsernamePasswordProvider(MyAuthUserService myAuthUserService) {
        this.myAuthUserService = myAuthUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyUsernamePasswordAuthenticationToken token = (MyUsernamePasswordAuthenticationToken) authentication;
        UserDetails userDetails = myAuthUserService.getAuthUserByUsername(
                (String) token.getPrincipal());
        if (userDetails != null) {
            //进行密码认证
            BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil = new BCryptPasswordEncoderUtil();
            if (bCryptPasswordEncoderUtil.matches((String) token.getCredentials(), userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            }
        }
        throw new PasswordNotMatchException("用户密码错误！");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return MyUsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
