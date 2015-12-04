package com.jhipster.application.context;

import com.jhipster.application.context.status.Status;
import com.jhipster.application.context.status.StatusType;

import java.util.Collection;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public interface Context {

    void addStatus(Status statusToAdd);

    Collection<Status> getAllStatuses();

    int clearStatuses();

    boolean containsStatusByType(StatusType statusType);

    boolean containsError();

}
