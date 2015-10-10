package com.jhipster.application.context;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by dimonn12 on 10.10.2015.
 */
@Component
public class ThreadLocalContextStrategy implements ContextStrategy {

    private static final ThreadLocal<Context> contextHolder = new ThreadLocal<Context>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public Context getContext() {
        Context context = contextHolder.get();
        if(null == context) {
            context = createEmptyContext();
            contextHolder.set(context);
        }
        return context;
    }

    @Override
    public void setContext(Context context) {
        Assert.notNull(context, "Only non-null Context instances are permitted");
        contextHolder.set(context);
    }

    @Override
    public Context createEmptyContext() {
        return new ContextImpl();
    }
}
