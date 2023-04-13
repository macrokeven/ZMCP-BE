package com.letoy.main.service.Impl;

import com.letoy.main.dao.ActiveCodeMapper;
import com.letoy.main.dao.UserInfoMapper;
import com.letoy.main.entity.auth.ActiveCode;
import com.letoy.main.entity.auth.AuthUser;
import com.letoy.main.entity.auth.PhoneNumberActiveCodeAuthUser;
import com.letoy.main.entity.auth.UserInfo;
import com.letoy.main.service.MyAuthUserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.letoy.main.utils.Constants.LOGIN_ACTIVE_CODE;

@Service
public class MyAuthUserServiceImpl implements MyAuthUserService {

    @Resource
    UserInfoMapper userInfoMapper;

    @Resource
    ActiveCodeMapper activeCodeMapper;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public AuthUser getUserByPhoneNumberAndActiveCode(String phoneNumber,String activeCode) {

        if (!activeCode.equals(activeCodeMapper.getActiveCodeByPhoneNumberAndType(phoneNumber,LOGIN_ACTIVE_CODE).getCode())) {
            throw new UsernameNotFoundException(String.format("用户 %s. 激活码密码错误", phoneNumber));
        } else {
            UserInfo userInfo = userInfoMapper.getUserInfoByPhone(phoneNumber);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(String.valueOf(userInfo.getRole())));
            return new AuthUser(phoneNumber , userInfo.getUsername(), "", userInfo.getUid(), userInfo.getPassword(), 0, authorities);
        }
    }

    @Override
    public PhoneNumberActiveCodeAuthUser getPhoneNumberActiveCodeAuthUserByPhoneNumber(String phone) {
        PhoneNumberActiveCodeAuthUser phoneNumberActiveCodeAuthUser = new PhoneNumberActiveCodeAuthUser();
        phoneNumberActiveCodeAuthUser.setPhoneNumber(phone);
        ActiveCode activeCode = activeCodeMapper.getActiveCodeByPhoneNumberAndType(phone,LOGIN_ACTIVE_CODE);
        Date date = new Date();
        try {
            if(activeCode == null){
                phoneNumberActiveCodeAuthUser.setActiveCode("");
                return phoneNumberActiveCodeAuthUser;
            }else {
                if (((date.getTime() - dateFormat.parse(activeCode.getCreateTime()).getTime()) / (1000 * 60)) <= 4) {
                    phoneNumberActiveCodeAuthUser.setActiveCode("");
                }
                return phoneNumberActiveCodeAuthUser;
            }
        } catch (ParseException e) {
            System.out.println("ParseException");
            e.printStackTrace();
        }
        phoneNumberActiveCodeAuthUser.setActiveCode(activeCode.getCode());
        return phoneNumberActiveCodeAuthUser;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public AuthUser getUserByUserId(String userId) {
        UserInfo user = userInfoMapper.getUserInfoByUid(userId);
        AuthUser authUser = new AuthUser();
        authUser.setId(user.getUid());
        return authUser;
    }

    @Override
    public AuthUser getAuthUserByUsername(String username) {
        UserInfo userInfo = userInfoMapper.getUserInfoByUsername(username);
        if(userInfo != null){
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(String.valueOf(userInfo.getRole())));
            return new AuthUser(userInfo.getPhoneNumber() , username, "", userInfo.getUid(), userInfo.getPassword(), 0, authorities);
        }else{
            return null;
        }
    }


    @Override
    public AuthUser getUserByUid(String uid) {
        UserInfo userInfo = userInfoMapper.getUserInfoByUid(uid);
        AuthUser authUser = new AuthUser();
        authUser.setId(userInfo.getUid());
        return authUser;
    }
}
