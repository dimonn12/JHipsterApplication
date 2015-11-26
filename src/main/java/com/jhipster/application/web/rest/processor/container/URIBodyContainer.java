package com.jhipster.application.web.rest.processor.container;

import org.springframework.http.HttpHeaders;

import java.net.URI;

/**
 * Created by dimonn12 on 17.10.2015.
 */
public class URIBodyContainer extends HttpHeadersContainer {

    private final URI uri;

    public URIBodyContainer(URI uri, HttpHeaders headers, Object body) {
        super(headers, body);
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }
}
