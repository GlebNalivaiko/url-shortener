package com.innowise.urlshortener.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UrlShortenRequestValidationTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @Test
  void whenOriginalUrlIsValidThenNoViolations() {
    // given
    final UrlShortenRequest request = new UrlShortenRequest("https://example.com");

    // when
    final Set<ConstraintViolation<UrlShortenRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).isEmpty();
  }

  @Test
  void whenOriginalUrlIsBlankThenViolation() {
    // given
    final UrlShortenRequest request = new UrlShortenRequest("");

    // when
    final Set<ConstraintViolation<UrlShortenRequest>> violations = validator.validate(request);

    // then
    assertThat(violations)
        .hasSize(1)
        .anyMatch(v -> v.getPropertyPath().toString().equals("originalUrl")
            && v.getMessage().contains("must not be blank"));
  }

  @Test
  void whenOriginalUrlIsInvalidUrlThenViolation() {
    // given
    final UrlShortenRequest request = new UrlShortenRequest("invalid-url");

    // when
    final Set<ConstraintViolation<UrlShortenRequest>> violations = validator.validate(request);

    // then
    assertThat(violations)
        .hasSize(1)
        .anyMatch(v -> v.getPropertyPath().toString().equals("originalUrl")
            && v.getMessage().contains("must be a valid URL"));
  }

  @Test
  void whenOriginalUrlIsTooLongThenViolation() {
    // given
    final String longUrl = "https://" + "a".repeat(2050) + ".com";
    final UrlShortenRequest request = new UrlShortenRequest(longUrl);

    // when
    final Set<ConstraintViolation<UrlShortenRequest>> violations = validator.validate(request);

    // then
    assertThat(violations)
        .hasSize(1)
        .anyMatch(v -> v.getPropertyPath().toString().equals("originalUrl")
            && v.getMessage().contains("size must be between"));
  }
}
