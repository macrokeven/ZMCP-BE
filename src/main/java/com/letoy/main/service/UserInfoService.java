package com.letoy.main.service;

import com.letoy.main.entity.auth.UserInfo;
import com.letoy.main.exception.ActiveCodeException;
import com.letoy.main.exception.SystemException;

import java.text.ParseException;
import java.util.List;

public interface UserInfoService {

    List<UserInfo> getAllUserInfo();
    int registerUser(UserInfo userInfo) throws Exception;
    UserInfo getUserInfoByUid(String userId);
    UserInfo activeUser(String phoneNumber , String code) throws ParseException, ActiveCodeException, SystemException;
    int createNewUserInfoByPhoneNumber(String phoneNumber);
    List<UserInfo> getUserInfoByFuzzyQueryUsername(String username);
    int updateUserPasswordByUid(String uid, String oldPassword, String newPassword);
    int updateUserPasswordByUsername(String username,String password);
//    int updateUserInfoByUid(UserInfo userInfo);
    int updateUserInfoByUsername(UserInfo userInfo);
    void updateUserInfoByUidWithUserInfo(UserInfo userInfo,String adminId);
    void deleteUserInfoByUid(String uid,String adminId);
    void createUserInfoByUserInfo(UserInfo userInfo,String adminId);
    void resetUserInfoPassword(String password,String uid,String adminId);
    void resetPhoneNumberByPhoneNumberUidAndActiveCode(String newPhoneNumber,String uid,String code);
    void updateUsernameByUid(String uid,String username);
    List<String> fuzzyQueryUsernameBySearchUsername(String searchUsername);

}
