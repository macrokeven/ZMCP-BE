package com.letoy.main.config.auth;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter extends OncePerRequestFilter {

//public class CorsFilter implements Filter {
//    static final String ORIGIN = "Origin";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

//        String origin = request.getHeader(ORIGIN);

        response.setHeader("Access-Control-Allow-Origin", "*");//* or origin as u prefer
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "content-type, authorization");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization");
        response.setHeader("XDomainRequestAllowed","1");
        //使前端能够获取到
        response.setHeader("Access-Control-Expose-Headers","download-status,download-filename,download-message");


        if ("OPTIONS".equals(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        else {
            filterChain.doFilter(request, response);
        }

    }

//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res,
//                         FilterChain chain) throws IOException, ServletException {
//
//        HttpServletResponse response = (HttpServletResponse) res;
//        //测试环境用【*】匹配，上生产环境后需要切换为实际的前端请求地址
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//
//        response.setHeader("Access-Control-Max-Age", "0");
//
//        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, auth");
//
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//
//        response.setHeader("XDomainRequestAllowed","1");
//        chain.doFilter(req, res);
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//    @Override
//    public void init(FilterConfig arg0) throws ServletException {
//    }
}
