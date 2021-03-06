package com.jhipster.application.service.security;

import com.jhipster.application.domain.security.User;

/**
 * Created by dimonn12 on 11.10.2015.
 */
public interface UserCreationStrategy {

    User createNewUserWithDefaultAuthorities();

    User createNewUserWithAdminAuthorities();

    User createNewUserWithUserAuthorities();

}
