package com.jhipster.application.service.security;

import com.jhipster.application.Application;
import com.jhipster.application.domain.security.Authority;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.repository.security.AuthorityRepository;
import com.jhipster.application.security.AuthoritiesConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dimonn12 on 11.10.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserCreationStrategyTest {

    private UserCreationStrategy userCreationStrategy;

    @Inject
    private AuthorityRepository authorityRepository;

    @Before
    public void doSetup() {
        userCreationStrategy = new DefaultUserCreationStrategyImpl();
        ReflectionTestUtils.setField(userCreationStrategy, "authorityRepository", authorityRepository);
    }

    @Test
    public void testDefaultAuthorities() {
        User user = userCreationStrategy.createNewUserWithDefaultAuthorities();
        Authority userAuthority = authorityRepository.findOne(AuthoritiesConstants.USER);
        assertThat(user.getAuthorities().contains(userAuthority)).isTrue();
    }

    @Test
    public void testUserAuthorities() {
        User user = userCreationStrategy.createNewUserWithUserAuthorities();
        Authority userAuthority = authorityRepository.findOne(AuthoritiesConstants.USER);
        assertThat(user.getAuthorities().contains(userAuthority)).isTrue();
    }

    @Test
    public void testAdminAuthorities() {
        User user = userCreationStrategy.createNewUserWithAdminAuthorities();
        Authority userAuthority = authorityRepository.findOne(AuthoritiesConstants.USER);
        assertThat(user.getAuthorities().contains(userAuthority)).isFalse();

        Authority adminAuthority = authorityRepository.findOne(AuthoritiesConstants.ADMIN);
        assertThat(user.getAuthorities().contains(adminAuthority)).isTrue();
    }

}
