package com.jhipster.application.repository.security;

import com.jhipster.application.domain.security.User;
import com.jhipster.application.repository.BaseEntityRepository;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends BaseEntityRepository<User, Long> {

    User findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(DateTime dateTime);

    User findOneByResetKey(String resetKey);

    User findOneByEmail(String email);

    User findOneByLogin(String login);

    @Override
    void delete(User t);

}
