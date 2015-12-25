package com.jhipster.application.web.rest.security;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.security.PersistentToken;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.postprocessor.annotation.RestResponse;
import com.jhipster.application.security.SecurityUtils;
import com.jhipster.application.service.mail.MailService;
import com.jhipster.application.service.security.PersistentTokenService;
import com.jhipster.application.service.security.UserService;
import com.jhipster.application.web.rest.AbstractController;
import com.jhipster.application.web.rest.dto.EmptyResponse;
import com.jhipster.application.web.rest.dto.security.KeyAndPasswordDTO;
import com.jhipster.application.web.rest.dto.security.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource extends AbstractController<User, UserDTO, Long> {

    @Inject
    private UserService userService;

    @Inject
    private PersistentTokenService persistentTokenService;

    @Inject
    private MailService mailService;

    public AccountResource() {
        super(LoggerFactory.getLogger(AccountResource.class), "Account");
    }

    /**
     * POST  /register -> register the user.
     */
    @RequestMapping(value = "/register",
                    method = RequestMethod.POST,
                    produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.BASE_DTO)
    public Object registerAccount(@RequestBody @Valid UserDTO userDTO, HttpServletRequest request) {
        getLogger().debug("REST request to register new Account: {}", userDTO);
        User user = userService.findOneByLogin(userDTO.getLogin());
        if(null != user) {
            addError(ErrorStatusCode.LOGIN_ALREADY_IN_USE);
            return null;
        }
        user = userService.findOneByEmail(userDTO.getEmail());
        if(null != user) {
            addError(ErrorStatusCode.EMAIL_ALREADY_IN_USE);
            return null;
        }
        user = userService.createUserInformation(userDTO.getLogin(),
            userDTO.getPassword(),
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail().toLowerCase(),
            userDTO.getLangKey());
        if(null != user) {
            String baseUrl = request.getScheme() + // "http"
                             "://" +                                // "://"
                             request.getServerName() +              // "myhost"
                             ":" +                                  // ":"
                             request.getServerPort();               // "80"
            mailService.sendActivationEmail(user, baseUrl);
        }
        return getDTO(user);
    }

    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/activate",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.BASE_DTO)
    public Object activateAccount(@RequestParam(value = "key") String key) {
        getLogger().debug("REST request to activate Account by key: key={}", key);
        return getDTO(userService.activateRegistration(key));
    }

    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.STRING)
    public Object isAuthenticated(HttpServletRequest request) {
        getLogger().debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.BASE_DTO)
    public Object getAccount() {
        getLogger().debug("REST request to get current Account: login={}", SecurityUtils.getCurrentLogin());
        User user = userService.getUserWithAuthorities();
        if(null == user) {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN);
        }
        return getDTO(user);
    }

    /**
     * POST  /account -> update the current user information.
     */
    @RequestMapping(value = "/account",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.BASE_DTO)
    public Object saveAccount(@RequestBody @Valid UserDTO userDTO) {
        getLogger().debug("REST request to save Account: {}", userDTO);
        User user = userService.findOneByLogin(userDTO.getLogin());
        if(null != user) {
            if(Objects.equals(user.getLogin(), SecurityUtils.getCurrentLogin())) {
                return getDTO(userService.updateUserInformation(userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getEmail(),
                    userDTO.getLangKey()));
            } else {
                addError(ErrorStatusCode.INVALID_ENTITY);
            }
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN);
        }
        return EmptyResponse.nullable();
    }

    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/change_password",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.DEFAULT)
    public Object changePassword(@RequestBody String password) {
        getLogger().debug("REST request to change password: login={}", SecurityUtils.getCurrentLogin());
        if(!checkPasswordLength(password)) {
            addError(ErrorStatusCode.PASSWORD_IS_TOO_WEAK);
        } else {
            userService.changePassword(password);
        }
        return EmptyResponse.noContent();
    }

    /**
     * GET  /account/sessions -> get the current open sessions.
     */
    @RequestMapping(value = "/account/sessions",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.LIST)
    public Object getCurrentSessions() {
        getLogger().debug("REST request to get current session: login={}", SecurityUtils.getCurrentLogin());
        User user = userService.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(null == user) {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN);
        }
        return persistentTokenService.findByUser(user);
    }

    /**
     * DELETE  /account/sessions?series={series} -> invalidate an existing session.
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     * still be able to use that session, until you quit your browser: it does not work in real time (there is
     * no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     * your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     * anymore.
     * There is an API to invalidate the current session, but there is no API to check which session uses which
     * cookie.
     */
    @RequestMapping(value = "/account/sessions/{series}",
                    method = RequestMethod.DELETE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.DEFAULT)
    public Object invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        getLogger().debug("REST request to invalidate session: login={}, series={}",
            SecurityUtils.getCurrentLogin(),
            series);
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        User user = userService.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(null != user) {
            Optional<PersistentToken> token = persistentTokenService.findByUser(user)
                .stream()
                .filter(persistentToken -> StringUtils.equals(persistentToken.getSeries(), decodedSeries))
                .findAny();
            if(!token.isPresent()) {
                addError(ErrorStatusCode.PERSISTENT_TOKEN_NOT_FOUND);
            } else {
                token.ifPresent(t -> persistentTokenService.delete(decodedSeries));
            }
        }
        return EmptyResponse.noContent();
    }

    @RequestMapping(value = "/account/reset_password/init",
                    method = RequestMethod.POST,
                    produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.DEFAULT)
    public Object requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
        getLogger().debug("REST request to request password reset by email: email={}", mail);
        User user = userService.requestPasswordReset(mail);
        if(null != user) {
            String baseUrl = request.getScheme() +
                             "://" +
                             request.getServerName() +
                             ":" +
                             request.getServerPort();
            mailService.sendPasswordResetMail(user, baseUrl);
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_EMAIL);
        }
        return EmptyResponse.noContent();
    }

    @RequestMapping(value = "/account/reset_password/finish",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RestResponse(type = RestResponse.ResponseReturnType.BASE_DTO)
    public Object finishPasswordReset(@RequestBody @Valid KeyAndPasswordDTO keyAndPassword) {
        getLogger().debug("REST request to finish password reset: login={}", SecurityUtils.getCurrentLogin());
        if(!checkPasswordLength(keyAndPassword.getNewPassword())) {
            addError(ErrorStatusCode.INCORRECT_PASSWORD);
        } else {
            return getDTO(userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey()));
        }
        return EmptyResponse.nullable();
    }

    protected UserDTO getDTO(User entity) {
        if(null == entity) {
            return null;
        }
        return new UserDTO(entity);
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
                password.length() >= UserDTO.PASSWORD_MIN_LENGTH &&
                password.length() <= UserDTO.PASSWORD_MAX_LENGTH);
    }

}
