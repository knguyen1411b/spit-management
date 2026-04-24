package com.spring.backend.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Schema(description = "API response containing a single data item")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataApiResponse<T> extends ApiResponse {

  @Schema(description = "Data returned by the API call")
  T data;

  /**
   * Creates a successful {@link DataApiResponse} with the given data and message.
   *
   * @param data The data to include in the response.
   * @param message A message describing the result of the API call.
   * @param <T> The type of the data being returned.
   * @return A {@link DataApiResponse} with success=true, the provided data, and message.
   */
  public static <T> DataApiResponse<T> success(T data, String message) {
    return DataApiResponse.<T>builder().success(true).message(message).data(data).build();
  }

  /**
   * Creates a failed {@link DataApiResponse} with the given data and message.
   *
   * @param data The data to include in the response, typically error details.
   * @param message A message describing the error.
   * @return A {@link DataApiResponse} with success=false, the provided data, and message.
   */
  public static DataApiResponse<BadRequestResponse> badRequest(
      BadRequestResponse data, String message) {
    return DataApiResponse.<BadRequestResponse>builder()
        .success(false)
        .message(message)
        .statusCode(400)
        .data(data)
        .build();
  }
}
