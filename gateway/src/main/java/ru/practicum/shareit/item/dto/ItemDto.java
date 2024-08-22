package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private long id;
    @NotBlank(message = "Название вещи не может быть пустым.")
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым.")
    private String description;
    @NotNull(message = "Доступность вещи не может быть пустым.")
    private Boolean available;
    private List<CommentDto> comments;
    private BookingDto nextBooking;
    private BookingDto lastBooking;
}

