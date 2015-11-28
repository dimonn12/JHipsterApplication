package com.jhipster.application.postprocessor;

import com.jhipster.application.aop.rest.AdviceFactory;
import com.jhipster.application.aop.rest.RestResponseMethodInterceptor;
import com.jhipster.application.postprocessor.annotation.RestResponse;
import com.jhipster.application.web.rest.AbstractController;
import com.jhipster.application.web.rest.processor.RequestProcessor;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.inject.Inject;


/**
 * Created by dimonn12 on 16.10.2015.
 */
@Component
public class RestControllerAnnotationBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(RestControllerAnnotationBeanPostProcessor.class);

    public static final Pointcut POINTCUT = new AnnotationMatchingPointcut(null, RestResponse.class);

    @Inject
    private AdviceFactory adviceFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof AbstractController) {
            final Class<?> targetClass = AopUtils.getTargetClass(bean);

            if(AopUtils.canApply(POINTCUT, targetClass)) {
                final Advice advice = adviceFactory.getRestResponseAdvice(bean, targetClass);
                final Advisor advisor = new DefaultPointcutAdvisor(POINTCUT, advice);

                if(bean instanceof Advised) {
                    LOG.info("Bean {} is already proxied, adding Advisor to existing proxy", beanName);
                    Advised advisedBean = (Advised)bean;
                    advisedBean.addAdvisor(advisor);
                    return bean;
                } else {
                    LOG.info("Proxying bean {} of type {}", beanName, targetClass.getCanonicalName());
                    final ProxyFactory proxyFactory = new ProxyFactory(bean);
                    proxyFactory.addAdvisor(advisor);

                    final Object proxy = proxyFactory.getProxy(this.getClass().getClassLoader());
                    return proxy;
                }
            }
        }

        return bean;
    }
        /*if(bean instanceof AbstractController) {
            final Class<?> targetClass = AopUtils.getTargetClass(bean);

            if(AopUtils.canApply(pointcut, targetClass)) {
                final Advice advice = adviceFactory.getRestResponseAdvice(bean, targetClass);
                final Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);

                if(bean instanceof Advised) {
                    LOG.debug("Bean {} is already proxied, adding Advisor to existing proxy", beanName);

                    ((Advised)bean).addAdvisor(0, advisor);

                    return bean;
                } else {
                    LOG.debug("Proxying bean {} of type {}", beanName, targetClass.getCanonicalName());

                    final ProxyFactory proxyFactory = new ProxyFactory(bean);
                    if(proxyConfig != null) {
                        proxyFactory.copyFrom(proxyConfig);
                    }
                    proxyFactory.addAdvisor(advisor);

                    final Object proxy = proxyFactory.getProxy(this.beanClassLoader);

                    return proxy;
                }
            }

            return bean;



            if(bean instanceof AbstractController) {
                final Class<?> targetClass = AopUtils.getTargetClass(bean);
                Method[] methods = targetClass.getMethods();
                if(null != methods) {
                    for(Method method : methods) {
                        RestResponse restResponseAnnotation = method.getAnnotation(RestResponse.class);
                        boolean doMethodWrap = false;
                        RestResponse.ResponseReturnType responseReturnType = RestResponse.ResponseReturnType.DEFAULT;
                        if(null != restResponseAnnotation) {
                            doMethodWrap = true;
                            responseReturnType = restResponseAnnotation.type();
                        }
                        if(doMethodWrap) {
                            final RestResponse.ResponseReturnType type = responseReturnType;
                            bean = Proxy.newProxyInstance(beanClass.getClassLoader(),
                                new Class[]{ beanClass },
                                (proxy, proxiedMethod, args) -> {
                                    Object result = proxiedMethod.invoke(proxy, args);
                                    if(null == result) {
                                        return requestProcessor.processRequest();
                                    }
                                    switch(type) {
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
                                            if(null == args) {
                                                return requestProcessor.processRequest();
                                            }
                                            BaseEntityDTO<?, ?> dto = null;
                                            for(int i = 0; i < args.length && null != dto; i++) {
                                                if(args[i] instanceof BaseEntityDTO<?, ?>) {
                                                    dto = (BaseEntityDTO<?, ?>)args[i];
                                                }
                                            }
                                            return requestProcessor.processRequest(StringUtils.lowerCase(result.getClass()
                                                .getName()), (BaseEntity<?, ?>)result, dto);
                                        case BASE_DTO:
                                            return requestProcessor.processRequest((BaseDTO)result);
                                        case DEFAULT:
                                        default:
                                            return requestProcessor.processRequest();
                                    }
                                });
                        }
                    }
                }
            }
            return bean;
        }*/


    }
