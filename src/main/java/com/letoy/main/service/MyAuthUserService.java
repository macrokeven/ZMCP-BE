package com.letoy.main.service;

import com.letoy.main.entity.auth.AuthUser;
import com.letoy.main.entity.auth.PhoneNumberActiveCodeAuthUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MyAuthUserService extends UserDetailsService {
    AuthUser getUserByPhoneNumberAndActiveCode(String phoneNumber,String activeCode);
    PhoneNumberActiveCodeAuthUser getPhoneNumberActiveCodeAuthUserByPhoneNumber(String phoneNumber);
    AuthUser getUserByUserId(String userId);
    AuthUser getAuthUserByUsername(String username);
    AuthUser getUserByUid(String uid);
}
