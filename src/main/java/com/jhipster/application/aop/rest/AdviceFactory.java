package com.jhipster.application.aop.rest;

import org.aopalliance.aop.Advice;

/**
 * Created by dimonn12 on 17.10.2015.
 */
public interface AdviceFactory {

    Advice getRestResponseAdvice(Object bean, Class<?> targetClass);

}

