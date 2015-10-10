package com.jhipster.application.web.rest.dto.security;

import com.jhipster.application.domain.security.Authority;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO extends BaseEntityDTO<User, Long> {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated;

    @Size(min = 2, max = 5)
    private String langKey;

    @Size(max = 20)
    private String activationKey;

    @Size(max = 20)
    private String resetKey;

    private boolean isLocked;

    private Set<String> authorities;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getLogin(),
            null,
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getActivated(),
            user.getLangKey(),
            user.getActivationKey(),
            user.getResetKey(),
            user.isLocked(),
            user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
    }

    public UserDTO(String login,
                   String password,
                   String firstName,
                   String lastName,
                   String email,
                   boolean activated,
                   String langKey,
                   String activationKey,
                   String resetKey,
                   boolean isLocked,
                   Set<String> authorities) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.activationKey = activationKey;
        this.resetKey = resetKey;
        this.isLocked = isLocked;
        this.authorities = authorities;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
               "login='" + login + '\'' +
               ", password='" + password + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", activated=" + activated +
               ", langKey='" + langKey + '\'' +
               ", authorities=" + authorities +
               '}';
    }
}
