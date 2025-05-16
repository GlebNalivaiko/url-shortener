package com.innowise.urlshortener.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Utility class for working with short URLs.
 * <p>
 * Provides helper methods for constructing full URLs based on the current server context and a short code.
 */
@UtilityClass
public class ShortUrlUtils {

  private static final String URL_DELIMITER = "/";

  /**
   * Builds a full short URL using the current server context and the given short code.
   * <p>
   * Example: {@code https://example.com/abc123}
   *
   * @param shortCode short code representing the shortened URL
   * @return full short URL as a string
   */
  public static String buildShortUrl(String shortCode) {
    var baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    return String.join(URL_DELIMITER, baseUrl, shortCode);
  }
}
