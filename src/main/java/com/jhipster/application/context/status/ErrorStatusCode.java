package com.jhipster.application.context.status;

/**
 * Created by dimonn12 on 10.10.2015.
 */
public enum ErrorStatusCode {

    /**
     * Login errors: 4000000 - 4000099;
     * Validation errors: 4000100 - 4009999;
     * No Access: 4030000 - 4039999;
     * Not found: 4040000 - 4049999;
     * Method not supported: 4050000 - 4059999;
     * Conflicting errors: 4090000 - 4099999;
     * Internal Server Errors: 5000000+;
     */
    BAD_REQUEST(4000000, "Bad Request"),
    EMAIL_ALREADY_IN_USE(4000001, "E-mail address is already in use"),
    LOGIN_ALREADY_IN_USE(4000002, "Login is already in use"),
    INCORRECT_PASSWORD(4000003, "Incorrect password"),
    PASSWORD_IS_TOO_WEAK(4000003, "Password is too weak"),
    USER_IS_LOCKED(4000011, "User is locked"),
    USER_NOT_ACTIVATED(4000012, "User is not active"),
    VALIDATION_ERROR(4000100, "Validation error"),
    INVALID_ENTITY(4000101, "Entity is not valid"),
    ENTITY_ALREADY_HAS_AN_ID(4000102, "Entity already has an id"),
    VALUE_EMPTY(4000103, "Value can't be empty"),
    DATE_EMPTY(4000104, "Date can't be empty"),
    NAME_EMPTY(4000105, "Name can't be empty"),
    EXTERNAL_ID_EMPTY(4000106, "External Id can't be empty"),
    FORBIDDEN(4030000, "Access Denied. Forbidden"),
    ENTITY_NOT_FOUND(4040000, "Requested entity not found"),
    USER_NOT_FOUND_BY_LOGIN(4040001, "User not found by login"),
    USER_NOT_FOUND_BY_EMAIL(4040002, "User not found by email"),
    USER_NOT_FOUND_BY_LOGIN_OR_EMAIL(4040003, "User not found by login or email"),
    USER_NOT_FOUND_BY_ID(4040004, "User not found by User Id"),
    USER_NOT_FOUND_BY_ACTIVATION_KEY(4040005, "User not found by selected activation key"),
    USER_NOT_FOUND_BY_RESET_KEY_ID(4040006, "User not found by Reset Key Id"),
    RESET_DAY_IS_ELAPSED(4040007, "Reset Day is elapsed"),
    METHOD_NOT_SUPPORTED(4050000, "Method is not supported"),
    CONCURRENCY_CONFLICT(4090000, "Concurrency conflict"),
    ENTITY_EXISTS(4090001, "Such entity already exists"),
    ENTITY_EXISTS_WITH_NAME(4090002, "Entity with such name already exists"),
    INTERNAL_SERVER_ERROR(5000000, "Internal server error");

    private final String message;
    private final int code;

    private ErrorStatusCode(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ErrorStatusCode valueOf(int statusCode) {
        ErrorStatusCode[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ErrorStatusCode status = var1[var3];
            if(status.code == statusCode) {
                return status;
            }
        }

        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }

}
