package com.mgl.accountsservice.controllers;

import com.mgl.accountsservice.dto.ErrorResponse;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.exceptions.InvalidRequestException;
import com.mgl.accountsservice.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** Maps API failures to HTTP statuses without exposing internal exception messages. */
@Log4j2
@RestControllerAdvice
public class ApiExceptionHandler {

    /** Handles missing application resources. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> missing(ResourceNotFoundException error) {
        return failure(HttpStatus.NOT_FOUND, "NOT_FOUND", error.getMessage());
    }

    /** Handles explicit input validation failures. */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> invalid(InvalidRequestException error) {
        return failure(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", error.getMessage());
    }

    /** Handles malformed JSON and query parameter types. */
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> malformed(Exception error) {
        return failure(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Malformed request");
    }

    /** Handles unsupported request media types. */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> media(Exception error) {
        return failure(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE", "Use application/json");
    }

    /** Handles unsupported HTTP methods. */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> method(Exception error) {
        return failure(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "Unsupported HTTP method");
    }

    /** Handles unknown API paths. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> path(Exception error) {
        return failure(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found");
    }

    /** Handles storage failures; details remain in server logs. */
    @ExceptionHandler({DatabaseException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponse> database(Exception error) {
        log.error("Storage operation failed", error);
        return failure(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", "Storage operation failed");
    }

    /** Handles unexpected failures with a safe generic message. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpected(Exception error) {
        log.error("Unexpected API failure", error);
        return failure(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected server error");
    }

    private ResponseEntity<ErrorResponse> failure(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(false, code, message));
    }
}
