package com.innowise.urlshortener.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.urlshortener.repository.CacheRepository;
import com.innowise.urlshortener.service.redis.key.RedisKeyGenerator;
import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RedisCacheServiceTest {

  private static final Duration URL_TTL = Duration.ofMinutes(5);

  @Mock
  private CacheRepository repository;
  @Mock
  private RedisKeyGenerator redisKeyGenerator;

  @InjectMocks
  private RedisCacheService redisCacheService;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(redisCacheService, "urlTimeToLive", URL_TTL);
  }

  @Test
  void saveUrlShouldCallRepositoryWithCorrectKeyAndValue() {
    // given
    final String shortCode = "abc123";
    final String expectedKey = "prefix:url:shortCode:" + shortCode;
    final String originalUrl = "https://example.com";

    when(redisKeyGenerator.getOriginalUrlByShortCodeKey(shortCode)).thenReturn(expectedKey);

    // when
    redisCacheService.saveUrl(shortCode, originalUrl);

    // then
    verify(repository).save(expectedKey, originalUrl, URL_TTL);
  }

  @Test
  void findUrlShouldReturnOriginalUrlIfFoundInCache() {
    // given
    final String shortCode = "abc123";
    final String expectedKey = "prefix:url:shortCode:" + shortCode;
    final String expectedUrl = "https://example.com";

    when(redisKeyGenerator.getOriginalUrlByShortCodeKey(shortCode)).thenReturn(expectedKey);
    when(repository.find(expectedKey)).thenReturn(Optional.of(expectedUrl));

    // when
    Optional<String> result = redisCacheService.findUrl(shortCode);

    // then
    assertThat(result).contains(expectedUrl);
  }

  @Test
  void findUrlShouldReturnEmptyIfNotFoundInCache() {
    // given
    final String shortCode = "notfound";
    final String expectedKey = "url:notfound";

    when(redisKeyGenerator.getOriginalUrlByShortCodeKey(shortCode)).thenReturn(expectedKey);
    when(repository.find(expectedKey)).thenReturn(Optional.empty());

    // when
    final Optional<String> result = redisCacheService.findUrl(shortCode);

    // then
    assertThat(result).isEmpty();
  }
}
