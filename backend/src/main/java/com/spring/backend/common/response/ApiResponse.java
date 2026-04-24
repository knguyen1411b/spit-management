package com.spring.backend.common.response;

import com.spring.backend.exception.AppException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true, builderMethodName = "apiBuilder")
@Schema(description = "Standard API response")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {
  @Schema(description = "Indicates whether the API call was successful", example = "true")
  boolean success;

  @Builder.Default
  @Schema(description = "HTTP status code of the response", example = "200")
  int statusCode = 200;

  @Schema(
      description = "Message describing the result of the API call",
      example = "Operation successful")
  String message;

  /**
   * Creates a successful API response with HTTP 200 (OK).
   *
   * @param message The success message.
   * @return An {@link ApiResponse} with success=true, statusCode=200, and the given message.
   */
  public static ApiResponse success(String message) {
    return ApiResponse.apiBuilder()
        .success(true)
        .statusCode(HttpStatus.OK.value())
        .message(message)
        .build();
  }

  /**
   * Creates a successful API response indicating that a resource was created (HTTP 201).
   *
   * @param message The success message.
   * @return An {@link ApiResponse} with success=true, statusCode=201, and the given message.
   */
  public static ApiResponse created(String message) {
    return ApiResponse.apiBuilder()
        .statusCode(HttpStatus.CREATED.value())
        .success(true)
        .message(message)
        .build();
  }

  /**
   * Creates a failed API response based on the provided {@link AppException}.
   *
   * @param ex The {@link AppException} containing error details.
   * @return An {@link ApiResponse} with success=false, statusCode and message from the exception.
   */
  public static ApiResponse from(AppException ex) {
    return ApiResponse.apiBuilder()
        .success(false)
        .statusCode(ex.getStatusCode().value())
        .message(ex.getMessage())
        .build();
  }

  /**
   * Creates an API response for an internal server error (HTTP 500).
   *
   * @param message The error message.
   * @return An {@link ApiResponse} with success=false, statusCode=500, and the given message.
   */
  public static ApiResponse internalServerError(String message) {
    return ApiResponse.apiBuilder()
        .success(false)
        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .message(message)
        .build();
  }

  /**
   * Creates an API response for a not found resource (HTTP 404).
   *
   * @param message The exception message.
   * @return An {@link ApiResponse} with success=false, statusCode=404, and the exception message.
   */
  public static ApiResponse notFoundResource(String message) {
    return ApiResponse.apiBuilder()
        .success(false)
        .statusCode(HttpStatus.NOT_FOUND.value())
        .message(message)
        .build();
  }

  /**
   * Creates an API response for a bad request (HTTP 400).
   *
   * @param message The error message.
   * @return An {@link ApiResponse} with success=false, statusCode=400, and the given message.
   */
  public static ApiResponse unauthorized(String message) {
    return ApiResponse.apiBuilder()
        .success(false)
        .statusCode(HttpStatus.UNAUTHORIZED.value())
        .message(message)
        .build();
  }

  /**
   * Creates an API response for forbidden access (HTTP 403).
   *
   * @param message The error message.
   * @return An {@link ApiResponse} with success=false, statusCode=403, and the given message.
   */
  public static ApiResponse forbidden(String message) {
    return ApiResponse.apiBuilder()
        .success(false)
        .statusCode(HttpStatus.FORBIDDEN.value())
        .message(message)
        .build();
  }
}
