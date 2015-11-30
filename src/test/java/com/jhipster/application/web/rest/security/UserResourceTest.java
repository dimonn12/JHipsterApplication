package com.jhipster.application.web.rest.security;

import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.web.rest.AbstractControllerTest;
import com.jhipster.application.web.rest.dto.security.UserDTO;
import org.hamcrest.core.StringContains;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
public class UserResourceTest extends AbstractControllerTest<UserResource, User, UserDTO, Long> {

    private static final String URL = "/api/users";

    @Test
    public void testGetExistingUser() throws Exception {
        ResultActions ra = sendGet(URL, "admin");
        ra.andExpect(status().isOk()).andExpect(jsonPath("$.lastName").value("Administrator"));
        afterTestOccurred();
    }

    @Test
    public void testGetUnknownUser() throws Exception {
        ResultActions ra = sendGet(URL, "unknown");
        ra.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorStatuses[0].code").value(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN.getCode()));
        afterTestOccurred();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        ResultActions ra = sendGet(URL);
        ra.andExpect(status().isOk());
        afterTestOccurred();
    }

    @Test
    public void testCreateNewUser() throws Exception {
        UserDTO newUser = new UserDTO(null,
            "system_unit_test",
            "password",
            "first_name",
            "last_name",
            "email@email.ru",
            true,
            "en",
            "activation_key",
            "reset_key",
            false,
            new HashSet<>());
        ResultActions ra = sendPost(URL, newUser);
        String header = ra.andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("X-jHipsterApplicationApp-alert");

        ra = sendGet(URL, "system_unit_test");
        ra.andExpect(status().isOk());
        JSONObject json = new JSONObject(ra.andReturn().getResponse().getContentAsString());
        Assert.assertEquals(json.get("lastName").toString(), newUser.getLastName());
        Assert.assertNotNull(json.get("id"));

    }

    @Test
    public void testSimplePagination() throws Exception {
        ResultActions ra = sendGet("/api/users?page=0&size=1");
        ra.andExpect(status().isOk())
            .andExpect(header().string("Link",
                StringContains.containsString("</api/users?page=1&size=1>; rel=\"next\"")))
            .andExpect(header().string("Link",
                StringContains.containsString("</api/users?page=0&size=1>; rel=\"first\"")));
        JSONArray json = new JSONArray(ra.andReturn().getResponse().getContentAsString());
        Assert.assertEquals(json.length(), 1);
        afterTestOccurred();
    }
}
