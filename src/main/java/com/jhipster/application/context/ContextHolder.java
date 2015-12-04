package com.jhipster.application.context;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public interface ContextHolder {

    Context getCurrentContext();

    void setContext(Context context);

    boolean removeContext(Context context);

    void removeCurrentContext();

}
