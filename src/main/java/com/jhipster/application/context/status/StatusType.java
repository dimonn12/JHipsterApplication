package com.jhipster.application.context.status;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public enum StatusType {

    INFO(1), WARNING(2), ERROR(3), UNKNOWN(0);

    private final int type;

    private StatusType(int type) {
        this.type = type;
    }

}
