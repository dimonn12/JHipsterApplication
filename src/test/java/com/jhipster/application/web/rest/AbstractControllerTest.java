package com.jhipster.application.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhipster.application.Application;
import com.jhipster.application.annotation.AuthorityMethodText;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.security.AuthoritiesConstants;
import com.jhipster.application.web.rest.dto.BaseEntityDTO;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Created by dimonn12 on 28.11.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@EnableSpringDataWebSupport
@WebAppConfiguration
@IntegrationTest
public abstract class AbstractControllerTest<C extends AbstractController<E, D, ID>, E extends BaseEntity<E, ID>, D extends BaseEntityDTO<E, ID>, ID extends Serializable> {

    @Inject
    protected C abstractController;

    @Inject
    protected ObjectMapper objectMapper;

    protected MockMvc restUserMockMvc;

    @Before
    public void postSetup() {
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(abstractController)
            .setCustomArgumentResolvers(new HateoasPageableHandlerMethodArgumentResolver())
            .build();
        AuthorityMethodText.AuthorityMethod method = this.getClass()
            .getAnnotation(AuthorityMethodText.class)
            .authority();
        switch(method) {
            case ADMIN:
                authorizeAdmin();
                break;
            case USER:
            default:
                authorizeUser();
                break;
        }
    }

    protected ResultActions sendGet(String url, String id) throws Exception {
        return sendGet(url.concat("/").concat(id));
    }

    protected ResultActions sendGet(String url) throws Exception {
        return restUserMockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    protected ResultActions sendPost(String url, D dto) throws Exception {
        return restUserMockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    protected ResultActions sendPut(String url, String id) throws Exception {
        return restUserMockMvc.perform(put(url.concat("/").concat(id)).accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    protected ResultActions sendDelete(String url, String id) throws Exception {
        return restUserMockMvc.perform(delete(url.concat("/").concat(id)).accept(MediaType.APPLICATION_JSON));
    }

    protected void authorizeUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user", "user", authorities));
        SecurityContextHolder.setContext(securityContext);
    }

    protected void authorizeAdmin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user", "user", authorities));
        SecurityContextHolder.setContext(securityContext);
    }

}
