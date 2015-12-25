package com.jhipster.application.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by dimonn12 on 16.10.2015.
 */
@Target({ TYPE })
@Retention(RUNTIME)
public @interface AuthorityMethodText {

    AuthorityMethod authority() default AuthorityMethod.USER;

    enum AuthorityMethod {
        ADMIN, USER;
    }
}
