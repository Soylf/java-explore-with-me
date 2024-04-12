package ru.practicum.main.api.privats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.api.privats.service.event.EventPrivateService;
import ru.practicum.main.api.privats.service.request.RequestPrivateService;
import ru.practicum.main.dto.event.*;
import ru.practicum.main.dto.request.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventPrivateService eventService;
    private final RequestPrivateService requestService;

    @PostMapping("/{userId}/events")
    public EventFullDto addEvent(@PathVariable(name = "userId") @Positive Long userId,
                                 @RequestBody @Valid EventNewDto request) {
        log.info("EventPrivateController: запрос на создания EventFullDto");
        return eventService.addEvent(request, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByOwner(@PathVariable(name = "userId") @Positive Long userId,
                                           @PathVariable(name = "eventId") @Positive Long eventId,
                                           @RequestBody @Valid EventUpdateUserRequest request) {
        log.info("EventPrivateController: запрос на обновления события (EventFullDto)");
        return eventService.updateEventByOwner(userId, eventId, request);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequest(@PathVariable(name = "userId") @Positive Long userId,
                                                              @PathVariable(name = "eventId") @Positive Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("EventPrivateController: запрос на обновления события (EventRequestStatusUpdateResult)");
        return requestService.updateStatusRequest(userId, eventId, request);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable(name = "userId") @Positive Long userId,
                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("EventPrivateController: запрос на списка EventShortDto по такому Энпоинту: (\"/{userId}/events\")");
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUserAndEvent(@PathVariable(name = "userId") @Positive Long userId,
                                               @PathVariable(name = "eventId") @Positive Long eventId) {
        log.info("EventPrivateController: запрос на получения списка EventFullDto по такому Эпоинту: (\"/{userId}/events/{eventId}\")");
        return eventService.getEventByUserIdAndEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestByEvent(@PathVariable(name = "userId") Long userId,
                                              @PathVariable(name = "eventId") Long eventId) {
        log.info("EventPrivateController: запрос на получения списка RequestDto по такому Эпоинту: (\"/{userId}/events/{eventId}/requests\")");
        return requestService.getRequestByEvent(userId, eventId);
    }


}
