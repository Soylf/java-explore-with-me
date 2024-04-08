package ru.practicum.evm.api.privats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.api.privats.service.request.RequestPrivateService;
import ru.practicum.evm.dto.request.RequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestPrivateService service;

    @PostMapping("/{userId}/requests")
    public RequestDto addRequest(@PathVariable Long userId,
                                 @RequestParam Long eventId) {
        log.info("RequestPrivateController: запрос на создания RequestDto");
        return service.addRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<RequestDto> getAllRequests(@PathVariable(name = "userId") Long userId) {
        log.info("RequestPrivateController: запрос на получения списка RequestDto");
        return service.getAllRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto updateRequest(@PathVariable(name = "userId") Long userId,
                                    @PathVariable(name = "requestId") Long requestId) {
        log.info("RequestPrivateController: запрос на обновления события");
        return service.updateRequest(userId, requestId);
    }
}
