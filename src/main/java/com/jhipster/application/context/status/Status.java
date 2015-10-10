package com.jhipster.application.context.status;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public interface Status {

    public StatusType getStatusType();

    public int getCode();

    public String getMessage();

}
