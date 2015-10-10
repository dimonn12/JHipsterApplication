package com.jhipster.application.web.rest.dto.errors;

import com.jhipster.application.web.rest.dto.BaseDTO;

import java.io.Serializable;

/**
 * DTO for sending a parameterized error message.
 */
public class ParametrizedErrorDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;
    private final String message;
    private final String[] params;

    public ParametrizedErrorDTO(String message, String... params) {
        this.message = message;
        this.params = params;
    }

    public String getMessage() {
        return message;
    }

    public String[] getParams() {
        return params;
    }

}
