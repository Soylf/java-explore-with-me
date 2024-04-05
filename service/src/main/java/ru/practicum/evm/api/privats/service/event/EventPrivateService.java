package ru.practicum.evm.api.privats.service.event;

import ru.practicum.evm.dto.event.EventFullDto;
import ru.practicum.evm.dto.event.EventNewDto;
import ru.practicum.evm.dto.event.EventShortDto;
import ru.practicum.evm.dto.event.EventUpdateUserRequest;

import java.util.List;

public interface EventPrivateService {
    EventFullDto addEvent(EventNewDto request, Long userId);

    EventFullDto updateEventByOwner(Long userId, Long eventId, EventUpdateUserRequest request);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto getEventByUserIdAndEvent(Long userId, Long eventId);
}
