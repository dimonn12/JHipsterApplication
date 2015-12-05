package com.jhipster.application.service.transaction;

import com.jhipster.application.context.ContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

/**
 * Created by dimonn12 on 04.12.2015.
 */
public class TransactionManager_Impl extends JpaTransactionManager {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionManager_Impl.class);
    @Inject
    private ContextHolder contextHolder;

    public TransactionManager_Impl() {
        super();
    }

    public TransactionManager_Impl(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        if(canCommit()) {
            super.doCommit(status);
        } else {
            LOG.warn("Transaction can't be committed due to: {}", getErrorDebugInfo());
        }
    }

    protected boolean canCommit() {
        return !contextHolder.getCurrentContext().containsError();
    }

    private String getErrorDebugInfo() {
        return contextHolder.getCurrentContext().getAllStatuses().toString();
    }

}
