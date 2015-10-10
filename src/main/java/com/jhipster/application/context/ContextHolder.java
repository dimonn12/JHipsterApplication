package com.jhipster.application.context;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public interface ContextHolder {

    public Context getCurrentContext();

    public void setContext(Context context);

    public boolean removeContext(Context context);

    public void removeCurrentContext();

}
