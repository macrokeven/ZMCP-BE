package com.letoy.main.service.Impl;

import com.letoy.main.dao.ActiveCodeMapper;
import com.letoy.main.dao.TokenInfoMapper;
import com.letoy.main.dao.UserInfoMapper;
import com.letoy.main.entity.auth.ActiveCode;
import com.letoy.main.entity.auth.TokenInfo;
import com.letoy.main.entity.auth.UserInfo;
import com.letoy.main.entity.system.SystemLogInfo;
import com.letoy.main.exception.ActiveCodeException;
import com.letoy.main.exception.SystemException;
import com.letoy.main.service.SystemLogInfoService;
import com.letoy.main.service.UserInfoService;
import com.letoy.main.utils.RandomUtil;
import com.letoy.main.utils.auth.ActiveCodeUtil;
import com.letoy.main.utils.auth.BCryptPasswordEncoderUtil;
import com.letoy.main.utils.auth.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.letoy.main.utils.Constants.*;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    ActiveCodeMapper activeCodeMapper;

    @Resource
    UserInfoMapper userInfoMapper;

    @Resource
    TokenInfoMapper tokenInfoMapper;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    SystemLogInfoService systemLogInfoService;

    @Override
    public List<UserInfo> getAllUserInfo() {
        return userInfoMapper.getAllUserInfo();
    }

    @Override
    public int registerUser(UserInfo userInfo) throws Exception {

        //创建激活码，并标注时间
        String code = RandomUtil.getCode();
        System.out.println("用户：" + userInfo.getPhoneNumber() + " 的验证码为:" + code);

        ActiveCode activeCode = new ActiveCode();
        activeCode.setCode(code);
        activeCode.setPhoneNumber(userInfo.getPhoneNumber());
        activeCode.setType(LOGIN_ACTIVE_CODE);
        activeCode.setCreateTime(dateFormat.format(new Date()));
        if (activeCodeMapper.getActiveCodeByPhoneNumberAndType(userInfo.getPhoneNumber(), LOGIN_ACTIVE_CODE) == null) {
            activeCodeMapper.createActiveCode(activeCode);
        } else {
            activeCodeMapper.updateActiveCodeByPhoneNumberAndType(activeCode);
        }
        if (userInfoMapper.detectionDuplicatePhone(userInfo.getPhoneNumber()) == null) {
            //当该手机号的用户不存在时，创建新的用户
            userInfo.setUid(RandomUtil.getRandomId());
            //初始化Token
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUid(userInfo.getUid());
            tokenInfo.setToken("");
            tokenInfoMapper.createNewTokenInfo(tokenInfo);
            userInfo.setUsername("用户" + RandomUtil.getCode());
            userInfo.setPassword("");
            userInfo.setRole(NORMAL_USER);
            userInfo.setActive(true);
            userInfoMapper.createUserInfoByUserInfo(userInfo);
            return SUCCESS;
        } else {
            /*
             * TODO
             *  以后这里换成判断激活码是否发送成功，并且此处为存在用户直接发验证码去登录的情况
             * */
            return SUCCESS;
        }
    }

    @Override
    public UserInfo getUserInfoByUid(String uid) {
        return userInfoMapper.getUserInfoByUid(uid);
    }

    @Override
    public UserInfo activeUser(String phoneNumber, String code) throws ParseException, ActiveCodeException, SystemException {
        ActiveCode activeCode = activeCodeMapper.getActiveCodeByPhoneNumberAndType(phoneNumber, LOGIN_ACTIVE_CODE);
        if (activeCode != null) {
            if (code.equals(activeCode.getCode())) {
                Date date = new Date();
                if (((date.getTime() - dateFormat.parse(activeCode.getCreateTime()).getTime()) / (1000 * 60)) <= 4) {
                    UserInfo userInfo = userInfoMapper.getUserInfoByPhone(phoneNumber);
//                    userInfoMapper.updateUserInfoByUidWithUserInfo(userInfo.getUid());
                    //TODO active user
                    //生成Token
                    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
                    TokenInfo tokenInfo = new TokenInfo();
                    tokenInfo.setUid(userInfo.getUid());
                    String token = jwtTokenUtil.generateTokenByUid(userInfo.getUid());
                    tokenInfo.setToken(token);
                    tokenInfoMapper.updateTokenInfo(tokenInfo);
                    userInfo.setToken(token);
                    activeCodeMapper.delActiveCodeByPhoneNumberAndType(phoneNumber, LOGIN_ACTIVE_CODE);
                    return userInfo;
                } else {
                    throw new ActiveCodeException("验证码失效！");
                }
            } else {
                throw new ActiveCodeException("验证码错误！");
            }
        } else {
            throw new ActiveCodeException("该账号未注册！或手机号码输入错误！");
        }
    }

    @Override
    public int createNewUserInfoByPhoneNumber(String phoneNumber) {
        UserInfo userInfo = new UserInfo();
        //当该手机号的用户不存在时，创建新的用户
        userInfo.setUid(RandomUtil.getRandomId());
        userInfo.setPhoneNumber(phoneNumber);
        //初始化Token
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUid(userInfo.getUid());
        tokenInfo.setToken("");
        tokenInfoMapper.createNewTokenInfo(tokenInfo);
        userInfo.setUsername("");
        userInfo.setPassword("");
        userInfo.setRole(NORMAL_USER);
        userInfo.setActive(true);
        userInfoMapper.createUserInfoByUserInfo(userInfo);
        return SUCCESS;
    }

    @Override
    public List<UserInfo> getUserInfoByFuzzyQueryUsername(String username) {
        String str = "%" + username + "%";
        return userInfoMapper.getUserInfoByFuzzyQueryUsername(str);
    }

    @Override
    public int updateUserPasswordByUid(String uid, String oldPassword, String newPassword) {
        BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil = new BCryptPasswordEncoderUtil();
        if (bCryptPasswordEncoderUtil.matches(oldPassword, userInfoMapper.getUserInfoByUid(uid).getPassword())) {
            userInfoMapper.resetUserInfoPassword(bCryptPasswordEncoderUtil.encode(newPassword), uid);
            UserInfo userInfo = userInfoMapper.getUserInfoByUid(uid);
            systemLogInfoService.createLog(
                    new SystemLogInfo(0,
                            userInfo.getUsername(),
                            userInfo.getUid(),
                            "重置",
                            "",
                            "用户模块",
                            "用户 ：" + userInfo.getUsername() + " , 进行<div class='log-p-purple'>重置</div>密码的操作",
                            "",
                            TRANSACTION_LOG,
                            USER_INFO_MODULE));
            return SUCCESS;
        } else {
            throw new SystemException("密码错误！");
        }
    }

    @Override
    public int updateUserPasswordByUsername(String username, String password) {
        BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil = new BCryptPasswordEncoderUtil();
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(bCryptPasswordEncoderUtil.encode(password));
        systemLogInfoService.createLog(
                new SystemLogInfo(0,
                        userInfo.getUsername(),
                        userInfo.getUid(),
                        "更新",
                        "",
                        "用户模块",
                        "用户 ：" + userInfo.getUsername() + " , 进行<div class='log-p-s-green'>更新</div>密码的操作",
                        "",
                        TRANSACTION_LOG,
                        USER_INFO_MODULE));
        return userInfoMapper.updateUserInfoByUsernameWithUserInfo(userInfo) >= 1 ? SUCCESS : SYSTEM_ERROR;
    }

    @Override
    public void createUserInfoByUserInfo(UserInfo userInfo, String adminId) {
        if (userInfoMapper.getUserInfoByUsername(userInfo.getUsername()) == null) {
            BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil = new BCryptPasswordEncoderUtil();
            userInfo.setPassword(bCryptPasswordEncoderUtil.encode(userInfo.getPassword()));
            userInfo.setUid(RandomUtil.getRandomId());
            //初始化Token
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUid(userInfo.getUid());
            tokenInfo.setToken("");
            tokenInfoMapper.createNewTokenInfo(tokenInfo);
            userInfoMapper.createUserInfoByUserInfo(userInfo);
            UserInfo adminInfo = userInfoMapper.getUserInfoByUid(adminId);
            systemLogInfoService.createLog(
                    new SystemLogInfo(0,
                            adminInfo.getUsername(),
                            adminInfo.getUid(),
                            "创建",
                            "",
                            "用户模块",
                            "管理员：" + adminInfo.getUsername() + " , 进行<div class='log-p-blue'>创建</div>用户：" + userInfo.getUsername() + " 的操作，新的用户名为：" + userInfo.getUsername(),
                            "",
                            TRANSACTION_LOG,
                            USER_INFO_MODULE));
        } else {
            throw new SystemException("该用户名已被使用！");
        }
    }

    @Override
    public int updateUserInfoByUsername(UserInfo userInfo) {
        if (userInfoMapper.getUserInfoByUsername(userInfo.getUsername()).getUsername().equals(userInfo.getUsername())) {
            systemLogInfoService.createLog(
                    new SystemLogInfo(0,
                            userInfo.getUsername(),
                            userInfo.getUid(),
                            "更新",
                            "",
                            "用户模块",
                            "用户 ：" + userInfo.getUsername() + " , 进行<div class='log-p-s-green'>更新</div>用户名的操作，新的用户名为：" + userInfo.getUsername(),
                            "",
                            TRANSACTION_LOG,
                            USER_INFO_MODULE));
            return userInfoMapper.updateUserInfoByUsernameWithUserInfo(userInfo) >= 1 ? SUCCESS : SYSTEM_ERROR;
        } else {
            return SYSTEM_ERROR;
        }
    }

    @Override
    public void updateUserInfoByUidWithUserInfo(UserInfo userInfo, String adminId) {
        userInfoMapper.updateUserInfoByUidWithUserInfo(userInfo);
        UserInfo adminInfo = userInfoMapper.getUserInfoByUid(adminId);
        systemLogInfoService.createLog(
                new SystemLogInfo(0,
                        adminInfo.getUsername(),
                        adminInfo.getUid(),
                        "更新",
                        "",
                        "用户模块",
                        "管理员：" + adminInfo.getUsername() + " , 进行<div class='log-p-s-green'>更新</div>用户：" + userInfo.getUsername() + " 的信息",
                        "",
                        TRANSACTION_LOG,
                        USER_INFO_MODULE));
    }

    @Override
    public void resetUserInfoPassword(String password, String uid, String adminId) {
        systemLogInfoService.createLog(new SystemLogInfo(0, "管理员",
                "", "重置", "",
                " 用户：" + uid + " 的密码", "",
                "", 0, 0));
        BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil = new BCryptPasswordEncoderUtil();
        UserInfo adminInfo = userInfoMapper.getUserInfoByUid(adminId);
        UserInfo userInfo = userInfoMapper.getUserInfoByUid(uid);
        systemLogInfoService.createLog(
                new SystemLogInfo(0,
                        adminInfo.getUsername(),
                        adminInfo.getUid(),
                        "重置",
                        "",
                        "用户模块",
                        "管理员：" + adminInfo.getUsername() + " , 进行<div class='log-p-purple'>重置</div>用户：" + userInfo.getUsername() + " 的密码",
                        "",
                        TRANSACTION_LOG,
                        USER_INFO_MODULE));
        userInfoMapper.resetUserInfoPassword(bCryptPasswordEncoderUtil.encode(password), uid);
    }

    @Override
    public void deleteUserInfoByUid(String uid, String adminId) {
        systemLogInfoService.createLog(new SystemLogInfo(0, "管理员",
                "", "删除", "",
                " 用户：" + uid + "", "",
                "", 0, 0));
        userInfoMapper.deleteUserInfoByUid(uid);
        UserInfo adminInfo = userInfoMapper.getUserInfoByUid(adminId);
        UserInfo userInfo = userInfoMapper.getUserInfoByUid(uid);
        systemLogInfoService.createLog(
                new SystemLogInfo(0,
                        adminInfo.getUsername(),
                        adminInfo.getUid(),
                        "删除",
                        "",
                        "用户模块",
                        "管理员：" + adminInfo.getUsername() + " , 进行<div class='log-p-red'>删除</div>用户：" + userInfo.getUsername() + " 的操作",
                        "",
                        TRANSACTION_LOG,
                        USER_INFO_MODULE));
    }

    @Override
    public void resetPhoneNumberByPhoneNumberUidAndActiveCode(String newPhoneNumber, String uid, String code) {
        UserInfo userInfo = userInfoMapper.getUserInfoByUid(uid);
        ActiveCode activeCode = activeCodeMapper.getActiveCodeByPhoneNumberAndType(userInfo.getPhoneNumber(), RESET_PHONE_ACTIVE_CODE);
        if (activeCode == null) {
            throw new SystemException("激活码不存在!");
        } else {
            try {
                if (ActiveCodeUtil.checkActiveCode(activeCode)) {
                    if (activeCode.getCode().equals(code)) {
                        userInfoMapper.resetPhoneNumberByUidAndPhoneNumber(uid, newPhoneNumber);
                    } else {
                        throw new SystemException("激活码错误!");
                    }
                } else {
                    throw new SystemException("激活码过期!");
                }
            } catch (Exception e) {
                throw new SystemException(e.getMessage());
            }
        }
    }


    @Override
    public void updateUsernameByUid(String uid, String username) {
        if (userInfoMapper.getUserInfoByUsername(username) == null) {
            this.userInfoMapper.updateUsernameByUid(uid, username);
        } else {
            throw new SystemException("该用户名已被使用！");
        }
    }

    @Override
    public List<String> fuzzyQueryUsernameBySearchUsername(String searchUsername) {
        return userInfoMapper.fuzzyQueryUsernameBySearchUsername(searchUsername);
    }
}
