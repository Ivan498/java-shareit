package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.validation.ValidateEmail;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidateEmail
public class UserUpdateDto {
    private String name;
    private String email;
}
