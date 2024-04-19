package ru.practicum.main.api.admins.service.event;

import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventUpdateAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsAdminService {
    List<EventFullDto> getAllEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime startDate, LocalDateTime endDate, Integer from, Integer size);

    EventFullDto updateEventById(Long eventId, EventUpdateAdminRequest request);
}
