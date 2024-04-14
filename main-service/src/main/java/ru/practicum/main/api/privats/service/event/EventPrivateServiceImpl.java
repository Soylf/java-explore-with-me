package ru.practicum.main.api.privats.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.main.api.repository.CategoryRepository;
import ru.practicum.main.api.repository.EventRepository;
import ru.practicum.main.api.repository.UserRepository;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventNewDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.dto.event.EventUpdateUserRequest;
import ru.practicum.main.error.exception.BadRequestException;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.mapper.LocationMapper;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;
import ru.practicum.main.model.state.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventPrivateServiceImpl implements EventPrivateService {

    // Константа, определяющая количество часов до начала события
    private static final int HOURS_BEFORE_START_EVENT = 2;

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public EventFullDto addEvent(EventNewDto request, Long userId) {
        checkTimeBeforeEventStart(request.getEventDate());
        User user = getUser(userId);
        Category category = getCategory(request.getCategory());

        Event event = EventMapper.MAPPER.toEvent(request, category, user);
        return EventMapper.MAPPER.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEventByOwner(Long userId, Long eventId, EventUpdateUserRequest request) {
        checkTimeBeforeEventStart(request.getEventDate());
        checkEvent(eventId);
        checkUser(userId);

        Event updateEvent = getEvent(eventId, userId);

        // Обновление информации о событии
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            updateEvent.setTitle(request.getTitle());
        }
        if (request.getEventDate() != null) {
            updateEvent.setEventDate(request.getEventDate());
        }
        if (StringUtils.hasLength(request.getAnnotation())) {
            updateEvent.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            Category category = getCategory(request.getCategory());
            updateEvent.setCategory(category);
        }
        if (request.getDescription() != null) {
            updateEvent.setDescription(request.getDescription());
        }
        if (request.getLocation() != null) {
            updateEvent.setLocation(LocationMapper.MAPPER.toLocation(request.getLocation()));
        }
        if (request.getParticipantLimit() != null) {
            updateEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            updateEvent.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case SEND_TO_REVIEW:
                    updateEvent.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    updateEvent.setState(EventState.CANCELED);
                    break;
            }
        }

        // Сохранение обновленного события
        return EventMapper.MAPPER.toEventFullDto(eventRepository.save(updateEvent));
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        checkUser(userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<Event> eventPage = eventRepository.findAllByInitiatorId(userId, pageable);
        List<Event> events = eventPage.getContent();

        return EventMapper.MAPPER.toEventShortDtoList(events);
    }

    @Override
    public EventFullDto getEventByUserIdAndEvent(Long userId, Long eventId) {
        checkEvent(eventId);
        checkUser(userId);

        Event event = getEvent(eventId, userId);
        return EventMapper.MAPPER.toEventFullDto(event);
    }

    //Дополнительные методы
    private void checkUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", userId)));
    }

    private void checkEvent(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", eventId)));
    }

    // Приватный метод для проверки времени до начала события
    private void checkTimeBeforeEventStart(LocalDateTime startDate) {
        LocalDateTime munTimePeriod = LocalDateTime.now().plusHours(HOURS_BEFORE_START_EVENT);
        if (startDate.isBefore(munTimePeriod)) {
            throw new BadRequestException("Событие начинается менее чем через " + HOURS_BEFORE_START_EVENT +
                    " часов");
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", userId)));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", categoryId)));
    }

    private Event getEvent(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с событием id: '%s' и инициатором с id= '%s' не найдена", eventId, userId)));
    }
}