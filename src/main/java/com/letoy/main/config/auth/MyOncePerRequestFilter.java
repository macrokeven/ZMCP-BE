package com.letoy.main.config.auth;

import com.letoy.main.dao.TokenInfoMapper;
import com.letoy.main.entity.auth.AuthUser;
import com.letoy.main.service.MyAuthUserService;
import com.letoy.main.utils.auth.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器
 */
@Component
public class MyOncePerRequestFilter extends OncePerRequestFilter {

    @Qualifier("myAuthUserServiceImpl")
    @Resource
    private MyAuthUserService myAuthUserService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    TokenInfoMapper tokenInfoMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = "Authorization";
        String headerToken = request.getHeader(header);
        if (!StringUtils.isEmpty(headerToken)) {
            String token = headerToken.replace("Bearer", "").trim();
            boolean check = false;
            try {
                check = this.jwtTokenUtil.isTokenExpired(token);
            } catch (Exception e) {
                System.out.println("MYONCE Throw");
            }
            if (!check) {
                String userId = jwtTokenUtil.getUidFromToken(token);
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //通过用户信息得到UserDetails
                    AuthUser authUser = myAuthUserService.getUserByUserId(userId);
                    //验证令牌有效性
                    boolean validate = false;
                    try {
                        validate = jwtTokenUtil.validateToken(token, authUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (validate) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        authUser,
                                        null,
                                        authUser.getAuthorities()
                                );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else {
                tokenInfoMapper.cleanToken(token);
            }
        }
        chain.
                doFilter(request, response);
    }
}
