package ru.practicum.main.service.admins.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.dto.user.UserDto;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.UserMapper;
import ru.practicum.main.model.User;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAdminServiceImpl implements UserAdminService {
    private final UserRepository repository;

    @Override
    @Transactional
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            Page<User> userPage = repository.findAll(PageRequest.of(from, size));
            List<User> users = userPage.getContent();
            return UserMapper.MAPPER.toUserDosList(users);
        }
        Page<User> userPage = repository.findAllByIdIn(ids, PageRequest.of(from, size));
        List<User> users = userPage.getContent();
        return UserMapper.MAPPER.toUserDosList(users);
    }

    @Transactional
    @Override
    public UserDto addUser(UserDto request) {
        User user = UserMapper.MAPPER.toUser(request);
        User saveUser = repository.save(user);
        return UserMapper.MAPPER.toUserDto(saveUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        checkUser(userId);
        repository.deleteById(userId);
    }

    //Дополнительные методы
    private void checkUser(Long userId) {
        repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id= '%s' не найден", userId)));
    }
}
