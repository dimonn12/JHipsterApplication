package com.jhipster.application.context;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public interface ContextStrategy {

    public void clearContext();

    public Context getContext();

    public void setContext(Context context);

    public Context createEmptyContext();
}
