package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.mapstruct.factory.Mappers.*;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = getMapper(UserMapper.class);

    UserDto toDto(User user);

    User toModel(UserDto userDto);

    List<UserDto> toDtoList(List<User> userList);

    List<User> toModelList(List<UserDto> userDtoList);
}
