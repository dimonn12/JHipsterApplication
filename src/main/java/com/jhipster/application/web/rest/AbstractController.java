package com.jhipster.application.web.rest;

import com.jhipster.application.context.Context;
import com.jhipster.application.context.ContextHolder;
import com.jhipster.application.context.status.ErrorStatus;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.context.status.Status;
import com.jhipster.application.context.status.StatusType;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.web.rest.dto.BaseDTO;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import com.jhipster.application.web.rest.dto.errors.ErrorDTO;
import com.jhipster.application.web.rest.util.HeaderUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import javax.inject.Inject;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dmitry_Shanko on 6/5/2015.
 */
public class AbstractController<E extends BaseEntity<E, ID>, D extends BaseEntityDTO<E, ID>, ID extends Serializable> {

    private Logger childLogger;
    protected String entityName;

    @Inject
    private ContextHolder contextHolder;

    protected AbstractController(Logger childLogger, String entityName) {
        Assert.notNull(entityName);
        this.childLogger = childLogger;
        this.entityName = entityName;
    }

    protected Logger getLogger() {
        return this.childLogger;
    }

    protected Context getCurrentContext() {
        return contextHolder.getCurrentContext();
    }

    protected void addError(ErrorStatusCode errorStatusCode) {
        getCurrentContext().addStatus(new ErrorStatus(errorStatusCode));
    }

    protected boolean hasErrors() {
        return getCurrentContext().containsStatusByType(StatusType.ERROR);
    }

    protected ResponseEntity<? extends BaseDTO> processRequest() {
        if(hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    protected ResponseEntity<?> processRequest(E entity) {
        if(hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(entity, HttpStatus.OK);
        }
    }

    protected ResponseEntity<? extends BaseDTO> processRequest(D dto) {
        if(hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    }

    protected ResponseEntity<?> processRequest(List<?> objects) {
        if(hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(objects, HttpStatus.OK);
        }
    }

    protected ResponseEntity<? extends BaseDTO> processRequest(E entity, D dto) {
        return processRequest(null, entity, dto);
    }

    protected ResponseEntity<? extends BaseDTO> processRequest(URI forward, E entity, D dto) {
        if(hasErrors()) {
            return processError();
        } else {
            return processSaveRequest(forward, entity, dto);
        }
    }

    protected ResponseEntity<? extends BaseDTO> processSaveRequest(URI forward, E entity, D dto) {
        if(Objects.equals(entity.getId(), dto.getId())) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            if(null != forward) {
                return ResponseEntity.created(forward)
                    .headers(HeaderUtil.createEntityCreationAlert(StringUtils.lowerCase(entityName),
                        entity.getId().toString()))
                    .body(dto);
            } else {
                return new ResponseEntity<>(dto,
                    HeaderUtil.createEntityCreationAlert(StringUtils.lowerCase(entityName), entity.getId().toString()),
                    HttpStatus.CREATED);
            }
        }
    }

    protected ResponseEntity<?> processRequest(MultiValueMap<String, String> headers, Object body) {
        if(hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }
    }

    protected ResponseEntity<? extends BaseDTO> processForbidden() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    protected ResponseEntity<? extends BaseDTO> processError() {
        Collection<Status> statuses = getCurrentContext().getAllStatuses();
        return new ResponseEntity<>(getErrorDTO(statuses), getErrorHttpStatus(statuses));
    }

    private ErrorDTO getErrorDTO(Collection<Status> statuses) {
        ErrorDTO error = new ErrorDTO();
        statuses.forEach(st -> error.addErrorStatus(st));
        return error;
    }

    private HttpStatus getErrorHttpStatus(Collection<Status> statuses) {
        HttpStatus responseStatus = HttpStatus.OK;
        for(Status status : statuses) {
            HttpStatus currentResponseStatus = getHttpStatusForErrorCode(status.getCode());
            if(currentResponseStatus.value() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                responseStatus = currentResponseStatus;
                break;
            } else {
                if(null == responseStatus ||
                   HttpStatus.BAD_REQUEST == currentResponseStatus ||
                   ((HttpStatus.BAD_REQUEST != responseStatus) &&
                    (responseStatus.value() < currentResponseStatus.value()))) {
                    responseStatus = currentResponseStatus;
                }
            }
        }
        return responseStatus;
    }

    private HttpStatus getHttpStatusForErrorCode(int errorCode) {
        if(errorCode >= ErrorStatusCode.BAD_REQUEST.getCode() && errorCode < ErrorStatusCode.FORBIDDEN.getCode()) {
            return HttpStatus.BAD_REQUEST;
        } else {
            if(errorCode >= ErrorStatusCode.FORBIDDEN.getCode() &&
               errorCode < ErrorStatusCode.ENTITY_NOT_FOUND.getCode()) {
                return HttpStatus.FORBIDDEN;
            } else {
                if(errorCode >= ErrorStatusCode.ENTITY_NOT_FOUND.getCode() &&
                   errorCode < ErrorStatusCode.METHOD_NOT_SUPPORTED.getCode()) {
                    return HttpStatus.NOT_FOUND;
                } else {
                    if(errorCode >= ErrorStatusCode.METHOD_NOT_SUPPORTED.getCode() &&
                       errorCode < ErrorStatusCode.CONCURRENCY_CONFLICT.getCode()) {
                        return HttpStatus.METHOD_NOT_ALLOWED;
                    } else {
                        if(errorCode >= ErrorStatusCode.CONCURRENCY_CONFLICT.getCode() &&
                           errorCode < ErrorStatusCode.INTERNAL_SERVER_ERROR.getCode()) {
                            return HttpStatus.CONFLICT;
                        } else {
                            if(errorCode >= ErrorStatusCode.INTERNAL_SERVER_ERROR.getCode()) {
                                return HttpStatus.INTERNAL_SERVER_ERROR;
                            }
                        }
                    }
                }
            }
        }
        return HttpStatus.OK;
    }

}
