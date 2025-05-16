package com.innowise.urlshortener.repository;

import java.time.Duration;
import java.util.Optional;

public interface CacheRepository {

  void save(String key, String value);

  void save(String key, String value, Duration timeToLive);

  Optional<String> find(String key);
}
