package com.innowise.urlshortener.validator;

import com.innowise.urlshortener.util.Base62;
import com.innowise.urlshortener.validator.annotation.Base62Encoded;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;

public class Base62EncodedValidator implements ConstraintValidator<Base62Encoded, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return Optional.ofNullable(value)
        .map(Base62::isValidBase62)
        .orElse(false);
  }
}
