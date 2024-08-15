package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.AddBookingDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidateDateRange, AddBookingDto> {

    @Override
    public boolean isValid(AddBookingDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            return true;
        }
        return bookingDto.getStart().isBefore(bookingDto.getEnd());
    }
}
