package com.jhipster.application.web.rest.processor;

import com.jhipster.application.context.Context;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.context.status.Status;
import com.jhipster.application.web.rest.dto.errors.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

/**
 * Created by dimonn12 on 16.10.2015.
 */
public interface ErrorProcessor {

    Context getCurrentContext();

    HttpStatus getErrorHttpStatus(Collection<Status> statuses);

    boolean hasErrors();

    void addError(ErrorStatusCode errorStatusCode);

    ErrorDTO getErrorDTO(Collection<Status> statuses);

    ResponseEntity<?> processError();

}
