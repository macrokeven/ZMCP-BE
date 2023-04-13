package com.letoy.main.config.auth;

import com.letoy.main.service.MyAuthUserService;
import com.letoy.main.utils.auth.BCryptPasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Security授权配置主文件
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Resource
    MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Resource
    MyAccessDeniedHandler myAccessDeniedHandler;
    @Resource
    MyAuthUserService myAuthUserService;
    @Resource
    MyLogoutHandler myLogoutHandler;
    @Resource
    MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Resource
    MyPhoneActiveCodeAuthenticationSuccessHandler myPhoneActiveCodeAuthenticationSuccessHandler;
    @Resource
    MyUsernamePasswordAuthenticationSuccessHandler myUsernamePasswordAuthenticationSuccessHandler;
    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Resource
    BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;
    @Resource
    MyOncePerRequestFilter myOncePerRequestFilter;

    @Resource
    DynamicPermission dynamicPermission;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(myAuthUserService).passwordEncoder(bCryptPasswordEncoderUtil);
    }

    private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-ui.html",
            "/doc.html",
            "/swagger-ui/*",
            "/webjars/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/ws/**",
            "/swagger-resources/**",
            "/ActiveCode/GetLoginPhoneNumberActiveCodeByPhoneNumber",
            "/SystemLogInfo/GetSystemLogToExcel",
            "/LeOrder/**",
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //第1步：解决跨域问题。cors 预检请cors求放行,让Spring security 放行所有preflight request（cors 预检请求）
        http.cors();
//        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
        //第2步：让Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl();
        //第3步：请求权限配置
        //放行注册API请求，其它任何请求都必须经过身份验证.
        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                //ROLE_ADMIN可以操作任何事情
                //.antMatchers("/**").hasRole("ADMIN")
                //同等上一行代码
                //.antMatchers("/**").hasAuthority("ROLE_ADMIN")
                //动态加载资源
                .anyRequest().access("@dynamicPermission.checkPermission(request,authentication)");

        MyPhoneActiveCodeProvider myPhoneActiveCodeProvider = new MyPhoneActiveCodeProvider(myAuthUserService);
        http.authenticationProvider(myPhoneActiveCodeProvider)
                .addFilterAt(myPhoneNumberActiveCodeAuthenticationFilter(), MyPhoneActiveCodeAuthenticationFilter.class);

        MyUsernamePasswordProvider myUsernamePasswordProvider = new MyUsernamePasswordProvider(myAuthUserService);
        http.authenticationProvider(myUsernamePasswordProvider)
                .addFilterAt(myUsernamePasswordAuthenticationFilter(), MyUsernamePasswordAuthenticationFilter.class);

//        //第5步：拦截token，并检测。在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
        http.addFilterBefore(myOncePerRequestFilter, MyPhoneActiveCodeAuthenticationFilter.class);

        http.addFilterBefore(myUsernamePasswordAuthenticationFilter(), MyPhoneActiveCodeAuthenticationFilter.class);

//        //第6步：处理异常情况：认证失败和权限不足
        http.exceptionHandling().authenticationEntryPoint(myAuthenticationEntryPoint).accessDeniedHandler(myAccessDeniedHandler);
//
//        //第7步：登录,因为使用前端发送JSON方式进行登录，所以登录模式不设置也是可以的。
        http.formLogin().successHandler(myUsernamePasswordAuthenticationSuccessHandler).failureHandler(myAuthenticationFailureHandler);
//
////        //第8步：退出
        http.logout().addLogoutHandler(myLogoutHandler).logoutSuccessHandler(myLogoutSuccessHandler);
    }

    @Bean
    MyPhoneActiveCodeAuthenticationFilter myPhoneNumberActiveCodeAuthenticationFilter() throws Exception{
        MyPhoneActiveCodeAuthenticationFilter filter = new MyPhoneActiveCodeAuthenticationFilter();
        //成功后处理
        filter.setAuthenticationSuccessHandler(myPhoneActiveCodeAuthenticationSuccessHandler);
        //失败后处理
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        //成功后处理
        filter.setAuthenticationSuccessHandler(myUsernamePasswordAuthenticationSuccessHandler);
        //失败后处理
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8080","http://localhost:8081","http://101.42.31.172:8102"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}