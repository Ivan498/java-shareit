package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemUpdateDto {
    @NotBlank(message = "Название не может быть пустым.")
    private String name;
    private String description;
    @NotNull(message = "Не указан статус доступности.")
    private Boolean available;
}
