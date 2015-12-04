package com.jhipster.application.web.rest.dto;

/**
 * Created by dimonn12 on 05.12.2015.
 */
public class EmptyResponse {

    private static final EmptyResponse RESPONSE = new EmptyResponse();

    private final String content = "";

    private EmptyResponse() {
    }

    public static EmptyResponse noContent() {
        return RESPONSE;
    }

    public static EmptyResponse ok() {
        return RESPONSE;
    }

    public static EmptyResponse nullable() {
        return RESPONSE;
    }

    @Override
    public String toString() {
        return "EmptyResponse{}";
    }
}
