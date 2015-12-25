package com.jhipster.application.web.rest.dto;

import org.springframework.http.HttpStatus;

/**
 * Created by dimonn12 on 05.12.2015.
 */
public class EmptyResponse {

    private static final EmptyResponse OK_RESPONSE = new EmptyResponse(HttpStatus.OK);
    private static final EmptyResponse NO_CONTENT_RESPONSE = new EmptyResponse(HttpStatus.NO_CONTENT);

    private final String content = "";
    private final HttpStatus status;

    private EmptyResponse(HttpStatus status) {
        this.status = status;
    }

    public static EmptyResponse noContent() {
        return NO_CONTENT_RESPONSE;
    }

    public static EmptyResponse ok() {
        return OK_RESPONSE;
    }

    public static EmptyResponse nullable() {
        return NO_CONTENT_RESPONSE;
    }

    public String getContent() {
        return content;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "EmptyResponse{status='" + status.toString() + "'}";
    }
}
