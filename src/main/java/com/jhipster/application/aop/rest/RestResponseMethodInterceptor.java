package com.jhipster.application.aop.rest;

import com.jhipster.application.aop.util.MethodKey;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.postprocessor.annotation.RestResponse;
import com.jhipster.application.web.rest.dto.BaseDTO;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import com.jhipster.application.web.rest.processor.RequestProcessor;
import com.jhipster.application.web.rest.processor.container.HttpHeadersContainer;
import com.jhipster.application.web.rest.processor.container.URIBodyContainer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dimonn12 on 17.10.2015.
 */
public class RestResponseMethodInterceptor implements MethodInterceptor, MethodCallback {

    private static final Logger LOG = LoggerFactory.getLogger(RestResponseMethodInterceptor.class);

    public static final MethodFilter METHOD_FILTER = new RestResponseMethodFilter();

    private final Class<?> targetClass;
    private final Map<MethodKey, RestResponse.ResponseReturnType> methodMap;

    private final RequestProcessor requestProcessor;

    public RestResponseMethodInterceptor(RequestProcessor requestProcessor, Class<?> targetClass) {
        this.requestProcessor = requestProcessor;
        this.targetClass = targetClass;
        this.methodMap = new ConcurrentHashMap<>();
        ReflectionUtils.doWithMethods(targetClass, this, METHOD_FILTER);
    }

    @Override
    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
        final RestResponse annotation = method.getAnnotation(RestResponse.class);
        if(annotation != null) {
            RestResponse.ResponseReturnType responseReturnType = annotation.type();
            final MethodKey methodKey = MethodKey.forMethod(method);
            this.methodMap.put(methodKey, responseReturnType);
            if(LOG.isDebugEnabled()) {
                LOG.debug("Created RestResponseMethodInterceptor {} for method: {}",
                    responseReturnType.name(),
                    methodKey);
            }
        }
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final MethodKey methodKey = MethodKey.forMethod(invocation.getMethod());
        RestResponse.ResponseReturnType annotationReturnType = methodMap.get(methodKey);
        try {
            return processInvocationResult(invocation.proceed(), annotationReturnType, invocation.getArguments());
        } catch(Exception e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Invocation ".concat(methodKey.toString()).concat(" caught exception: ").concat(e.getMessage()), e);
            }
            requestProcessor.addError(ErrorStatusCode.INTERNAL_SERVER_ERROR);
            return requestProcessor.processError();
        }
    }

    private ResponseEntity<?> processInvocationResult(Object result,  RestResponse.ResponseReturnType annotationReturnType, Object[] args) {
        switch(annotationReturnType) {
            case STRING:
                return requestProcessor.processRequest((String)result);
            case HTTP_HEADERS_CONTAINER:
                HttpHeadersContainer headersContainer = (HttpHeadersContainer)result;
                return requestProcessor.processRequest(headersContainer.getHeaders(),
                    headersContainer.getBody());
            case URI_CONTAINER:
                URIBodyContainer uriBodyContainer = (URIBodyContainer)result;
                return requestProcessor.processRequestCreated(uriBodyContainer.getHeaders(),
                    uriBodyContainer.getUri(),
                    uriBodyContainer.getBody());
            case LIST:
                return requestProcessor.processRequest((List<?>)result);
            case BASE_ENTITY:
                return requestProcessor.processRequest((BaseEntity<?, ?>)result);
            case SAVE_REQUEST:
                BaseEntityDTO<?, ?> dto = null;
                for(int i = 0; i < args.length && null != dto; i++) {
                    if(args[i] instanceof BaseEntityDTO<?, ?>) {
                        dto = (BaseEntityDTO<?, ?>)args[i];
                    }
                }
                return requestProcessor.processRequest(StringUtils.lowerCase(result.getClass().getName()), (BaseEntity<?, ?>)result, dto);
            case BASE_DTO:
                return requestProcessor.processRequest((BaseDTO)result);
            case DEFAULT:
            default:
                return requestProcessor.processRequest();
        }
    }

}
