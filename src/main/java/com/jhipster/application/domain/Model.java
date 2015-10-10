package com.jhipster.application.domain;

import java.io.Serializable;


public interface Model<ID extends Serializable> extends Serializable {

    public ID getId();

    public void setId(ID id);

}
