package ru.practicum.shareit.user.dto;

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
public class UserDto {
    private long id;
    @NotNull(message = "Имя пользователя обязательно может быть заполнено.")
    @NotBlank(message = "Имя пользователя не может быть пустым.")
    private String name;
    @NotBlank(message = "Должен быть обязательно указан email.")
    private String email;
}
