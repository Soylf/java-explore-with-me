package ru.practicum.main.api.admins.service.user;

import ru.practicum.main.dto.user.UserDto;

import java.util.List;

public interface UserAdminService {
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto addUser(UserDto request);

    void deleteUser(Long userId);
}
