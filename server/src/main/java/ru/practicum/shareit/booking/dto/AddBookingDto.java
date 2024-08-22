package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.validation.ValidateDateRange;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidateDateRange
public class AddBookingDto {
    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}
