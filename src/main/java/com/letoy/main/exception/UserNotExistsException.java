package com.letoy.main.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotExistsException extends AuthenticationException {

    /**
     * Constructs a <code>BadCredentialsException</code> with the specified message.
     * @param msg the detail message
     */
    public UserNotExistsException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>BadCredentialsException</code> with the specified message and
     * root cause.
     * @param msg the detail message
     * @param cause root cause
     */
    public UserNotExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
