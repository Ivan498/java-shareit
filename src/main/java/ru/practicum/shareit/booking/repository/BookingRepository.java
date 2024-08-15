package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.StatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByItemIdAndBookerId(Long itemId, Long bookerId);

    List<Booking> findAllByBookerAndStatus(User booker, StatusEnum status, Sort sort);

    List<Booking> findAllByBooker(User booker, Sort sort);

    List<Booking> findAllByBookerAndEndIsBeforeAndStatus(User booker, LocalDateTime end, StatusEnum status, Sort sort);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfterAndStatus(User booker, LocalDateTime start, LocalDateTime end, StatusEnum status, Sort sort);

    List<Booking> findAllByBookerAndStartIsAfterAndStatus(User booker, LocalDateTime start, StatusEnum status, Sort sort);

    List<Booking> findAllByItemAndStatusAndStartIsAfter(Item item, StatusEnum statusEnum, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemAndStatusAndEndIsBefore(Item item, StatusEnum statusEnum, LocalDateTime end, Sort sort);
}
