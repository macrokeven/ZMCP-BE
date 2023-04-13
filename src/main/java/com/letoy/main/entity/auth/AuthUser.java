package com.letoy.main.entity.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 要实现UserDetails接口，这个接口是Security提供的
 */
@Component
public class AuthUser implements UserDetails {

    private String phoneNumber;
    private String username;
    private String systemToken;
    private String id;
    private String password;
    private Integer state;

    private Collection<? extends GrantedAuthority> authorities;

    public AuthUser() {
    }


    public String getSystemToken() {
        return systemToken;
    }

    public void setSystemToken(String systemToken) {
        this.systemToken = systemToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 账户是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未被锁
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public AuthUser(String phoneNumber, String username, String systemToken, String id, String password, Integer state, Collection<? extends GrantedAuthority> authorities) {
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.systemToken = systemToken;
        this.id = id;
        this.password = password;
        this.state = state;
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", username='" + username + '\'' +
                ", systemToken='" + systemToken + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", state=" + state +
                ", authorities=" + authorities +
                '}';
    }
}
