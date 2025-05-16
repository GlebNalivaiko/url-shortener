package com.innowise.urlshortener.exception.handler;

import com.innowise.urlshortener.dto.response.ExceptionResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ExceptionResponseDto entityNotFoundExceptionHandler(
      HttpServletRequest request,
      EntityNotFoundException exception
  ) {
    var requestUri = request.getRequestURI();
    log.debug("Entity not found: URI={}", requestUri, exception);
    return ExceptionResponseDto.builder()
        .timestamp(Instant.now())
        .code(HttpStatus.BAD_REQUEST.value())
        .message(exception.getMessage())
        .path(requestUri)
        .build();
  }
}
