package com.innowise.urlshortener.service.impl;

import com.innowise.urlshortener.repository.CacheRepository;
import com.innowise.urlshortener.service.CacheService;
import com.innowise.urlshortener.service.redis.key.RedisKeyGenerator;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheService implements CacheService {

  private final CacheRepository repository;
  private final RedisKeyGenerator redisKeyGenerator;

  @Value("${app.cache.url.timeToLive}")
  private final Duration urlTimeToLive;

  @Override
  public void saveUrl(String shortCode, String originalUrl) {
    var key = redisKeyGenerator.getOriginalUrlByShortCodeKey(shortCode);
    repository.save(key, originalUrl, urlTimeToLive);
    log.debug("Original URL '{}' was saved using key '{}'", originalUrl, key);
  }

  @Override
  public Optional<String> findUrl(String shortCode) {
    var key = redisKeyGenerator.getOriginalUrlByShortCodeKey(shortCode);
    var originalUrl = repository.find(key);
    if (originalUrl.isPresent()) {
      log.debug("Found original URL for shortCode '{}', key '{}'", shortCode, key);
    } else {
      log.debug("No original URL found for shortCode '{}', key '{}'", shortCode, key);
    }

    return originalUrl;
  }
}
