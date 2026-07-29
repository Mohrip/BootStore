package com.io.CoreBackend.shared.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);


    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Object> handleBusinessRule(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }



    @ExceptionHandler({ BookNotFoundException.class, AuthorNotFoundException.class, CategoryNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ BookAlreadyExistsException.class, InsufficientStockException.class })
    public ResponseEntity<Object> handleBadRequest(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Database-level constraint failures are the client's fault (400), but their
     * messages contain raw SQL, table and constraint names — so log the detail
     * server-side and return something generic.
     */
    @ExceptionHandler({ ConstraintViolationException.class,
            DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrity(
            Exception ex, WebRequest request) {
        log.warn("Data integrity violation on {}", request.getDescription(false), ex);
        return handleExceptionInternal(ex, "Request violates a data constraint",
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        return handleExceptionInternal(ex, message, headers, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex, WebRequest request) {
        log.error("Unhandled exception on {}", request.getDescription(false), ex);
        return handleExceptionInternal(ex, "Something went wrong",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}