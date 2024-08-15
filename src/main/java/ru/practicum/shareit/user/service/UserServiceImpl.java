package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    @Transactional
    public UserDto addUser(final UserDto userDto) {
        final User user = userMapper.toModel(userDto);
        final User addedUser = repository.save(user);
        log.info("Добавление нового пользователя: {}.", addedUser);
        return userMapper.toDto(addedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(final long userId, final UserUpdateDto userUpdateDto) {
        final User foundUser = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '" + userId + "' не найден."));
        foundUser.setName(Optional.ofNullable(userUpdateDto.getName())
                .orElse(foundUser.getName()));
        foundUser.setEmail(Optional.ofNullable(userUpdateDto.getEmail())
                .orElse(foundUser.getEmail()));
        log.info("Обновление пользователя с id '{}': {}.", userId, foundUser);
        return userMapper.toDto(foundUser);
    }

    @Override
    @Transactional
    public UserDto findUserById(final long userId) {
        final User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '" + userId + "' не найден."));
        log.info("Получение пользователя с id '{}.", userId);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public List<UserDto> findAllUsers() {
        final List<User> users = repository.findAll();
        log.info("Получение списка всех пользователей.");
        return userMapper.toDtoList(users);
    }

    @Override
    @Transactional
    public void deleteUserById(final long userId) {
        repository.deleteById(userId);
        log.info("Удаление пользователя с id '{}'.", userId);
    }
}
