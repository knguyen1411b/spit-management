package com.spring.backend.exception;

import com.spring.backend.common.constants.MessageConstants;
import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.BadRequestResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.swagger.InternalServerErrorApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  /**
   * Handles custom application exceptions that extend {@link AppException}. Returns a standardized
   * response with the appropriate status code and message.
   *
   * @param ex the custom application exception that was thrown
   * @return an {@link ApiResponse} containing the error details
   */
  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiResponse> handleAppException(AppException ex) {
    log.warn("Business exception: {}", ex.getMessage());
    return ResponseEntity.status(ex.getStatusCode()).body(ApiResponse.from(ex));
  }

  /**
   * Handles exceptions when method arguments have type mismatches. Returns a bad request response
   * with the error details.
   *
   * @param ex the type mismatch exception that was thrown
   * @return an {@link ApiResponse} indicating the type mismatch error
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<DataApiResponse<BadRequestResponse>> handleTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    log.warn("Type mismatch: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            DataApiResponse.badRequest(
                BadRequestResponse.from(ex), MessageConstants.BAD_REQUEST.getMessage()));
  }

  /**
   * Handles validation exceptions that occur when method arguments fail validation. Returns a bad
   * request response with the validation error details.
   *
   * @param ex the validation exception that was thrown
   * @return an {@link ApiResponse} indicating the validation error
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<DataApiResponse<BadRequestResponse>> handleValidationException(
      MethodArgumentNotValidException ex) {
    log.warn("Validation failed: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            DataApiResponse.badRequest(
                BadRequestResponse.from(ex), MessageConstants.BAD_REQUEST.getMessage()));
  }

  /**
   * Handles exceptions when a requested resource is not found. Returns a not found response with
   * the error details.
   *
   * @param ex the no resource found exception that was thrown
   * @return an {@link ApiResponse} indicating the resource was not found
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
    log.warn("Resource not found: {}", ex.getResourcePath());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.notFoundResource(MessageConstants.NOT_FOUND.getMessage()));
  }

  /**
   * Handles exceptions when authentication is insufficient or missing. Returns an unauthorized
   * response with the error details.
   *
   * @param ex the insufficient authentication exception that was thrown
   * @return an {@link ApiResponse} indicating unauthorized access
   */
  @ExceptionHandler({InsufficientAuthenticationException.class, AuthenticationException.class})
  public ResponseEntity<ApiResponse> handleInsufficientAuthenticationException(
      AuthenticationException ex) {
    log.warn("Unauthorized access: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ApiResponse.unauthorized(MessageConstants.UNAUTHORIZED.getMessage()));
  }

  /**
   * Handles exceptions when authorization is denied. Returns a forbidden response with the error
   * details.
   *
   * @param ex the authorization denied exception that was thrown
   * @return an {@link ApiResponse} indicating forbidden access
   */
  @ExceptionHandler({AuthorizationDeniedException.class, AccessDeniedException.class})
  public ResponseEntity<ApiResponse> handleAuthorizationDeniedException(Exception ex) {
    log.warn("Forbidden access: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ApiResponse.forbidden(MessageConstants.FORBIDDEN.getMessage()));
  }

  /**
   * Handles all other exceptions that do not extend {@link AppException}. Logs the error and
   * returns a generic internal server error response.
   *
   * @param ex the exception that was thrown
   * @return an {@link ApiResponse} indicating an internal server error
   */
  @InternalServerErrorApiResponse
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> handleException(Exception ex) {
    log.error("Unexpected system error", ex);
    return ResponseEntity.internalServerError()
        .body(ApiResponse.internalServerError(MessageConstants.INTERNAL_SERVER_ERROR.getMessage()));
  }
}
