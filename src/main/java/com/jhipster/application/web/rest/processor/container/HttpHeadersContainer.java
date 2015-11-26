package com.jhipster.application.web.rest.processor.container;

import org.springframework.http.HttpHeaders;

import java.io.Serializable;

/**
 * Created by dimonn12 on 17.10.2015.
 */
public class HttpHeadersContainer implements Serializable {

    private final HttpHeaders headers;
    private final Object body;

    public HttpHeadersContainer(HttpHeaders headers, Object body) {
        this.headers = headers;
        this.body = body;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }

}
