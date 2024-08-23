package ru.practicum.shareit.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateEmail {
    String message() default "Некорректный формат электронной почты.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
