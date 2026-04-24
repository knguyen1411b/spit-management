package com.spring.backend.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@Schema(description = "Pagination metadata for the response")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableMetaResponse {
  @Schema(description = "Current page number", example = "0")
  int page;

  @Schema(description = "Size of the page", example = "20")
  int size;

  @Schema(description = "Total number of elements", example = "100")
  long total;

  @Schema(description = "Total number of pages", example = "5")
  long pages;

  @Schema(description = "Sort order applied to the results", example = "name,asc")
  String sort;

  /**
   * Builds a {@link PageableMetaResponse} from a Spring Data {@link Page} object.
   *
   * @param page the {@link Page} containing pagination details
   * @return a {@code PageableMetaResponse} populated with the page number, size, total elements,
   *     total pages, and sort order
   */
  public static PageableMetaResponse from(Page<?> page) {
    return PageableMetaResponse.builder()
        .page(page.getNumber())
        .size(page.getSize())
        .total(page.getTotalElements())
        .pages(page.getTotalPages())
        .sort(page.getSort().toString())
        .build();
  }
}
