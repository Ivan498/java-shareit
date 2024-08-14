package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.GetBookingState;
import ru.practicum.shareit.booking.enums.StatusEnum;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NotAuthorizedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Transient
    @Override
    public BookingDto addBooking(Long userId, AddBookingDto bookingDto) {
        User user = userRepository.findById(userId).get();
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id '" + bookingDto.getItemId() + "' не найдена."));
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotAuthorizedException("Пользователь с id '" + userId +
                    "' не является владельцем вещи с id '" + item.getId() + "'.");
        }
        checkItemAvailability(item);
        final Booking booking = new Booking.Builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .status(StatusEnum.WAITING)
                .booker(user)
                .build();
        final Booking savedBooking = bookingRepository.save(booking);
        log.info("Пользователь с id '{}' добавил бронирование вещи с id '{}'.", userId, bookingDto.getItemId());
        return bookingMapper.toDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingDto confirmationOrRejectionOfBookingRequest(final Long userId, final Long bookingId, final Boolean approved) {
        findUser(userId);
        final Booking booking = findBooking(bookingId);
        final Item item = booking.getItem();
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotAuthorizedException("Пользователь с id '" + userId +
                    "' не является владельцем вещи с id '" + item.getId() + "'.");
        }
        if (!booking.getStatus().equals(StatusEnum.WAITING)) {
            throw new ItemUnavailableException("Вещь уже находится в аренде.");
        }
        if (approved) {
            booking.setStatus(StatusEnum.APPROVED);
        } else {
            booking.setStatus(StatusEnum.REJECTED);
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        findUser(userId);
        final Booking booking = findBooking(bookingId);
        if (booking.getItem().getOwner().getId().equals(userId)) {
            return bookingMapper.toDto(booking);
        } else {
            throw new NotAuthorizedException("У пользователя  нет прав для доступа к бронированию");
        }
    }

    private Booking findBooking(final Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id '" + bookingId + "' не найдено."));
    }

    private User findUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '" + userId + "' не найден."));
    }

    private void checkItemAvailability(Item item) {
        if (!item.getAvailable()) {
            throw new ItemUnavailableException("");
        }
    }
}
