package com.letoy.main.config.auth;

import com.letoy.main.entity.auth.PhoneNumberActiveCodeAuthUser;
import com.letoy.main.entity.auth.PhoneNumberActiveCodeAuthenticationToken;
import com.letoy.main.exception.MyAuthenticationException;
import com.letoy.main.exception.PasswordNotMatchException;
import com.letoy.main.exception.UserNotExistsException;
import com.letoy.main.service.MyAuthUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

public class MyPhoneActiveCodeProvider implements AuthenticationProvider {
    MyAuthUserService myAuthUserService;
    public MyPhoneActiveCodeProvider(MyAuthUserService myAuthUserService) {
        this.myAuthUserService = myAuthUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {

        /*
         * TODO
         *  解决异地同时登录的问题
         * */
        PhoneNumberActiveCodeAuthenticationToken token = (PhoneNumberActiveCodeAuthenticationToken) authentication;
        String phoneNumber = (String) token.getPrincipal();
        String activeCode = (String) token.getCredentials();
        PhoneNumberActiveCodeAuthUser phoneNumberActiveCodeAuthUser =
                myAuthUserService.getPhoneNumberActiveCodeAuthUserByPhoneNumber(phoneNumber);
        if (phoneNumberActiveCodeAuthUser == null) {
            throw new UserNotExistsException("用户不存在！");
        } else {
            if (phoneNumberActiveCodeAuthUser.getActiveCode() == null || phoneNumberActiveCodeAuthUser.getActiveCode().equals("")) {
                throw new MyAuthenticationException("激活码不存在");
            } else {
                if ((phoneNumberActiveCodeAuthUser.getActiveCode()).equals(activeCode)) {
                    //激活码正确
                    return new PhoneNumberActiveCodeAuthenticationToken(phoneNumberActiveCodeAuthUser,
                            phoneNumberActiveCodeAuthUser.getActiveCode(), phoneNumberActiveCodeAuthUser.getAuthorities());
                } else {
                    throw new PasswordNotMatchException("用户密码错误！");
                }
            }
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneNumberActiveCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
