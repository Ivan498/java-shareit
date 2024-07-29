package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @Email(message = "Некорректный формат электронной почты.")
    private String email;
}
