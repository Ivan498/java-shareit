package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserUpdateDto userUpdateDto);

    UserDto findUserById(long userId);

    List<UserDto> findAllUsers();

    void deleteUserById(long userId);
}
