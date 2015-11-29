package com.jhipster.application.web.rest.dto.errors;

import com.jhipster.application.context.status.Status;
import com.jhipster.application.web.rest.dto.BaseDTO;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public class ErrorStatusDTO extends BaseDTO {

    private final String message;
    private final int code;

    public ErrorStatusDTO(Status status) {
        this.message = status.getMessage();
        this.code = status.getCode();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorStatusDTO{" +
               "code=" + code +
               ", message='" + message + '\'' +
               '}';
    }


}
