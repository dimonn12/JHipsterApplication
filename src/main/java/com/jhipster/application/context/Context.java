package com.jhipster.application.context;

import com.jhipster.application.context.status.Status;
import com.jhipster.application.context.status.StatusType;

import java.util.Collection;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public interface Context {

    public void addStatus(Status statusToAdd);

    public Collection<Status> getAllStatuses();

    public int clearStatuses();

    public boolean containsStatusByType(StatusType statusType);

}
