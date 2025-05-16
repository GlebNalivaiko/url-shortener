package com.innowise.urlshortener.service.redis.key;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedisKeyGeneratorTest {

  private static final String PREFIX = "test-app";

  private RedisKeyGenerator redisKeyGenerator;

  @BeforeEach
  void setUp() {
    redisKeyGenerator = new RedisKeyGenerator(PREFIX);
  }

  @Test
  void getOriginalUrlByShortCodeKeyShouldReturnCorrectKey() {
    // given
    final String shortCode = "abc123";

    // when
    final String result = redisKeyGenerator.getOriginalUrlByShortCodeKey(shortCode);

    // then
    final String expectedKey = "test-app:url:shortCode:abc123";
    assertThat(result).isEqualTo(expectedKey);
  }
}
