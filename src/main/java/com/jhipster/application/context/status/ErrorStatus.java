package com.jhipster.application.context.status;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public class ErrorStatus implements Status {

    private final String message;
    private final int code;

    public ErrorStatus(ErrorStatusCode errorStatusCode) {
        this(errorStatusCode.getMessage(), errorStatusCode.getCode());
    }

    private ErrorStatus(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public StatusType getStatusType() {
        return StatusType.ERROR;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(code);
    }

    @Override
    public boolean equals(Object obj) {
        if(null != obj && obj instanceof ErrorStatus) {
            return this.code == ((ErrorStatus)obj).code;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ErrorStatus{" +
               "code=" + code +
               ", message='" + message + '\'' +
               '}';
    }
}
