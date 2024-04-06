package com.example.youtubeupload.Payloads;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiResponse
public class ErrorAPIResponse {
    private String expectationName;
    private String errorMessage;
}
