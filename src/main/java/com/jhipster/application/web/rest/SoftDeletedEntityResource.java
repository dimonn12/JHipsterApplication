package com.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.repository.BaseEntityRepository;
import com.jhipster.application.service.BaseEntityService;
import com.jhipster.application.web.rest.dto.BaseDTO;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class SoftDeletedEntityResource<S extends BaseEntityService<R, E, D, ID>, R extends BaseEntityRepository<E, ID>, E extends BaseEntity<E, ID>, D extends BaseEntityDTO<E, ID>, ID extends Serializable>
    extends AbstractResourceController<S, R, E, D, ID> {

    protected SoftDeletedEntityResource(Logger childLogger, String entityName) {
        super(childLogger, entityName);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<? extends BaseDTO> create(@RequestBody D dto) {
        getLogger().debug(REST_REQUEST.concat(" to save ").concat(this.entityName).concat(" : {}"), dto);
        if(null != dto.getId()) {
            addError(ErrorStatusCode.ENTITY_ALREADY_HAS_AN_ID);
        } else {
            E entity = saveDTO(dto);
            if(null != entity) {
                return processRequest(entity, dto);
            }
        }
        return processRequest();
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getAll() {
        getLogger().debug(REST_REQUEST.concat(" to get all ").concat(this.entityName).concat("s"));
        List<E> entityList = getService().findAll();
        List<BaseDTO> dtos = getDTOs(entityList);
        return processRequest(dtos);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<? extends BaseDTO> delete(@PathVariable ID id) {
        getLogger().debug(REST_REQUEST.concat(" to delete ").concat(this.entityName).concat(" : {}"), id);
        getService().delete(id);
        return processRequest();
    }

    @Override
    protected E getEntity(D dto) {
        return getService().prepareEntityFromDTO(dto);
    }

    protected List<BaseDTO> getDTOs(List<E> entities) {
        List<BaseDTO> result = new ArrayList<>();
        entities.forEach(entity -> result.add(getDTO(entity)));
        return result;
    }

}
