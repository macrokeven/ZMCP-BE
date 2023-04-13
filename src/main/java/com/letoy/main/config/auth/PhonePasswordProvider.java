package com.letoy.main.config.auth;

import com.letoy.main.entity.auth.PhoneNumberActiveCodeAuthenticationToken;
import com.letoy.main.service.MyAuthUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class PhonePasswordProvider implements AuthenticationProvider {

    MyAuthUserService myAuthUserService;

    public PhonePasswordProvider(MyAuthUserService myAuthUserService) {
        this.myAuthUserService = myAuthUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        System.out.println("work");
        PhoneNumberActiveCodeAuthenticationToken token = (PhoneNumberActiveCodeAuthenticationToken) authentication;
        UserDetails user = myAuthUserService.getUserByPhoneNumberAndActiveCode(
                (String) token.getPrincipal(),
                (String) token.getCredentials());
        if (user == null) {
            return null;
        } else {
            return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneNumberActiveCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
