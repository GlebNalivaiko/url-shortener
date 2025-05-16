package com.innowise.urlshortener.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.innowise.urlshortener.dto.response.UrlShortenResponse;
import com.innowise.urlshortener.entity.Url;
import com.innowise.urlshortener.util.ShortUrlUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING, imports = ShortUrlUtils.class)
public interface UrlMapper {

  @Mapping(target = "shortUrl", expression = "java(ShortUrlUtils.buildShortUrl(url.getShortCode()))")
  UrlShortenResponse toShortenResponse(Url url);
}
