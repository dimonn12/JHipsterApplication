package com.jhipster.application.service;

import com.jhipster.application.context.Context;
import com.jhipster.application.context.ContextHolder;
import com.jhipster.application.context.status.ErrorStatus;
import com.jhipster.application.context.status.ErrorStatusCode;

import javax.inject.Inject;

/**
 * Created by dimonn12 on 13.10.2015.
 */
public abstract class AbstractService {

    @Inject
    private ContextHolder contextHolder;

    protected Context getCurrentContext() {
        return contextHolder.getCurrentContext();
    }

    protected void addError(ErrorStatusCode errorStatusCode) {
        getCurrentContext().addStatus(new ErrorStatus(errorStatusCode));
    }

}
