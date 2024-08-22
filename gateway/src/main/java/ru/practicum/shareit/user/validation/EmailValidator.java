package ru.practicum.shareit.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidateEmail, UserUpdateDto> {

    @Override
    public boolean isValid(UserUpdateDto userDto, ConstraintValidatorContext context) {
        if (userDto.getEmail() != null) {
            Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
            return pattern.matcher(userDto.getEmail()).matches();
        }
        return true;
    }
}
