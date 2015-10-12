package com.jhipster.application.web.rest.security;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.security.AuthoritiesConstants;
import com.jhipster.application.service.security.AuthorityService;
import com.jhipster.application.service.security.UserService;
import com.jhipster.application.web.rest.AbstractController;
import com.jhipster.application.web.rest.dto.BaseDTO;
import com.jhipster.application.web.rest.dto.security.ManagedUserDTO;
import com.jhipster.application.web.rest.dto.security.UserDTO;
import com.jhipster.application.web.rest.util.HeaderUtil;
import com.jhipster.application.web.rest.util.PaginationUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing users.
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </p>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api/users")
public class UserResource extends AbstractController<User, UserDTO, Long> {

    @Inject
    private AuthorityService authorityService;

    @Inject
    private UserService userService;

    public UserResource() {
        super(LoggerFactory.getLogger(UserResource.class), "User");
    }

    /**
     * POST  /users -> Create a new user.
     */
    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<? extends BaseDTO> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        getLogger().debug("REST request to save User : {}", userDTO);
        if(null != userDTO.getId()) {
            addError(ErrorStatusCode.ENTITY_ALREADY_HAS_AN_ID);
        } else {
            User result = userService.addNewUser(getEntity(userDTO));
            if(null != result) {
                return processRequest(new URI("/api/users/" + result.getId()), result, userDTO);
            }
        }
        return processRequest();
    }

    /**
     * PUT  /users -> Updates an existing User.
     */
    @RequestMapping(
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> updateUser(@RequestBody ManagedUserDTO managedUserDTO) throws URISyntaxException {
        getLogger().debug("REST request to update User : {}", managedUserDTO);
        User result = userService.save(getEntity(managedUserDTO));
        if(null != result) {
            return processRequest(HeaderUtil.createEntityUpdateAlert("user", managedUserDTO.getLogin()),
                new ManagedUserDTO(result));
        }
        return processRequest();
    }

    /**
     * GET  /users -> get all users.
     */
    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllUsers(Pageable pageable) throws URISyntaxException {
        getLogger().debug("REST request to get all Users: {}", pageable);
        Page<User> page = userService.findAll(pageable);
        List<ManagedUserDTO> managedUserDTOs = page.getContent()
            .stream()
            .map(user -> new ManagedUserDTO(user))
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return processRequest(headers, managedUserDTOs);
    }

    /**
     * GET  /users/:login -> get user by login.
     */
    @RequestMapping(value = "/{login}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<? extends BaseDTO> getUser(@PathVariable String login) {
        getLogger().debug("REST request to get User : {}", login);
        User user = userService.getUserWithAuthoritiesByLogin(login);
        if(null != user) {
            return processRequest(new ManagedUserDTO(user));
        } else {
            addError(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN);
        }
        return processRequest();
    }

    /**
     * SEARCH  /_search/users/:query -> search for the User corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/{query}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> search(@PathVariable String query) {
        getLogger().debug("REST request to search by query={}", query);
        return processRequest(userService.search(query));
    }

    protected User getById(Long id) {
        return userService.findById(id);
    }

    protected User getEntity(UserDTO dto) {
        User user = null;
        if(null != dto.getId()) {
            user = getById(dto.getId());
        }
        if(null == user) {
            user = new User();
            user.setLocked(dto.isLocked());
            user.setActivationKey(dto.getActivationKey());
            user.setPassword(dto.getPassword());
            user.setResetKey(dto.getResetKey());
        }
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setLangKey(dto.getLangKey());
        user.setLastName(dto.getLastName());
        user.setActivated(dto.isActivated());
        user.setAuthorities(dto.getAuthorities()
            .stream()
            .map(authorityService::getAuthorityByName)
            .collect(Collectors.toSet()));
        return user;
    }

}
