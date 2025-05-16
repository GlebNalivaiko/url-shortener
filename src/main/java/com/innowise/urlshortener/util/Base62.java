package com.innowise.urlshortener.util;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for Base62 encoding and decoding.
 * <p>
 * Base62 uses characters from the set: 0-9, A-Z, a-z (total 62 characters).
 */
@UtilityClass
public class Base62 {

  private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final int BASE = ALPHABET.length();
  private static final char[] ALPHABET_ARRAY = ALPHABET.toCharArray();
  private static final Map<Character, Integer> REVERSE_LOOKUP_MAP;

  static {
    REVERSE_LOOKUP_MAP = IntStream.range(0, ALPHABET_ARRAY.length)
        .boxed()
        .collect(Collectors.toUnmodifiableMap(i -> ALPHABET_ARRAY[i], Function.identity()));
  }

  /**
   * Encodes a non-negative long number to a Base62 string.
   *
   * @param number the number to encode, must be >= 0
   * @return the Base62 encoded string
   * @throws IllegalArgumentException if number is negative
   */
  public static String encode(long number) {
    if (number < 0) {
      throw new IllegalArgumentException("Number must be non-negative");
    }

    if (number == 0) {
      return String.valueOf(ALPHABET_ARRAY[0]);
    }

    var stringBuilder = new StringBuilder();
    while (number > 0) {
      int remainder = (int) (number % BASE);
      stringBuilder.append(ALPHABET_ARRAY[remainder]);
      number /= BASE;
    }

    return stringBuilder.reverse().toString();
  }

  /**
   * Decodes a Base62-encoded string into a long number.
   *
   * @param input the Base62 string to decode
   * @return the decoded number
   * @throws IllegalArgumentException if input is null, empty, or contains invalid characters
   */
  public static long decode(String input) {
    if (StringUtils.isEmpty(input)) {
      throw new IllegalArgumentException("Input cannot be null or empty");
    }

    var result = 0L;
    for (char c : input.toCharArray()) {
      int value = Optional.ofNullable(REVERSE_LOOKUP_MAP.get(c))
          .orElseThrow(() -> new IllegalArgumentException("Invalid Base62 character: " + c));

      result = result * BASE + value;
    }

    return result;
  }

  /**
   * Checks whether a given string is a valid Base62-encoded string.
   *
   * @param input the string to validate
   * @return true if valid, false otherwise
   */
  public static boolean isValidBase62(String input) {
    return StringUtils.isNotEmpty(input)
        && input.chars().allMatch(c -> REVERSE_LOOKUP_MAP.containsKey((char) c));
  }
}
