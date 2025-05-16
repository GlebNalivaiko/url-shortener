package com.innowise.urlshortener.repository.impl;

import com.innowise.urlshortener.repository.CacheRepository;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedissonRepository implements CacheRepository {

  private final RedissonClient redissonClient;

  @Override
  public void save(String key, String value) {
    redissonClient.getBucket(key).set(value);
  }

  @Override
  public void save(String key, String value, Duration timeToLive) {
    redissonClient.getBucket(key).set(value, timeToLive);
  }

  @Override
  public Optional<String> find(String key) {
    RBucket<String> bucket = redissonClient.getBucket(key);
    return Optional.ofNullable(bucket.get())
        .filter(StringUtils::isNotBlank);
  }
}
