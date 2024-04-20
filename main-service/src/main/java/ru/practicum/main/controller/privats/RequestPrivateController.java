package ru.practicum.main.controller.privats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.privats.request.RequestPrivateService;
import ru.practicum.main.dto.request.RequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestPrivateService service;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
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
