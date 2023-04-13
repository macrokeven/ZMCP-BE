package com.letoy.main.utils.auth;

import com.letoy.main.entity.auth.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "letoy-private-key";
    
    private static final long EXPIRATION_TIME = 3600000L * 24 * 7;
    
    public String generateToken(AuthUser authUser) {
        HashMap<String, Object> claims = new HashMap<>(2);
        claims.put(Claims.SUBJECT, authUser.getId());
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }

    public String generateTokenByUid(String uid) {
        HashMap<String, Object> claims = new HashMap<>(2);
        claims.put(Claims.SUBJECT, uid);
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }
    
    public String getUidFromToken(String token) {
        String uid = "";
        try {
            Claims claims = getClaimsFromToken(token);
            uid = claims.getSubject();
        } catch (Exception e) {
            System.out.println("e = " + e.getMessage());
        }
        return uid;
    }

    public Boolean isTokenExpired(String token) throws Exception {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            new Throwable(e);
        }
        return true;
    }

    public Boolean validateToken(String token, AuthUser authUser) throws Exception {
        String userId = getUidFromToken(token);
//        System.out.println("token userid check: "+userId.equals(authUser.getId()));
        return (userId.equals(authUser.getId()) && !isTokenExpired(token));
    }


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(HashMap<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) throws Exception {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            new Throwable(e);
        }
        return claims;
    }
}