package com.jhipster.application.web.rest.processor;

import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.web.rest.dto.BaseDTO;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.List;

/**
 * Created by dimonn12 on 16.10.2015.
 */
public interface RequestProcessor {

    void addError(ErrorStatusCode errorStatusCode);

    ResponseEntity<?> processError();

    ResponseEntity<?> processRequest();

    ResponseEntity<?> processRequest(String value);

    ResponseEntity<?> processRequest(BaseEntity<?, ?> entity);

    ResponseEntity<?> processRequest(BaseDTO dto);

    ResponseEntity<?> processRequest(List<?> objects);

    ResponseEntity<?> processRequest(String entityName, BaseEntity<?, ?> entity, BaseEntityDTO<?, ?> dto);

    ResponseEntity<?> processRequest(String creationAlertMessage,
                                     URI forward,
                                     BaseEntity<?, ?> entity,
                                     BaseEntityDTO<?, ?> dto);

    ResponseEntity<?> processSaveRequest(String creationAlertMessage,
                                         URI forward,
                                         BaseEntity<?, ?> entity,
                                         BaseEntityDTO<?, ?> dto);

    ResponseEntity<?> processRequest(MultiValueMap<String, String> headers, Object body);

    ResponseEntity<?> processRequestCreated(HttpHeaders headers, URI forward, Object body);

    ResponseEntity<?> processForbidden();

}
