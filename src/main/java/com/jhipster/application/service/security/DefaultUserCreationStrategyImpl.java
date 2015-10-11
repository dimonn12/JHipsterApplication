package com.jhipster.application.service.security;

import com.jhipster.application.config.Constants;
import com.jhipster.application.domain.security.Authority;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.repository.security.AuthorityRepository;
import com.jhipster.application.security.AuthoritiesConstants;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dimonn12 on 11.10.2015.
 */
@Service
public class DefaultUserCreationStrategyImpl implements UserCreationStrategy {

    @Inject
    private AuthorityRepository authorityRepository;

    @Override
    public User createNewUserWithDefaultAuthorities() {
        return createNewUserWithUserAuthorities();
    }

    @Override
    public User createNewUserWithAdminAuthorities() {
        User newUser = createNewUser();
        Set<Authority> authorities = new HashSet<>();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.ADMIN);
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        return newUser;
    }

    @Override
    public User createNewUserWithUserAuthorities() {
        User newUser = createNewUser();
        Set<Authority> authorities = new HashSet<>();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        return newUser;
    }

    private User createNewUser() {
        return new User();
    }

}
