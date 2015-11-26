package com.jhipster.application.aop.rest;

import org.aopalliance.aop.Advice;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * Created by dimonn12 on 17.10.2015.
 */
@Component
public class AdviceFactoryImpl implements AdviceFactory{

    @Override
    public Advice getRestResponseAdvice(Object bean, Class<?> targetClass) {
        return new RestResponseMethodInterceptor(targetClass);
    }
}
