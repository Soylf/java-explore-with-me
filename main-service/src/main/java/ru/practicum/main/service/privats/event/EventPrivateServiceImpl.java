package ru.practicum.main.service.privats.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.dto.event.EventFullDto;
import ru.practicum.main.dto.event.EventNewDto;
import ru.practicum.main.dto.event.EventShortDto;
import ru.practicum.main.dto.event.EventUpdateUserRequest;
import ru.practicum.main.error.exception.BadRequestException;
import ru.practicum.main.error.exception.ConflictException;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.mapper.LocationMapper;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;
import ru.practicum.main.model.state.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImpl implements EventPrivateService {

    // Константа, определяющая количество часов до начала события
    private static final int HOURS_BEFORE_START_EVENT = 2;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public EventFullDto addEvent(EventNewDto request, Long userId) {
        checkTimeBeforeEventStart(request.getEventDate());
        User user = getUser(userId);
        Category category = getCategory(request.getCategory());

        Event event = EventMapper.MAPPER.toEvent(request, category, user);
        Event newEvent = eventRepository.save(event);
        return EventMapper.MAPPER.toEventFullDto(newEvent);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByOwner(Long userId, Long eventId, EventUpdateUserRequest request) {
        checkUser(userId);

        Event updateEvent = getEvent(eventId, userId);

        if (!Objects.equals(userId, updateEvent.getInitiator().getId())) {
            throw new ConflictException(String.format("Пользователь с id='%s' не автор события с id='%s'",
                    userId, eventId));
        }

        if (Objects.equals(EventState.PUBLISHED, updateEvent.getState())) {
            throw new ConflictException("Статус события - 'PUBLISHED'. " +
                    "Статус события должен быть 'PENDING' or 'CANCELED'");
        }

        // Обновление информации о событии
        if (Objects.nonNull(request.getTitle())) {
            updateEvent.setTitle(request.getTitle());
        }
        if (Objects.nonNull(request.getEventDate())) {
            checkTimeBeforeEventStart(request.getEventDate());
            updateEvent.setEventDate(request.getEventDate());
        }
        if (Objects.nonNull(request.getAnnotation()) && StringUtils.hasLength(request.getAnnotation())) {
            updateEvent.setAnnotation(request.getAnnotation());
        }
        if (Objects.nonNull(request.getCategory())) {
            Category category = getCategory(request.getCategory());
            updateEvent.setCategory(category);
        }
        if (Objects.nonNull(request.getDescription())) {
            updateEvent.setDescription(request.getDescription());
        }
        if (Objects.nonNull(request.getLocation())) {
            updateEvent.setLocation(LocationMapper.MAPPER.toLocation(request.getLocation()));
        }
        if (Objects.nonNull(request.getParticipantLimit())) {
            updateEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (Objects.nonNull(request.getRequestModeration())) {
            updateEvent.setRequestModeration(request.getRequestModeration());
        }
        if (Objects.nonNull(request.getStateAction())) {
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