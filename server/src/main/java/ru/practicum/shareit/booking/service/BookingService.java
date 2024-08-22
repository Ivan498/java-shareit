package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.GetBookingState;

import java.util.List;

public interface BookingService {
    List<BookingDto> getAllBookingsFromUserByStatus(Long userId, GetBookingState status, Integer from, Integer size);

    BookingDto addBooking(Long userId, AddBookingDto bookingDto);

    BookingDto getBookingById(Long userId, Long bookingId);

    BookingDto confirmationOrRejectionOfBookingRequest(final Long userId, final Long bookingId, final Boolean approved);
}
