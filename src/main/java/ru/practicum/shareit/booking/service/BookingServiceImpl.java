package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private static final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    @Override
    @Transactional
    public List<BookingDto> getAllBookingsFromUserByStatus(Long userId, GetBookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '" + userId + "' не найден."));
        Sort sort = Sort.by("start").descending();
        List<Booking> bookingList = switch (state) {
            case ALL -> bookingRepository.findAllByBooker(user, sort);
            case PAST -> bookingRepository.findAllByBookerAndEndIsBeforeAndStatus(user, LocalDateTime.now(),
                    StatusEnum.APPROVED, sort);
            case CURRENT -> bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterAndStatus(user,
                    LocalDateTime.now(), LocalDateTime.now(), StatusEnum.APPROVED, sort);
            case FUTURE -> bookingRepository.findAllByBookerAndStartIsAfterAndStatus(user, LocalDateTime.now(),
                    StatusEnum.APPROVED, sort);
            case WAITING -> bookingRepository.findAllByBookerAndStatus(user, StatusEnum.WAITING, sort);
            case REJECTED -> bookingRepository.findAllByBookerAndStatus(user, StatusEnum.REJECTED, sort);
        };
        return bookingMapper.toDtoList(bookingList);
    }

    @Transactional
    @Override
    public BookingDto addBooking(Long userId, AddBookingDto bookingDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '" + userId + "' не найден."));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id '" + bookingDto.getItemId() + "' не найдена."));
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotAuthorizedException("Пользователь с id '" + userId +
                    "' не является владельцем вещи с id '" + item.getId() + "'.");
        }
        checkItemAvailability(item);
        final Booking booking = Booking.builder()
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
    @Transactional
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
