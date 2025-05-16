package com.innowise.urlshortener.service;

import java.util.Optional;

public interface CacheService {

  void saveUrl(String shortCode, String originalUrl);

  Optional<String> findUrl(String shortCode);
}
