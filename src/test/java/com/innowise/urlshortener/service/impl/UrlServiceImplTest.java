package com.innowise.urlshortener.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.innowise.urlshortener.dto.response.UrlShortenResponse;
import com.innowise.urlshortener.entity.Url;
import com.innowise.urlshortener.exception.UrlNotFoundException;
import com.innowise.urlshortener.mapper.UrlMapper;
import com.innowise.urlshortener.repository.UrlRepository;
import com.innowise.urlshortener.service.CacheService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

  private static final String ORIGINAL_URL = "https://example.com";

  @Mock
  private UrlRepository urlRepository;
  @Mock
  private UrlMapper urlMapper;
  @Mock
  private CacheService cacheService;

  @InjectMocks
  private UrlServiceImpl urlService;

  @Test
  void shortenUrlShouldReturnMappedResponseWhenOriginalUrlExists() {
    // given
    final Url existingUrl = new Url(1L, ORIGINAL_URL, "abc123");
    final UrlShortenResponse response = new UrlShortenResponse(1L, "abc123");

    when(urlRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.of(existingUrl));
    when(urlMapper.toShortenResponse(existingUrl)).thenReturn(response);

    // when
    final UrlShortenResponse result = urlService.shortenUrl(ORIGINAL_URL);

    // then
    assertThat(result).isEqualTo(response);
    verifyNoMoreInteractions(urlRepository);
    verifyNoMoreInteractions(cacheService);
  }

  @Test
  void shortenUrlShouldGenerateShortCodeSaveAndReturnResponseWhenOriginalUrlNotExists() {
    // given
    final long nextId = 12345L;
    final String expectedShortCode = "3D7";
    final Url newUrl = new Url(nextId, ORIGINAL_URL, expectedShortCode);
    final UrlShortenResponse response = new UrlShortenResponse(1L, expectedShortCode);

    when(urlRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.empty());
    when(urlRepository.getNextSequenceValue()).thenReturn(nextId);
    when(urlMapper.toShortenResponse(newUrl)).thenReturn(response);

    // when
    final UrlShortenResponse result = urlService.shortenUrl(ORIGINAL_URL);

    // then
    assertAll(
        () -> assertThat(result).isEqualTo(response),
        () -> verify(urlRepository).save(newUrl),
        () -> verify(cacheService).saveUrl(expectedShortCode, ORIGINAL_URL));
  }

  @Test
  void getOriginalUrlShouldReturnItWhenUrlIsInCache() {
    // given
    final String shortCode = "abc123";

    when(cacheService.findUrl(shortCode)).thenReturn(Optional.of(ORIGINAL_URL));

    // when
    final String result = urlService.getOriginalUrl(shortCode);

    // then
    assertAll(
        () -> assertThat(result).isEqualTo(ORIGINAL_URL),
        () -> verifyNoInteractions(urlRepository));
  }

  @Test
  void getOriginalUrlShouldReturnItWhenNotInCacheButInDb() {
    // given
    final String shortCode = "xyz789";
    final Url url = new Url(1L, ORIGINAL_URL, shortCode);

    when(cacheService.findUrl(shortCode)).thenReturn(Optional.empty());
    when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));

    // when
    final String result = urlService.getOriginalUrl(shortCode);

    // then
    assertAll(
        () -> assertThat(result).isEqualTo(ORIGINAL_URL),
        () -> verify(cacheService).saveUrl(shortCode, ORIGINAL_URL));
  }

  @Test
  void getOriginalUrlShouldThrowExceptionWhenNotFoundAnywhere() {
    // given
    final String shortCode = "notfound";

    when(cacheService.findUrl(shortCode)).thenReturn(Optional.empty());
    when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

    // when-then
    assertThatThrownBy(() -> urlService.getOriginalUrl(shortCode))
        .isInstanceOf(UrlNotFoundException.class)
        .hasMessageContaining(shortCode);
  }
}
