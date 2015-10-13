package com.jhipster.application.service.security;

import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.security.Authority;
import com.jhipster.application.repository.security.AuthorityRepository;
import com.jhipster.application.service.AbstractService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by dimonn12 on 11.10.2015.
 */
@Service
public class AuthorityService extends AbstractService {

    @Inject
    private AuthorityRepository authorityRepository;

    public Authority getAuthorityByName(String authority) {
        Authority authorityFromDatabase = authorityRepository.findOne(authority);
        if(null == authorityFromDatabase) {
            addError(ErrorStatusCode.AUTHORITY_NOT_FOUND);
        }
        return authorityFromDatabase;
    }

}
