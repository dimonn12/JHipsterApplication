package com.jhipster.application.context;

import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by dimonn12 on 10.10.2015.
 */
@Component
public class BasicContextHolder implements ContextHolder {

    @Inject
    private ContextStrategy strategy;

    @Override
    public Context getCurrentContext() {
        return strategy.getContext();
    }

    @Override
    public void setContext(Context context) {
        strategy.setContext(context);
    }

    @Override
    public boolean removeContext(Context context) {
        return false;
    }

    @Override
    public void removeCurrentContext() {
        strategy.clearContext();
    }

}
