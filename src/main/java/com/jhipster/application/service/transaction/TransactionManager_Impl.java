package com.jhipster.application.service.transaction;

import com.jhipster.application.context.ContextHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

/**
 * Created by dimonn12 on 04.12.2015.
 */
public class TransactionManager_Impl extends JpaTransactionManager {

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
        if(canCommint()) {
            super.doCommit(status);
        }
    }

    protected boolean canCommint() {
        return !contextHolder.getCurrentContext().containsError();
    }

}
