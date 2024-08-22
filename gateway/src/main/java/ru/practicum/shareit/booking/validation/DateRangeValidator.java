package ru.practicum.shareit.booking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

public class DateRangeValidator implements ConstraintValidator<ValidateDateRange, BookItemRequestDto> {

    @Override
    public boolean isValid(BookItemRequestDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            return true;
        }
        return bookingDto.getStart().isBefore(bookingDto.getEnd());
    }
}
