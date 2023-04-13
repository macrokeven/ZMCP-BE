package com.letoy.main.exception;

import org.springframework.security.core.AuthenticationException;

public class PasswordNotMatchException extends AuthenticationException {
    /**
     * Constructs a <code>BadCredentialsException</code> with the specified message.
     * @param msg the detail message
     */
    public PasswordNotMatchException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>BadCredentialsException</code> with the specified message and
     * root cause.
     * @param msg the detail message
     * @param cause root cause
     */
    public PasswordNotMatchException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
