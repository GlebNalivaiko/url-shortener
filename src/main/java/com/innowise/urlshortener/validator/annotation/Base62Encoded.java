package com.innowise.urlshortener.validator.annotation;

import com.innowise.urlshortener.validator.Base62EncodedValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Base62EncodedValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Base62Encoded {

  String message() default "Must be a valid Base62 string";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
