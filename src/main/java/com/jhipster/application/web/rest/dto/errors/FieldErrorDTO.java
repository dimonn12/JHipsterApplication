package com.jhipster.application.web.rest.dto.errors;

import com.jhipster.application.web.rest.dto.BaseDTO;

import java.io.Serializable;

public class FieldErrorDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private final String objectName;
    private final String field;
    private final String message;

    public FieldErrorDTO(String dto, String field, String message) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}
