package com.innowise.urlshortener.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.innowise.urlshortener.dto.response.UrlShortenResponse;
import com.innowise.urlshortener.entity.Url;
import com.innowise.urlshortener.util.ShortUrlUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class UrlMapperTest {

  private static final String BASE_URL = "https://example.com";

  private final UrlMapper urlMapper = Mappers.getMapper(UrlMapper.class);

  @BeforeEach
  void setUp() {
    var mockRequest = new MockHttpServletRequest();
    mockRequest.setScheme("https");
    mockRequest.setServerName("example.com");
    mockRequest.setServerPort(443);

    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
  }

  @Test
  void toShortenResponseShouldMapShortUrlUsingShortUrlUtils() {
    // given
    final String originalUrl = BASE_URL + "/some/long/url";
    final Url url = new Url(1L, originalUrl, "abc123");

    // when
    final UrlShortenResponse response = urlMapper.toShortenResponse(url);

    // then
    final String expectedShortUrl = ShortUrlUtils.buildShortUrl("abc123");
    assertAll(
        () -> assertThat(response.id()).isEqualTo(1L),
        () -> assertThat(response.shortUrl()).isEqualTo(expectedShortUrl));
  }
}
