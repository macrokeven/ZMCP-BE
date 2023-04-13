package com.letoy.main.utils.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderUtil extends BCryptPasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil = new BCryptPasswordEncoderUtil();
        System.out.println(bCryptPasswordEncoderUtil.encode(MD5Util.encode("lxrnb2023!!")));
    }
    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        return super.matches(rawPassword, encodedPassword);
    }

}
