package com.jhipster.application.service.security;

import com.jhipster.application.context.ContextHolder;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.security.Authority;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.repository.search.UserSearchRepository;
import com.jhipster.application.repository.security.AuthorityRepository;
import com.jhipster.application.repository.security.PersistentTokenRepository;
import com.jhipster.application.repository.security.UserRepository;
import com.jhipster.application.security.SecurityUtils;
import com.jhipster.application.service.AbstractService;
import com.jhipster.application.service.util.RandomUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService extends AbstractService<UserRepository, User, Long> {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private ContextHolder contextHolder;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        getRepository().findOneByActivationKey(key).map(user -> {
            // activate given user for the registration key.
            user.setActivated(true);
            user.setActivationKey(null);
            getRepository().save(user);
            userSearchRepository.save(user);
            log.debug("Activated user: {}", user);
            return user;
        }).orElseGet(() -> {

            return null;
        });
        return Optional.empty();
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findOneByResetKey(key).filter(user -> {
            DateTime oneDayAgo = DateTime.now().minusHours(24);
            return user.getResetDate().isAfter(oneDayAgo.toInstant().getMillis());
        }).map(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetKey(null);
            user.setResetDate(null);
            userRepository.save(user);
            return user;
        });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail).filter(user -> user.getActivated() == true).map(user -> {
            user.setResetKey(RandomUtil.generateResetKey());
            user.setResetDate(DateTime.now());
            userRepository.save(user);
            return user;
        });
    }

    public User createUserInformation(String login,
                                      String password,
                                      String firstName,
                                      String lastName,
                                      String email,
                                      String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            userRepository.save(u);
            userSearchRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void changePassword(String password) {
        User user = getRepository().findOneByLogin(SecurityUtils.getCurrentLogin());
        if(null != user) {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            getRepository().save(user);
            log.debug("Changed password for User: {}", user);
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN);
        }
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthoritiesByLogin(String login) {
        User user = getRepository().findOneByLogin(login);
        if(null != user) {
            user.getAuthorities().size();// eagerly load the association
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = getRepository().findOne(id);
        if(null != user) {
            user.getAuthorities().size();// eagerly load the association
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_ID);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        return getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentLogin());
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = new LocalDate();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        DateTime now = new DateTime();
        List<User> users = getRepository().findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for(User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            getRepository().delete(user);
            userSearchRepository.delete(user);
        }
    }

    @Transactional(readOnly = true)
    public User findOneByLoginOrEmail(String login) {
        User user = getRepository().findOneByLogin(login);
        if(null == user) {
            user = getRepository().findOneByEmail(login);
        }
        return user;
    }

}
