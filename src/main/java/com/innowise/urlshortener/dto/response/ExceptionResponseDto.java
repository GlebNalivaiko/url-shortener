package com.innowise.urlshortener.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.Instant;
import java.util.Map;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"timestamp", "code", "status", "message", "additional"})
public record ExceptionResponseDto(

    Instant timestamp,

    int code,

    String status,

    String message,

    String path,

    Map<String, Object> additional
) {

}
