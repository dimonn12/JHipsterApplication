package com.jhipster.application.web.rest.security;

import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.web.rest.AbstractControllerTest;
import com.jhipster.application.web.rest.dto.security.UserDTO;
import org.hamcrest.core.StringContains;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
public class UserResourceTest extends AbstractControllerTest<UserResource, User, UserDTO, Long> {

    @Test
    public void testGetExistingUser() throws Exception {
        ResultActions ra = sendGet("/api/users/admin");
        ra.andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName").value("Administrator"));
        afterTestOccurred();
    }

    @Test
    public void testGetUnknownUser() throws Exception {
        ResultActions ra = sendGet("/api/users/unknown");
        ra.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorStatuses[0].code").value(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN.getCode()));
        afterTestOccurred();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        ResultActions ra = sendGet("/api/users");
        ra.andExpect(status().isOk());
        afterTestOccurred();
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
