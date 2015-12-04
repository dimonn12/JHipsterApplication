package com.jhipster.application.aop.rest;

import com.jhipster.application.web.rest.processor.RequestProcessor;
import org.aopalliance.aop.Advice;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by dimonn12 on 17.10.2015.
 */
@Component
public class AdviceFactoryImpl implements AdviceFactory {

    @Inject
    private RequestProcessor requestProcessor;

    @Override
    public Advice getRestResponseAdvice(Object bean, Class<?> targetClass) {
        return new RestResponseMethodInterceptor(requestProcessor, targetClass);
    }
}
