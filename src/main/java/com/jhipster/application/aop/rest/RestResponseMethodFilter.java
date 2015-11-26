package com.jhipster.application.aop.rest;

import com.jhipster.application.postprocessor.annotation.RestResponse;
import org.springframework.util.ReflectionUtils.MethodFilter;

import java.lang.reflect.Method;

/**
 * Created by dimonn12 on 17.10.2015.
 */
public class RestResponseMethodFilter implements MethodFilter {

    @Override
    public boolean matches(Method method) {
        return method.isAnnotationPresent(RestResponse.class);
    }
}
