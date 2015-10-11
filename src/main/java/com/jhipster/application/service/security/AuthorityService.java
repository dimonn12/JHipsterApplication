package com.jhipster.application.service.security;

import com.jhipster.application.domain.security.Authority;
import com.jhipster.application.repository.security.AuthorityRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by dimonn12 on 11.10.2015.
 */
@Service
public class AuthorityService {

    @Inject
    private AuthorityRepository authorityRepository;

    public Authority getAuthorityByName(String authority) {
        return authorityRepository.findOne(authority);
    }

}
