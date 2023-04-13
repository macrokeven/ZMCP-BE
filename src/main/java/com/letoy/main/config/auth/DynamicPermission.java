package com.letoy.main.config.auth;


import com.letoy.main.dao.ApiInfoMapper;
import com.letoy.main.entity.auth.ApiInfo;
import com.letoy.main.entity.auth.AuthUser;
import com.letoy.main.entity.auth.UserInfo;
import com.letoy.main.entity.system.SystemLogInfo;
import com.letoy.main.service.SystemLogInfoService;
import com.letoy.main.service.UserInfoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.letoy.main.utils.Constants.SYSTEM_ACCESS_LOG;

@Component
public class DynamicPermission {

    @Resource
    UserInfoService userInfoService;

    @Resource
    SystemLogInfoService systemLogInfoService;

    @Resource
    ApiInfoMapper apiInfoMapper;

    public boolean checkPermission(HttpServletRequest request,
                                   Authentication authentication) {

        Object principal = authentication.getPrincipal();
        //判断是否能转换成UserDetails
        if (principal instanceof AuthUser) {
            AuthUser authUser = (AuthUser) principal;
            //获取当前登录的id
            String userId = authUser.getId();
            UserInfo userInfo = userInfoService.getUserInfoByUid(userId);
            List<ApiInfo> myApiList = apiInfoMapper.getApiInfoListByUid(userId);
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            //当前访问路径
            String requestURI = request.getRequestURI();
            //提交类型
            String urlMethod = request.getMethod();
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            System.out.println("用户名： " + userInfo.getUsername() +
                    " 请求URI： " + request.getRequestURI() + " 操作时间：" + dateFormat.format(date));
            boolean AuthenticationResult = myApiList.stream().anyMatch(item -> {
                boolean hashAntPath = antPathMatcher.match(item.getUrl(), requestURI);
                //判断请求方式是否和数据库中匹配（数据库存储：GET,POST,PƒUT,DELETE）
                String dbMethod = item.getMethod();
                //处理null，万一数据库存值
                dbMethod = (dbMethod == null) ? "" : dbMethod;
                int hasMethod = dbMethod.indexOf(urlMethod);
                //两者都成立，返回真，否则返回假
                if (hashAntPath && (hasMethod != -1)) {
                    systemLogInfoService.createLog(
                            new SystemLogInfo(0,
                                    userInfo.getUsername(),
                                    userInfo.getUid(),
                                    "访问",
                                    "",
                                    item.getName(),
                                    "用户："+userInfo.getUsername()+" , 访问接口："+requestURI+" "+item.getName()+" , 结果：<div class='allow'>放行</div>",
                                    "",
                                    SYSTEM_ACCESS_LOG,
                                    0));
                }
                return hashAntPath && (hasMethod != -1);
            });
            if(!AuthenticationResult){
                systemLogInfoService.createLog(
                        new SystemLogInfo(0,
                                userInfo.getUsername(),
                                userInfo.getUid(),
                                "访问",
                                "",
                                "",
                                "用户："+userInfo.getUsername()+" , 访问接口："+requestURI+" "+" , 结果：<div class='deny'>因权限不足禁止访问</div>",
                                "",
                                SYSTEM_ACCESS_LOG,
                                0));
            }
            return AuthenticationResult;
        } else {
            return false;
        }
    }
}

























