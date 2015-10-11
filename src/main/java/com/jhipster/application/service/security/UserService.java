package com.jhipster.application.service.security;

import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.repository.search.UserSearchRepository;
import com.jhipster.application.repository.security.PersistentTokenRepository;
import com.jhipster.application.repository.security.UserRepository;
import com.jhipster.application.security.SecurityUtils;
import com.jhipster.application.service.AbstractService;
import com.jhipster.application.service.util.RandomUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

/**
 * Service class for managing users.
 */
@Service
public class UserService extends AbstractService<UserRepository, User, Long> {

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private UserCreationStrategy userCreationStrategy;

    @Transactional
    public User activateRegistration(String key) {
        logger.debug("Activating user for activation key {}", key);
        User user = getRepository().findOneByActivationKey(key);
        if(null != user) {
            if(!user.isLocked()) {
                user.setActivated(true);
                user.setActivationKey(null);
                user = saveUser(user);
                if(null != user) {
                    logger.debug("Activated user: {}", user);
                }
                return user;
            } else {
                addError(ErrorStatusCode.USER_IS_LOCKED);
            }
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_ACTIVATION_KEY);
        }
        return null;
    }

    @Transactional
    public User completePasswordReset(String newPassword, String key) {
        logger.debug("Reset user password for reset key {}", key);
        User user = getRepository().findOneByResetKey(key);
        if(null != user) {
            DateTime oneDayAgo = DateTime.now().minusHours(24);
            if(user.getResetDate().isAfter(oneDayAgo.toInstant().getMillis())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                user = saveUser(user);
                return user;
            } else {
                addError(ErrorStatusCode.RESET_DAY_IS_ELAPSED);
            }
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_RESET_KEY_ID);
        }
        return null;
    }

    @Transactional
    public User requestPasswordReset(String mail) {
        User user = findOneByEmail(mail);
        if(null != user) {
            if(user.getActivated()) {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(DateTime.now());
                user = saveUser(user);
                if(null != user) {
                    logger.debug("Password was reseted for {}", user);
                }
                return user;
            } else {
                addError(ErrorStatusCode.USER_NOT_ACTIVATED);
            }
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_EMAIL);
        }
        return null;
    }

    @Transactional
    public User createUserInformation(String login,
                                      String password,
                                      String firstName,
                                      String lastName,
                                      String email,
                                      String langKey) {
        User newUser = userCreationStrategy.createNewUserWithDefaultAuthorities();
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
        newUser.setLocked(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        if(validateUserToSave(newUser)) {
            newUser = saveUser(newUser);
            if(null != newUser) {
                logger.debug("Created Information for User: {}", newUser);
            }
            return newUser;
        }
        return null;
    }

    @Transactional
    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        User user = findOneByLogin(SecurityUtils.getCurrentLogin());
        if(null != user) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLangKey(langKey);
            user = saveUser(user);
            if(null != user) {
                logger.debug("Changed Information for User: {}", user);
            }
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN);
        }
    }

    @Override
    @Transactional
    public User save(User user) {
        logger.debug("Attempt from {} to save User: {}", SecurityUtils.getCurrentLogin(), user);
        User existingUser = findById(user.getId());
        if(null != existingUser) {
            if(validateUserToSave(existingUser)) {
                existingUser.setLogin(user.getLogin());
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());
                existingUser.setEmail(user.getEmail());
                existingUser.setActivated(user.getActivated());
                existingUser.setLangKey(user.getLangKey());
                return saveUser(existingUser);
            }
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_ID);
        }
        return null;
    }

    @Transactional
    public User addNewUser(User newUser) {
        if(validateUserToSave(newUser)) {
            return save(newUser);
        }
        return null;
    }

    @Transactional
    public void lockUser(User user) {
        logger.debug("Attempt from {} to lock User: {}", SecurityUtils.getCurrentLogin(), user);
        User existingUser = findById(user.getId());
        if(null != existingUser) {
            existingUser.setLocked(true);
            saveUser(user);
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_ID);
        }
    }

    @Transactional
    public void unlockUser(User user) {
        logger.debug("Attempt from {} to unlock User: {}", SecurityUtils.getCurrentLogin(), user);
        User existingUser = findById(user.getId());
        if(null != existingUser) {
            existingUser.setLocked(false);
            saveUser(user);
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_ID);
        }
    }

    @Transactional
    public void changePassword(String password) {
        User user = findOneByLogin(SecurityUtils.getCurrentLogin());
        if(null != user) {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            user = saveUser(user);
            if(null != saveUser(user)) {
                logger.debug("Changed password for User: {}", user);
            }
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN);
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

    @Transactional(readOnly = true)
    public User findOneByEmail(String login) {
        return getRepository().findOneByEmail(login);
    }

    @Transactional(readOnly = true)
    public User findOneByLogin(String login) {
        return getRepository().findOneByLogin(login);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthoritiesByLogin(String login) {
        User user = findOneByLogin(login);
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

    @Transactional(readOnly = true)
    public List<User> search(String query) {
        return StreamSupport.stream(userSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
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
    @Transactional
    public void removeOldPersistentTokens() {
        LocalDate now = new LocalDate();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            logger.debug("Deleting token {}", token.getSeries());
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
    @Transactional
    public void removeNotActivatedUsers() {
        DateTime now = new DateTime();
        List<User> users = getRepository().findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for(User user : users) {
            logger.debug("Deleting not activated user {}", user.getLogin());
            delete(user);
        }
    }

    private User saveUser(User user) {
        try {
            getRepository().save(user);
            userSearchRepository.save(user);
            return user;
        } catch(Exception e) {
            logger.error("Can't update {}", e, user);
            addError(ErrorStatusCode.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    private void delete(User user) {
        getRepository().delete(user);
        userSearchRepository.delete(user);
    }

    private boolean validateUserToSave(User userToSave) {
        boolean isValid = true;
        User userWithLogin = findOneByLogin(userToSave.getLogin());
        if(null != userWithLogin) {
            if(!Objects.equals(userToSave.getId(), userWithLogin.getId())) {
                addError(ErrorStatusCode.LOGIN_ALREADY_IN_USE);
                isValid = false;
            }
        }
        User userWithEmail = findOneByEmail(userToSave.getEmail());
        if(null != userWithEmail) {
            if(!Objects.equals(userToSave.getId(), userWithEmail.getId())) {
                addError(ErrorStatusCode.EMAIL_ALREADY_IN_USE);
                isValid = false;
            }
        }
        return isValid;
    }

}
