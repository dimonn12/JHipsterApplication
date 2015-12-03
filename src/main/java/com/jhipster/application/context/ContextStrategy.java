package com.jhipster.application.context;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public interface ContextStrategy {

    void clearContext();

    Context getContext();

    void setContext(Context context);

    Context createEmptyContext();
}
