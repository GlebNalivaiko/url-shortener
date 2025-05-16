package com.innowise.urlshortener.controller;

import com.innowise.urlshortener.service.UrlService;
import com.innowise.urlshortener.validator.annotation.Base62Encoded;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UrlRedirectController {

  private final UrlService urlService;

  @GetMapping("{shortCode}")
  public ResponseEntity<Void> redirect(@PathVariable @Base62Encoded String shortCode) {
    var originalUrl = urlService.getOriginalUrl(shortCode);
    return ResponseEntity
        .status(HttpStatus.FOUND)
        .location(URI.create(originalUrl))
        .build();
  }
}
