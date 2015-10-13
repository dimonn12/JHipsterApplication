package com.jhipster.application.service;

import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.repository.BaseEntityRepository;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public abstract class BaseEntityService<R extends BaseEntityRepository<E, ID>, E extends BaseEntity<E, ID>, D extends BaseEntityDTO<E, ID>, ID extends Serializable>
    extends EntityService<R, E, ID> {

    public E save(D dto) {
        return this.save(prepareEntityFromDTO(dto));
    }

    public E prepareEntityFromDTO(D dto) {
        if(null == dto) {
            return null;
        }
        if(null == dto.getId()) {
            return null;
        }
        E entity = this.getRepository().findOne(dto.getId());
        if(null == entity) {
            dto.setId(null);
            return null;
        } else {
            return entity.copyEntity(entity);
        }
    }

}
