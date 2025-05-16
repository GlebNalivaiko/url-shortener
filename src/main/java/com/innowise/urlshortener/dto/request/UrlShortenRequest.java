package com.innowise.urlshortener.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UrlShortenRequest(

    @URL
    @NotBlank
    @Size(max = 2048)
    String originalUrl
) {

}
