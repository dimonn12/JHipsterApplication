package com.jhipster.application.web.rest.dto.errors;

import com.jhipster.application.context.status.ErrorStatus;
import com.jhipster.application.context.status.Status;
import com.jhipster.application.web.rest.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for transferring error message with a list of field errors.
 */
public class ErrorDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private List<ErrorStatusDTO> errorStatuses;
    private List<FieldErrorDTO> fieldErrors;

    public ErrorDTO() {
    }

    public ErrorDTO(ErrorStatus... errorStatuses) {
        if(null != errorStatuses) {
            for(ErrorStatus errorStatus : errorStatuses) {
                addErrorStatus(errorStatus);
            }
        }
    }

    public ErrorDTO(String objectName, String field, String message) {
        addFieldError(objectName, field, message);
    }

    public void addErrorStatus(Status errorStatus) {
        if(null == errorStatuses) {
            errorStatuses = new ArrayList<>();
        }
        errorStatuses.add(new ErrorStatusDTO(errorStatus));
    }

    public List<ErrorStatusDTO> getErrorStatuses() {
        return errorStatuses;
    }

    public void addFieldError(String objectName, String field, String message) {
        if(null == fieldErrors) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorDTO(objectName, field, message));
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
               "errorStatuses=" + errorStatuses +
               ", fieldErrors=" + fieldErrors +
               '}';
    }
}
