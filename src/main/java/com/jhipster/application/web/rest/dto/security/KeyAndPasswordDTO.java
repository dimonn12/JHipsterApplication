package com.jhipster.application.web.rest.dto.security;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class KeyAndPasswordDTO {

    @NotNull
    @Size(min = 1)
    private String key;

    @NotNull
    @Size(min = UserDTO.PASSWORD_MIN_LENGTH, max = UserDTO.PASSWORD_MAX_LENGTH)
    private String newPassword;

    public KeyAndPasswordDTO() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
