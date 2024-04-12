package ru.practicum.main.api.privats.service.event;

import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventNewDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.dto.event.EventUpdateUserRequest;

import java.util.List;

public interface EventPrivateService {
    EventFullDto addEvent(EventNewDto request, Long userId);

    EventFullDto updateEventByOwner(Long userId, Long eventId, EventUpdateUserRequest request);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto getEventByUserIdAndEvent(Long userId, Long eventId);
}
