package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
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

    List<Booking> findAllByBookerAndStatus(User booker, StatusEnum status, Pageable pageable);

    List<Booking> findAllByBooker(User booker, Pageable pageable);

    List<Booking> findAllByBookerAndEndIsBeforeAndStatus(User booker, LocalDateTime end, StatusEnum status, Pageable pageable);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfterAndStatus(User booker, LocalDateTime start, LocalDateTime end, StatusEnum status, Pageable pageable);

    List<Booking> findAllByBookerAndStartIsAfterAndStatus(User booker, LocalDateTime start, StatusEnum status, Pageable pageable);

    List<Booking> findAllByItemAndStatusAndStartIsAfter(Item item, StatusEnum statusEnum, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemAndStatusAndEndIsBefore(Item item, StatusEnum statusEnum, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemInAndStatus(List<Item> item, StatusEnum status, Pageable pageable);

    List<Booking> findAllByItemIn(List<Item> item, Pageable pageable);

    List<Booking> findAllByItemInAndEndIsBeforeAndStatus(List<Item> item, LocalDateTime end, StatusEnum status, Pageable pageable);

    List<Booking> findAllByItemInAndStartIsBeforeAndEndIsAfterAndStatus(List<Item> item, LocalDateTime start, LocalDateTime end, StatusEnum status, Pageable pageable);

    List<Booking> findAllByItemInAndStartIsAfterAndStatus(List<Item> item, LocalDateTime start, StatusEnum status, Pageable pageable);
}
