package com.jhipster.application.web.rest.dto.security;

import com.jhipster.application.domain.security.Authority;
import com.jhipster.application.domain.security.User;
import org.joda.time.DateTime;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserDTO extends UserDTO {

    private DateTime createdDate;
    private String createdBy;
    private DateTime lastModifiedDate;
    private String lastModifiedBy;

    public ManagedUserDTO() {
    }

    public ManagedUserDTO(User user) {
        this(user.getId(),
            user.getLogin(),
            null,
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getActivated(),
            user.getLangKey(),
            user.getActivationKey(),
            user.getResetKey(),
            user.isLocked(),
            user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()),
            user.getCreatedDate(),
            user.getCreatedBy(),
            user.getLastModifiedDate(),
            user.getLastModifiedBy());
    }

    public ManagedUserDTO(Long id,
                          String login,
                          String password,
                          String firstName,
                          String lastName,
                          String email,
                          boolean activated,
                          String langKey,
                          String activationKey,
                          String resetKey,
                          boolean isLocked,
                          Set<String> authorities,
                          DateTime createdDate,
                          String createdBy,
                          DateTime lastModifiedDate,
                          String lastModifiedBy) {
        super(id,
            login,
            password,
            firstName,
            lastName,
            email,
            activated,
            langKey,
            activationKey,
            resetKey,
            isLocked,
            authorities);
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.createdBy = createdBy;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "ManagedUserDTO{" +
               "login='" + getLogin() + '\'' +
               ", firstName='" + getFirstName() + '\'' +
               ", lastName='" + getLastName() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", activated=" + isActivated() +
               ", langKey='" + getLangKey() + '\'' +
               ", authorities=" + getAuthorities() +
               ", createdDate='" + createdDate + '\'' +
               ", createdBy='" + createdBy + '\'' +
               ", lastModifiedDate='" + lastModifiedDate + '\'' +
               ", lastModifiedBy='" + lastModifiedBy + '\'' +
               '}';
    }

}
