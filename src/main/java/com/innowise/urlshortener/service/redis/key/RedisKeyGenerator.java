package com.innowise.urlshortener.service.redis.key;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisKeyGenerator {

  private static final String URL = "url";
  private static final String SHORT_CODE = "shortCode";

  @Value("${spring.application.name:url-shortener}")
  private final String prefix;

  /**
   * Redis key used to store the original URL by shortCode.
   *
   * @return {@code <prefix>:url:shortCode:<shortCode>}
   */
  public String getOriginalUrlByShortCodeKey(String shortCode) {
    return String.join(":", prefix, URL, SHORT_CODE, shortCode);
  }
}
