package com.jhipster.application.aop.rest;

import com.jhipster.application.aop.util.MethodKey;
import com.jhipster.application.postprocessor.annotation.RestResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dimonn12 on 17.10.2015.
 */
public class RestResponseMethodInterceptor implements MethodInterceptor, MethodCallback {

    private static final Logger LOG = LoggerFactory.getLogger(RestResponseMethodInterceptor.class);
    private static final MethodFilter methodFilter = new RestResponseMethodFilter();

    private final Class<?> targetClass;
    private final Map<MethodKey, RestResponse.ResponseReturnType> methodMap;

    public RestResponseMethodInterceptor(Class<?> targetClass) {
        this.targetClass = targetClass;
        this.methodMap = new ConcurrentHashMap<>();
        ReflectionUtils.doWithMethods(targetClass, this, methodFilter);
    }

    @Override
    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
        final RestResponse annotation = method.getAnnotation(RestResponse.class);
        if (annotation != null) {
            RestResponse.ResponseReturnType responseReturnType = annotation.type();
            final MethodKey methodKey = MethodKey.forMethod(method);
            this.methodMap.put(methodKey, responseReturnType);

            if (metric != null) {
                metrics.put(methodKey, new AnnotationMetricPair<A, M>(annotation, metric));

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Created {} {} for method {}", metric.getClass().getSimpleName(), metricName, methodKey);
                }
            }
        }
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();
    }

}
