package com.innowise.urlshortener.controller;

import com.innowise.urlshortener.dto.request.UrlShortenRequest;
import com.innowise.urlshortener.dto.response.UrlShortenResponse;
import com.innowise.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UrlController {

  private final UrlService urlService;

  @PostMapping("shorten")
  public ResponseEntity<UrlShortenResponse> shorten(@Validated @RequestBody UrlShortenRequest request) {
    var urlShortenResponse = urlService.shortenUrl(request.originalUrl());
    return new ResponseEntity<>(urlShortenResponse, HttpStatus.CREATED);
  }
}
