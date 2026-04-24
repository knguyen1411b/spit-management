package com.spring.backend;

import com.spring.backend.common.constants.MessageConstants;
import com.spring.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@EnableCaching
@SpringBootApplication
@Tag(name = "00. Default (V1)", description = "Controller for default API endpoint")
public class BackendApplication {

  @Operation(
      summary = "Default API Endpoint",
      description = "A simple endpoint to verify that the API is running.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class)))
      })
  @SecurityRequirements
  @GetMapping
  public ApiResponse index() {
    return ApiResponse.success(MessageConstants.CHECK_HEALTH.getMessage());
  }

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }
}
