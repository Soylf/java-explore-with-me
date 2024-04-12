package ru.practicum.main.api.admins.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.api.repository.UserRepository;
import ru.practicum.main.dto.user.UserDto;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.UserMapper;
import ru.practicum.main.model.User;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            Page<User> users = repository.findAll(PageRequest.of(from, size));
            return UserMapper.MAPPER.toUserDosList(users.getContent());
        }
        Page<User> users = repository.findAllByIdIn(ids, PageRequest.of(from, size));
        return UserMapper.MAPPER.toUserDosList(users.getContent());
    }

    @Override
    public UserDto addUser(UserDto request) {
        User saveUser = repository.save(UserMapper.MAPPER.toUser(request));
        return UserMapper.MAPPER.toUserDto(saveUser);
    }

    @Override
    public void deleteUser(Long userId) {
        checkUser(userId);
        repository.deleteById(userId);
    }

    //Дополнительные методы
    public void checkUser(Long userId) {
        repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id= '%s' не найден", userId)));
    }
}
