package com.spring.backend.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Schema(description = "API response containing a paginated list of data items")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagedApiResponse<T> extends ApiResponse {
  @Schema(description = "Data returned by the API call")
  List<T> data;

  @Schema(description = "Pagination metadata")
  PageableMetaResponse meta;

  /**
   * Creates a successful {@link PagedApiResponse} containing paginated data and metadata.
   *
   * @param page the {@link Page} object containing the data and pagination information
   * @param message a message describing the response
   * @param <T> the type of elements in the page
   * @return a {@link PagedApiResponse} instance representing a successful paginated response
   */
  public static <T> PagedApiResponse<T> success(Page<T> page, String message) {
    return PagedApiResponse.<T>builder()
        .success(true)
        .statusCode(HttpStatus.OK.value())
        .message(message)
        .data(page.getContent())
        .meta(PageableMetaResponse.from(page))
        .build();
  }
}
