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

    public void addError(ErrorStatusCode errorStatusCode);

    public ResponseEntity<?> processError();

    public ResponseEntity<?> processRequest();

    public ResponseEntity<?> processRequest(String value);

    public ResponseEntity<?> processRequest(BaseEntity<?, ?> entity);

    public ResponseEntity<?> processRequest(BaseDTO dto);

    public ResponseEntity<?> processRequest(List<?> objects);

    public ResponseEntity<?> processRequest(String entityName, BaseEntity<?, ?> entity, BaseEntityDTO<?, ?> dto);

    public ResponseEntity<?> processRequest(String creationAlertMessage,
                                            URI forward,
                                            BaseEntity<?, ?> entity,
                                            BaseEntityDTO<?, ?> dto);

    public ResponseEntity<?> processSaveRequest(String creationAlertMessage,
                                                URI forward,
                                                BaseEntity<?, ?> entity,
                                                BaseEntityDTO<?, ?> dto);

    public ResponseEntity<?> processRequest(MultiValueMap<String, String> headers, Object body);

    public ResponseEntity<?> processRequestCreated(HttpHeaders headers, URI forward, Object body);

    public ResponseEntity<?> processForbidden();

}
