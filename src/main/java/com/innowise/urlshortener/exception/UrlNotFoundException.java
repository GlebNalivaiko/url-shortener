package com.innowise.urlshortener.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.experimental.StandardException;

@StandardException
public class UrlNotFoundException extends EntityNotFoundException {

  private static final String DEFAULT_MESSAGE = "Requested URL '%s' wasn't found";

  public UrlNotFoundException(String shortCode) {
    super(DEFAULT_MESSAGE.formatted(shortCode));
  }
}

