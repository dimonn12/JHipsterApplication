package com.jhipster.application.postprocessor.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by dimonn12 on 16.10.2015.
 */
@Target({ METHOD })
@Retention(RUNTIME)
public @interface RestResponse {

    ResponseReturnType type() default ResponseReturnType.DEFAULT;

    public enum ResponseReturnType {
        LIST, BASE_ENTITY, SAVE_REQUEST, BASE_DTO, STRING, HTTP_HEADERS_CONTAINER, URI_CONTAINER, DEFAULT
    }

}
