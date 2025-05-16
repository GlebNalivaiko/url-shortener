package com.innowise.urlshortener.valiator;

import static org.assertj.core.api.Assertions.assertThat;

import com.innowise.urlshortener.validator.Base62EncodedValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Base62EncodedValidatorTest {

  private final Base62EncodedValidator validator = new Base62EncodedValidator();

  @CsvSource(
      nullValues = "null",
      value = {
          "abc123,true",
          "ABCxyz019,true",
          "'',false",
          "' ',false",
          "'!@#$',false",
          "'abc-123',false",
          "null,false"
      }
  )
  @ParameterizedTest(name = "isValid(\"{0}\") should be {1}")
  void isValidShouldReturnExpectedResult(String input, boolean expected) {
    // given-when
    boolean result = validator.isValid(input, null);

    // then
    assertThat(result).isEqualTo(expected);
  }
}
