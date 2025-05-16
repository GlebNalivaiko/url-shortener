package com.innowise.urlshortener.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

@ExtendWith(MockitoExtension.class)
class RedissonRepositoryTest {

  private static final String KEY = "key";
  @Mock
  private RedissonClient redissonClient;
  @Mock
  private RBucket<String> bucket;

  @InjectMocks
  private RedissonRepository repository;

  @BeforeEach
  void setUp() {
    when(redissonClient.<String>getBucket(KEY)).thenReturn(bucket);
  }

  @Test
  void saveShouldCallSetWithoutTTL() {
    // given
    final String value = "value";

    // when
    repository.save(KEY, value);

    // then
    assertAll(
        () -> verify(bucket).set(value),
        () -> verifyNoMoreInteractions(bucket, redissonClient));
  }

  @Test
  void saveShouldCallSetWithTTL() {
    // given
    final String value = "value";
    final Duration ttl = Duration.ofMinutes(10);

    // when
    repository.save(KEY, value, ttl);

    // then
    assertAll(
        () -> verify(bucket).set(value, ttl),
        () -> verifyNoMoreInteractions(bucket, redissonClient));
  }

  @Test
  void findShouldReturnValueIfPresentAndNotBlank() {
    // given
    final String value = "someValue";

    when(bucket.get()).thenReturn(value);

    // when
    final Optional<String> result = repository.find(KEY);

    // then
    assertAll(
        () -> assertThat(result).contains(value),
        () -> verifyNoMoreInteractions(bucket, redissonClient));
  }

  @Test
  void findShouldReturnEmptyIfValueIsBlank() {
    // given
    final String blankValue = "   ";

    when(bucket.get()).thenReturn(blankValue);

    // when
    final Optional<String> result = repository.find(KEY);

    // then
    assertAll(
        () -> assertThat(result).isEmpty(),
        () -> verifyNoMoreInteractions(bucket, redissonClient));
  }

  @Test
  void findShouldReturnEmptyIfValueIsNull() {
    // given
    when(bucket.get()).thenReturn(null);

    // when
    final Optional<String> result = repository.find(KEY);

    // then
    assertAll(
        () -> assertThat(result).isEmpty(),
        () -> verifyNoMoreInteractions(bucket, redissonClient));
  }
}
