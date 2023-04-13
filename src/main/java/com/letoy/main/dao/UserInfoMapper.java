package com.letoy.main.dao;

import com.letoy.main.entity.auth.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    void createUserInfoByUserInfo(UserInfo userInfo);
    List<UserInfo> getAllUserInfo();
    //检测用户手机号是否重复
    String detectionDuplicatePhone(String phone);
    UserInfo getUserInfoByPhone(String phone);
    UserInfo getUserInfoByUid(String uid);
    List<UserInfo> getUserInfoByUidList(@Param("uidList") List<String> uidList);
    List<UserInfo> getUserInfoByFuzzyQueryUsername(String username);
    UserInfo getUserInfoByUsername(String username);
    void updateUserInfoByUidWithUserInfo(UserInfo userInfo);
    int updateUserInfoByUsernameWithUserInfo(UserInfo userInfo);
    void deleteUserInfoByUid(String uid);
    void resetUserInfoPassword(String password, String uid);
    void resetPhoneNumberByUidAndPhoneNumber(String uid,String phoneNumber);
    void updateUsernameByUid(String uid,String username);
    List<String> fuzzyQueryUsernameBySearchUsername(String searchUsername);
}
