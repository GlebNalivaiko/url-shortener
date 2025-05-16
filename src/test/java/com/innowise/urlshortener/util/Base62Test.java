package com.innowise.urlshortener.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class Base62Test {

  @Test
  @DisplayName("should preserve value after encoding and decoding")
  void roundTripEncodeDecode() {
    // given-when-then
    LongStream.iterate(0, i -> i < 100_000, i -> i + 1234).forEach(i -> {
      final String encoded = Base62.encode(i);
      final long decoded = Base62.decode(encoded);
      assertThat(decoded)
          .withFailMessage("Round-trip failed for value: %d -> '%s' -> %d", i, encoded, decoded)
          .isEqualTo(i);
    });
  }

  @Nested
  @DisplayName("Encode method")
  class EncodeTests {

    @CsvSource({
        "0, 0",
        "1, 1",
        "61, z",
        "62, 10",
        "9876549, fRLB",
        "123456789, 8M0kX",
        "9223372036854775807, AzL8n0Y58m7"
    })
    @ParameterizedTest(name = "should encode '{0}' to '{1}'")
    void encodeValues(long input, String expected) {
      // given-when-then
      assertThat(Base62.encode(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, -100})
    @DisplayName("should throw exception for negative numbers")
    void encodeNegative(long negativeValue) {
      // given-when-then
      assertThatThrownBy(() -> Base62.encode(negativeValue))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("non-negative");
    }
  }

  @Nested
  @DisplayName("Decode method")
  class DecodeTests {

    @CsvSource({
        "0, 0",
        "1, 1",
        "z, 61",
        "10, 62",
        "fRLB, 9876549",
        "8M0kX, 123456789",
        "AzL8n0Y58m7, 9223372036854775807"
    })
    @ParameterizedTest(name = "should decode '{0}' to '{1}'")
    void decodeValues(String input, long expected) {
      // given-when-then
      assertThat(Base62.decode(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc$", "!", "#", "Av1+"})
    @DisplayName("should throw exception for null, empty or invalid characters")
    void decodeInvalidInputs(String invalid) {
      // given-when-then
      assertThatThrownBy(() -> Base62.decode(invalid))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Invalid Base62 character");
    }
  }

  @Nested
  @DisplayName("Validation method")
  class Base62ValidationTests {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "abc+", " ", "\n"})
    @DisplayName("should return false for invalid Base62 strings")
    void invalidBase62(String invalid) {
      // given-when-then
      assertThat(Base62.isValidBase62(invalid)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "abcDEF123", "Zz9"})
    @DisplayName("should return true for valid Base62 strings")
    void validBase62(String valid) {
      // given-when-then
      assertThat(Base62.isValidBase62(valid)).isTrue();
    }
  }
}
