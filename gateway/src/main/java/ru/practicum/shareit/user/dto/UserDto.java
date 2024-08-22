package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.user.validation.ValidateEmail;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidateEmail
public class UserDto extends UserUpdateDto{
    private long id;
    @NotNull(message = "Имя пользователя обязательно может быть заполнено.")
    @NotBlank(message = "Имя пользователя не может быть пустым.")
    private String name;
    @NotBlank(message = "Должен быть обязательно указан email.")
    private String email;
}
