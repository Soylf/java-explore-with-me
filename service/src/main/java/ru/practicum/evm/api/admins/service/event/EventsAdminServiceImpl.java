package ru.practicum.evm.api.admins.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.evm.api.repository.CategoryRepository;
import ru.practicum.evm.api.repository.EventRepository;
import ru.practicum.evm.dto.event.EventFullDto;
import ru.practicum.evm.dto.event.EventUpdateAdminRequest;
import ru.practicum.evm.error.exception.BadRequestException;
import ru.practicum.evm.error.exception.ConflictException;
import ru.practicum.evm.error.exception.NotFoundException;
import ru.practicum.evm.mapper.EventMapper;
import ru.practicum.evm.mapper.LocationMapper;
import ru.practicum.evm.model.Category;
import ru.practicum.evm.model.Event;
import ru.practicum.evm.model.state.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventsAdminServiceImpl implements EventsAdminService {

    // Константа, определяющая количество часов до начала события
    private static final Integer HOURS_BEFORE_START_EVENT = 1;

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventFullDto> getAllEvents(List<Long> users, List<String> states, List<Long> categories,
                                           LocalDateTime startDate, LocalDateTime endDate, Integer from, Integer size) {
        checkEndIsAfterStart(startDate, endDate);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));

        Specification<Event> specification = Specification.where(null);
        // Фильтрация по пользователям
        if (users != null && !users.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(users));
        }
        // Фильтрация по состояниям
        if (states != null && !states.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("state").as(String.class).in(states));
        }
        // Фильтрация по категориям
        if (categories != null && !categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }
        // Фильтрация по начальной дате
        if (startDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startDate));
        }
        // Фильтрация по конечной дате
        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endDate));
        }

        Page<Event> events = eventRepository.findAll(specification, pageable);
        return EventMapper.MAPPER.toEventFullDtoList(events.getContent());
    }

    @Override
    public EventFullDto updateEventById(Long eventId, EventUpdateAdminRequest request) {
        Event foundEvent = getEventByIdIfExist(eventId);

        // Проверка, что статус события равен 'PENDING'
        if (!Objects.equals(EventState.PENDING, foundEvent.getState())) {
            throw new ConflictException("Статус события должен быть 'PENDING'");
        }

        // Обновление аннотации, если указана
        if (request.getAnnotation() != null && StringUtils.hasLength(request.getAnnotation())) {
            foundEvent.setAnnotation(request.getAnnotation());
        }
        // Обновление заголовка, если указан
        if (request.getTitle() != null && StringUtils.hasLength(request.getTitle())) {
            foundEvent.setTitle(request.getTitle());
        }
        // Обновление категории, если указана
        if (request.getCategory() != null) {
            final Category category = getCategory(request.getCategory());
            foundEvent.setCategory(category);
        }
        // Обновление описания, если указано
        if (request.getDescription() != null && StringUtils.hasLength(request.getDescription())) {
            foundEvent.setDescription(request.getDescription());
        }
        // Обновление даты события, если указана
        if (request.getEventDate() != null) {
            checkTime(request.getEventDate());
            foundEvent.setEventDate(request.getEventDate());
        }
        // Обновление местоположения, если указано
        if (request.getLocation() != null) {
            foundEvent.setLocation(LocationMapper.MAPPER.toLocation(request.getLocation()));
        }
        // Обновление информации о платности, если указано
        if (request.getPaid() != null) {
            foundEvent.setPaid(request.getPaid());
        }
        // Обновление информации о лимите участников, если указано
        if (request.getParticipantLimit() != null) {
            foundEvent.setParticipantLimit(request.getParticipantLimit());
        }
        // Обновление информации о необходимости модерации, если указано
        if (request.getRequestModeration() != null) {
            foundEvent.setRequestModeration(request.getRequestModeration());
        }
        // Обновление статуса события, если указано
        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case PUBLISH_EVENT:
                    foundEvent.setState(EventState.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    foundEvent.setState(EventState.CANCELED);
                    break;
            }
        }

        // Сохранение обновленного события
        Event updatedEvent = eventRepository.save(foundEvent);
        return EventMapper.MAPPER.toEventFullDto(updatedEvent);
    }

    //Дополнительные методы
    private Event getEventByIdIfExist(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Событие с id='%s' не найдено", eventId)));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", categoryId)));
    }

    // Приватный метод для проверки корректности дат начала и окончания
    private void checkEndIsAfterStart(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BadRequestException("Дата начала не может быть позже даты конца");
        }
    }

    private void checkTime(LocalDateTime startDate) {
        LocalDateTime minStartDate = LocalDateTime.now().plusHours(HOURS_BEFORE_START_EVENT);
        if (startDate.isBefore(minStartDate)) {
            throw new BadRequestException("До начала события менее " + HOURS_BEFORE_START_EVENT + " часов");
        }
    }
}