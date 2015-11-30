package com.jhipster.application.context;

import com.jhipster.application.context.status.Status;
import com.jhipster.application.context.status.StatusType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public class ContextImpl implements Context {

    private final Set<Status> statusList;
    private final Lock lock = new ReentrantLock();

    public ContextImpl() {
        statusList = new HashSet<>();
    }

    @Override
    public void addStatus(Status statusToAdd) {
        statusList.add(statusToAdd);
    }

    @Override
    public Collection<Status> getAllStatuses() {
        return new HashSet<>(statusList);
    }

    @Override
    public int clearStatuses() {
        try {
            lock.lock();
            int size = statusList.size();
            statusList.clear();
            return size;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsStatusByType(StatusType statusType) {
        if(null == statusType) {
            return false;
        }
        try {
            lock.lock();
            for(Status status : statusList) {
                if(status.getStatusType().equals(statusType)) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
