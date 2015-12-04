package com.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.postprocessor.annotation.RestResponse;
import com.jhipster.application.repository.BaseEntityRepository;
import com.jhipster.application.service.BaseEntityService;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import com.jhipster.application.web.rest.dto.EmptyResponse;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.util.List;


public abstract class SoftDeletedEntityResource<S extends BaseEntityService<R, E, D, ID>, R extends BaseEntityRepository<E, ID>, E extends BaseEntity<E, ID>, D extends BaseEntityDTO<E, ID>, ID extends Serializable>
    extends AbstractResourceController<S, R, E, D, ID> {

    protected SoftDeletedEntityResource(Logger childLogger, String entityName) {
        super(childLogger, entityName);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.BASE_DTO)
    public Object create(@RequestBody D dto) {
        getLogger().debug(REST_REQUEST.concat(" to save ").concat(this.entityName).concat(": {}"), dto);
        if(null != dto.getId()) {
            addError(ErrorStatusCode.ENTITY_ALREADY_HAS_AN_ID);
        } else {
            E entity = saveDTO(dto);
            if(null != entity) {
                return getDTO(entity);
            } else {
                addError(ErrorStatusCode.INTERNAL_SERVER_ERROR);
            }
        }
        return EmptyResponse.nullable();
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.LIST)
    public Object getAll() {
        getLogger().debug(REST_REQUEST.concat(" to get all ").concat(this.entityName).concat("s"));
        List<E> entityList = getService().findAll();
        return getDTOs(entityList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.DEFAULT)
    public Object delete(@PathVariable ID id) {
        getLogger().debug(REST_REQUEST.concat(" to delete by Id ").concat(this.entityName).concat(": id={}"), id);
        getService().delete(id);
        return EmptyResponse.noContent();
    }

    @Override
    protected E getEntity(D dto) {
        return getService().prepareEntityFromDTO(dto);
    }

}
