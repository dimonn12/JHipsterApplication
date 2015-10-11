package com.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.repository.BaseEntityRepository;
import com.jhipster.application.service.AbstractService;
import com.jhipster.application.web.rest.dto.BaseDTO;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by user on 31.05.2015.
 */
public abstract class AbstractResourceController<S extends AbstractService<R, E, ID>, R extends BaseEntityRepository<E, ID>, E extends BaseEntity<E, ID>, D extends BaseEntityDTO<E, ID>, ID extends Serializable>
    extends AbstractController<E, D, ID> {

    protected static final String REST_REQUEST = "REST request ";

    @Inject
    private S service;

    protected AbstractResourceController(Logger childLogger, String entityName) {
        super(childLogger, entityName);
    }

    protected S getService() {
        return this.service;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<? extends BaseDTO> updateById(@PathVariable ID id, @RequestBody D dto) {
        getLogger().debug(REST_REQUEST.concat(" to update by Id ").concat(this.entityName).concat(" id={} : {}"),
            id,
            dto);
        dto.setId(id);
        E entity = saveDTO(dto);
        if (null != entity) {
            return processRequest(entity, dto);
        } else {
            return processRequest(dto);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<? extends BaseDTO> get(@PathVariable ID id) {
        getLogger().debug(REST_REQUEST.concat(" to get ").concat(this.entityName).concat(" : {}"), id);
        E entity = getById(id);
        if (null != entity) {
            D dto = getDTO(entity);
            if (null != dto) {
                return processRequest(dto);
            }
        }
        addError(ErrorStatusCode.ENTITY_NOT_FOUND);
        return processRequest();
    }

    protected E saveDTO(D dto) {
        if(validate(dto)) {
            E entity = getEntity(dto);
            return service.save(entity);
        }
        return null;
    }

    protected boolean validate(D dto) {
        return true;
    }

    protected abstract E getById(ID id);

    protected abstract D getDTO(E entity);

    protected abstract E getEntity(D dto);

}
