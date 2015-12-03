package com.jhipster.application.context.status;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public interface Status {

    StatusType getStatusType();

    int getCode();

    String getMessage();

}
