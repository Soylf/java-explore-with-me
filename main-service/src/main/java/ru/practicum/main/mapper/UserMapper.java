package ru.practicum.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.main.dto.user.UserDto;
import ru.practicum.main.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    List<UserDto> toUserDosList(List<User> users);
}