package com.innowise.urlshortener.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class ShortUrlUtilsTest {

  private static final String BASE_URL = "https://example.com";

  @BeforeEach
  void setUp() {
    var mockRequest = new MockHttpServletRequest();
    mockRequest.setScheme("https");
    mockRequest.setServerName("example.com");
    mockRequest.setServerPort(443);

    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
  }

  @Test
  void buildShortUrlShouldReturnFullUrl() {
    // given
    final String shortCode = "abc123";

    // when
    final String result = ShortUrlUtils.buildShortUrl(shortCode);

    // then
    final String expectedUrl = String.join("/", BASE_URL, shortCode);
    assertThat(result).isEqualTo(expectedUrl);
  }
}
