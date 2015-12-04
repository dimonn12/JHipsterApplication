package com.jhipster.application.web.rest.processor;

import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.web.rest.dto.BaseDTO;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import com.jhipster.application.web.rest.util.HeaderUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.inject.Inject;
import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * Created by dimonn12 on 16.10.2015.
 */
@Component
public class RequestProcessorImpl implements RequestProcessor {

    @Inject
    private ErrorProcessor errorProcessor;

    @Override
    public void addError(ErrorStatusCode errorStatusCode) {
        errorProcessor.addError(errorStatusCode);
    }

    @Override
    public ResponseEntity<?> processError() {
        return errorProcessor.processError();
    }

    @Override
    public ResponseEntity<?> processRequest() {
        if(errorProcessor.hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<?> processRequest(String value) {
        if(errorProcessor.hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(value, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> processRequest(BaseEntity<?, ?> entity) {
        if(errorProcessor.hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(entity, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> processRequest(BaseDTO dto) {
        if(errorProcessor.hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> processRequest(List<?> objects) {
        if(errorProcessor.hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(objects, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> processRequest(String entityName, BaseEntity<?, ?> entity, BaseEntityDTO<?, ?> dto) {
        return processRequest(StringUtils.lowerCase(entityName), null, entity, dto);
    }

    @Override
    public ResponseEntity<?> processRequest(String creationAlertMessage,
                                            URI forward,
                                            BaseEntity<?, ?> entity,
                                            BaseEntityDTO<?, ?> dto) {
        return processSaveRequest(creationAlertMessage, forward, entity, dto);
    }

    @Override
    public ResponseEntity<?> processSaveRequest(String creationAlertMessage,
                                                URI forward,
                                                BaseEntity<?, ?> entity,
                                                BaseEntityDTO<?, ?> dto) {
        if(errorProcessor.hasErrors()) {
            return processError();
        }
        if(Objects.equals(entity.getId(), dto.getId())) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            if(null != forward) {
                return ResponseEntity.created(forward)
                    .headers(HeaderUtil.createEntityCreationAlert(creationAlertMessage, entity.getId().toString()))
                    .body(dto);
            } else {
                return new ResponseEntity<>(dto,
                    HeaderUtil.createEntityCreationAlert(creationAlertMessage, entity.getId().toString()),
                    HttpStatus.CREATED);
            }
        }
    }

    @Override
    public ResponseEntity<?> processRequest(MultiValueMap<String, String> headers, Object body) {
        if(errorProcessor.hasErrors()) {
            return processError();
        } else {
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> processRequestCreated(HttpHeaders headers, URI forward, Object body) {
        if(errorProcessor.hasErrors()) {
            return processError();
        }
        if(null != forward) {
            return ResponseEntity.created(forward).headers(headers).body(body);
        } else {
            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<?> processForbidden() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
