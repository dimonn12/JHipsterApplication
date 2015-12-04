package com.jhipster.application.web.rest.processor;

import com.jhipster.application.context.Context;
import com.jhipster.application.context.ContextHolder;
import com.jhipster.application.context.status.ErrorStatus;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.context.status.Status;
import com.jhipster.application.context.status.StatusType;
import com.jhipster.application.web.rest.dto.errors.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Created by dimonn12 on 16.10.2015.
 */
@Component
public class ErrorProcessorImpl implements ErrorProcessor {

    @Inject
    private ContextHolder contextHolder;

    public Context getCurrentContext() {
        return contextHolder.getCurrentContext();
    }

    @Override
    public HttpStatus getErrorHttpStatus(Collection<Status> statuses) {
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

    @Override
    public boolean hasErrors() {
        return getCurrentContext().containsError();
    }

    @Override
    public void addError(ErrorStatusCode errorStatusCode) {
        getCurrentContext().addStatus(new ErrorStatus(errorStatusCode));
    }

    @Override
    public ErrorDTO getErrorDTO(Collection<Status> statuses) {
        ErrorDTO error = new ErrorDTO();
        statuses.forEach(st -> error.addErrorStatus(st));
        return error;
    }

    @Override
    public ResponseEntity<?> processError() {
        Collection<Status> statuses = getCurrentContext().getAllStatuses();
        getCurrentContext().clearStatuses();
        return new ResponseEntity<>(getErrorDTO(statuses), getErrorHttpStatus(statuses));
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
