package com.jhipster.application.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is throw in case of a not activated user trying to authenticate.
 */
public class UserIsLockedException extends AuthenticationException {

    public UserIsLockedException(String message) {
        super(message);
    }

    public UserIsLockedException(String message, Throwable t) {
        super(message, t);
    }
}
