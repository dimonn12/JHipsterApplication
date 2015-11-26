package com.jhipster.application.web.rest;

import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import com.jhipster.application.web.rest.processor.RequestProcessor;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by Dmitry_Shanko on 6/5/2015.
 */
public class AbstractController<E extends BaseEntity<E, ID>, D extends BaseEntityDTO<E, ID>, ID extends Serializable> {

    private Logger childLogger;
    protected String entityName;

    @Inject
    private RequestProcessor requestProcessor;

    protected AbstractController(Logger childLogger, String entityName) {
        Assert.notNull(entityName);
        this.childLogger = childLogger;
        this.entityName = entityName;
    }

    protected Logger getLogger() {
        return this.childLogger;
    }

    protected void addError(ErrorStatusCode errorStatusCode) {
        requestProcessor.addError(errorStatusCode);
    }

}
