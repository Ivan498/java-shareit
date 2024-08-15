package ru.practicum.shareit.user.validation;

import ru.practicum.shareit.user.model.User;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidateEmail, User> {

    @Override
    public boolean isValid(User userDto, ConstraintValidatorContext context) {
        if (userDto.getEmail() != null) {
            Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
            return pattern.matcher(userDto.getEmail()).matches();
        }
        return true;
    }
}
