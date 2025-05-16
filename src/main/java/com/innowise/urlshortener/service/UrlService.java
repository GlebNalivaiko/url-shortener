package com.innowise.urlshortener.service;

import com.innowise.urlshortener.dto.response.UrlShortenResponse;

public interface UrlService {

  UrlShortenResponse shortenUrl(String originalUrl);

  String getOriginalUrl(String shortCode);
}
