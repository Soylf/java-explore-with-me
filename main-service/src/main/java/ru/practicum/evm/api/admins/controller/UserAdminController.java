package ru.practicum.evm.api.admins.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.api.admins.service.user.UserAdminService;
import ru.practicum.evm.dto.user.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService service;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto request) {
        log.info("UserAdminController: запрос на создания пользователя");
        return service.addUser(request);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                  @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("UserAdminController: запрос на получения списка пользователей");
        return service.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable(name = "userId") Long userId) {
        log.info("UserAdminController: запрос на удаление пользователя");
        service.deleteUser(userId);
    }
}
