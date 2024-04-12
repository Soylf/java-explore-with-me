package ru.practicum.main.api.publics.service.event;

import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsPublicService {
    List<EventShortDto> getAllEvents(String text, List<Long> categories, Boolean paid, LocalDateTime startDate, LocalDateTime endDate, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto getEventById(Long eventId);
}
