package com.innowise.urlshortener.service.impl;

import com.innowise.urlshortener.dto.response.UrlShortenResponse;
import com.innowise.urlshortener.entity.Url;
import com.innowise.urlshortener.exception.UrlNotFoundException;
import com.innowise.urlshortener.mapper.UrlMapper;
import com.innowise.urlshortener.repository.UrlRepository;
import com.innowise.urlshortener.service.CacheService;
import com.innowise.urlshortener.service.UrlService;
import com.innowise.urlshortener.util.Base62;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

  private final UrlRepository urlRepository;
  private final UrlMapper urlMapper;
  private final CacheService cacheService;

  @Override
  @Transactional
  public UrlShortenResponse shortenUrl(String originalUrl) {
    return urlRepository.findByOriginalUrl(originalUrl)
        .map(urlMapper::toShortenResponse)
        .orElseGet(() -> saveUrl(originalUrl));
  }

  private UrlShortenResponse saveUrl(String originalUrl) {
    var nextId = urlRepository.getNextSequenceValue();
    var shortCode = Base62.encode(nextId);
    var url = new Url(nextId, originalUrl, shortCode);

    urlRepository.save(url);
    cacheService.saveUrl(shortCode, originalUrl);

    return urlMapper.toShortenResponse(url);
  }

  @Override
  @Transactional(readOnly = true)
  public String getOriginalUrl(String shortCode) {
    return cacheService.findUrl(shortCode)
        .or(() -> fetchAndCacheOriginalUrl(shortCode))
        .orElseThrow(() -> new UrlNotFoundException(shortCode));
  }

  private Optional<String> fetchAndCacheOriginalUrl(String shortCode) {
    return urlRepository.findByShortCode(shortCode)
        .map(Url::getOriginalUrl)
        .map(originalUrl -> {
          cacheService.saveUrl(shortCode, originalUrl);
          return originalUrl;
        });
  }
}
