package com.jhipster.application.web.rest.dto;


import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.domain.Model;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;


public abstract class BaseEntityDTO<E extends BaseEntity<E, ID> & Model<ID>, ID extends Serializable> extends BaseDTO
    implements Model<ID> {

    private ID id;

    public BaseEntityDTO() {
    }

    public BaseEntityDTO(E entity) {
        if(null != entity) {
            setId(entity.getId());
        }
    }

    public BaseEntityDTO(ID id) {
        setId(id);
    }

    @Override
    public ID getId() {
        return this.id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this);
    }

}
