package com.jhipster.application.service.security;

import com.jhipster.application.Application;
import com.jhipster.application.domain.security.Authority;
import com.jhipster.application.domain.security.PersistentToken;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.repository.security.AuthorityRepository;
import com.jhipster.application.repository.security.PersistentTokenRepository;
import com.jhipster.application.security.AuthoritiesConstants;
import com.jhipster.application.service.util.RandomUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class UserServiceTest {

    @Inject
    private UserService userService;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private PersistentTokenService persistentTokenService;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    private String testLogin = "system_unit_test";
    private String testEmail = "system_unit_test@email.com";

    private User newUser;

    @Before
    public void configure() {
        newUser = createUser();
    }

    @Test
    public void testRemoveOldPersistentTokens() {
        userService.removeOldPersistentTokens();
        User admin = userService.findOneByLogin("admin");
        int existingCount = persistentTokenService.findByUser(admin).size();
        generateUserToken(admin, "1111-1111", new LocalDate());
        LocalDate now = new LocalDate();
        generateUserToken(admin, "2222-2222", now.minusDays(32));
        assertThat(persistentTokenService.findByUser(admin)).hasSize(existingCount + 2);
        userService.removeOldPersistentTokens();
        assertThat(persistentTokenService.findByUser(admin)).hasSize(existingCount + 1);
    }
/*
    @Test
    public void assertThatUserMustExistToResetPassword() {
        Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeUser.isPresent()).isFalse();

        maybeUser = userService.requestPasswordReset("admin@localhost");
        assertThat(maybeUser.isPresent()).isTrue();

        assertThat(maybeUser.get().getEmail()).isEqualTo("admin@localhost");
        assertThat(maybeUser.get().getResetDate()).isNotNull();
        assertThat(maybeUser.get().getResetKey()).isNotNull();

    }

    @Test
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        User user = userService.createUserInformation("johndoe",
            "johndoe",
            "John",
            "Doe",
            "john.doe@localhost",
            "en-US");
        Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeUser.isPresent()).isFalse();
        userRepository.delete(user);
    }

    @Test
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {

        User user = userService.createUserInformation("johndoe",
            "johndoe",
            "John",
            "Doe",
            "john.doe@localhost",
            "en-US");

        DateTime daysAgo = DateTime.now().minusHours(25);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);

        userRepository.save(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());

        assertThat(maybeUser.isPresent()).isFalse();

        userRepository.delete(user);

    }

    @Test
    public void assertThatResetKeyMustBeValid() {

        User user = userService.createUserInformation("johndoe",
            "johndoe",
            "John",
            "Doe",
            "john.doe@localhost",
            "en-US");

        DateTime daysAgo = DateTime.now().minusHours(25);
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey("1234");

        userRepository.save(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());

        assertThat(maybeUser.isPresent()).isFalse();

        userRepository.delete(user);

    }

    @Test
    public void assertThatUserCanResetPassword() {

        User user = userService.createUserInformation("johndoe",
            "johndoe",
            "John",
            "Doe",
            "john.doe@localhost",
            "en-US");

        String oldPassword = user.getPassword();

        DateTime daysAgo = DateTime.now().minusHours(2);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);

        userRepository.save(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());

        assertThat(maybeUser.isPresent()).isTrue();
        assertThat(maybeUser.get().getResetDate()).isNull();
        assertThat(maybeUser.get().getResetKey()).isNull();
        assertThat(maybeUser.get().getPassword()).isNotEqualTo(oldPassword);

        userRepository.delete(user);

    }*/

    @Test
    public void testRemoveNotActivatedUsersByCreationDateBefore() {
        userService.removeNotActivatedUsers();
        DateTime now = new DateTime();
        User notActiveUser = createUser();
        notActiveUser.setActivated(false);
        userService.addNewUser(notActiveUser);
        userService.removeNotActivatedUsers(now.plusDays(1));
        List<User> allUsers = userService.findAll()
            .stream()
            .filter(user -> user.getActivated())
            .collect(Collectors.toList());
        assertThat(allUsers).isEmpty();
    }

    private void generateUserToken(User user, String tokenSeries, LocalDate localDate) {
        PersistentToken token = new PersistentToken();
        token.setSeries(tokenSeries);
        token.setUser(user);
        token.setTokenValue(tokenSeries + "-data");
        token.setTokenDate(localDate);
        token.setIpAddress("127.0.0.1");
        token.setUserAgent("Test agent");
        persistentTokenRepository.saveAndFlush(token);
    }

    private User createUser() {
        User newUser = new User();
        newUser.setPassword("password1!");
        newUser.setActivated(true);
        newUser.setEmail(testEmail);
        newUser.setLogin(testLogin);
        newUser.setActivationKey("123456");
        newUser.setFirstName("firstName");
        newUser.setLastName("lastName");
        newUser.setLocked(false);
        newUser.setLangKey("en");
        newUser.setResetKey("reset");

        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findOne(AuthoritiesConstants.USER));
        newUser.setAuthorities(authorities);
        return newUser;
    }
}
