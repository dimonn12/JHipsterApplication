package com.jhipster.application.web.exception;

import com.jhipster.application.context.status.ErrorStatus;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.security.UserIsLockedException;
import com.jhipster.application.security.UserNotActivatedException;
import com.jhipster.application.web.rest.dto.errors.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionTranslator.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO processException(Exception e) {
        LOG.error("Exception caught: ", e);
        return new ErrorDTO(new ErrorStatus(ErrorStatusCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDTO processConcurrencyError(ConcurrencyFailureException ex) {
        LOG.error("Concurrency Failure Exception. Will return Conflict 409: ", ex);
        return new ErrorDTO(new ErrorStatus(ErrorStatusCode.CONCURRENCY_CONFLICT));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO processAccessDeniedException(AccessDeniedException e) {
        LOG.error("Access Denied: {}", e.getMessage());
        return new ErrorDTO(new ErrorStatus(ErrorStatusCode.FORBIDDEN));
    }

    @ExceptionHandler(UserIsLockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO processUserIsLocked(UserIsLockedException e) {
        LOG.error("User is locked: ", e);
        return new ErrorDTO(new ErrorStatus(ErrorStatusCode.USER_IS_LOCKED));
    }

    @ExceptionHandler(UserNotActivatedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO processUserNotActivated(UserNotActivatedException e) {
        LOG.error("User is not active: ", e);
        return new ErrorDTO(new ErrorStatus(ErrorStatusCode.USER_NOT_ACTIVATED));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO processUserNotFound(UsernameNotFoundException e) {
        LOG.error("User not found by login: {}", e.getMessage());
        return new ErrorDTO(new ErrorStatus(ErrorStatusCode.USER_NOT_FOUND_BY_LOGIN));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorDTO processMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        LOG.error("HttpRequest Method Not Supported: {}", exception.getMessage());
        return new ErrorDTO(new ErrorStatus(ErrorStatusCode.METHOD_NOT_SUPPORTED));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorDTO processMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        LOG.error("Http Media Type Not Supported: {}", ex.getMessage());
        return new ErrorDTO(new ErrorStatus(ErrorStatusCode.UNSUPPORTED_MEDIA_TYPE));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO processValidationError(MethodArgumentNotValidException ex) {
        LOG.error("Validation exception: {}", ex.getMessage());
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private ErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        ErrorDTO dto = new ErrorDTO(new ErrorStatus(ErrorStatusCode.VALIDATION_ERROR));
        for(FieldError fieldError : fieldErrors) {
            dto.addFieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode());
        }
        return dto;
    }
}
