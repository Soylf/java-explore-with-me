package ru.practicum.evm.api.publics.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.evm.api.repository.CategoryRepository;
import ru.practicum.evm.api.repository.EventRepository;
import ru.practicum.evm.dto.event.EventFullDto;
import ru.practicum.evm.dto.event.EventShortDto;
import ru.practicum.evm.error.exception.BadRequestException;
import ru.practicum.evm.error.exception.NotFoundException;
import ru.practicum.evm.mapper.EventMapper;
import ru.practicum.evm.model.Event;
import ru.practicum.evm.model.state.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventsPublicServiceImpl implements EventsPublicService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventShortDto> getAllEvents(String text, List<Long> categories, Boolean paid,
                                            LocalDateTime startDate, LocalDateTime endDate, Boolean onlyAvailable,
                                            String sort, Integer from, Integer size) {
        checkTime(startDate, endDate);
        checkCategory(categories);

        Pageable pageable = PageRequest.of(from / size, size);
        Specification<Event> specification = Specification.where(null);

        if(text != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                            "%" + text.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                            "%" + text.toLowerCase() + "%")
            ));
        }
        if (!categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }
        LocalDateTime startDateTime = Objects.requireNonNullElse(startDate, LocalDateTime.now());

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("eventDate"), startDateTime));

        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("eventDate"), endDate));
        }

        if (onlyAvailable) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));
        Page<Event> events = eventRepository.findAll(specification, pageable);


        return EventMapper.MAPPER.toEventShortDtoList(events.getContent());
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        return EventMapper.MAPPER.toEventFullDto(event);
    }


    private void checkTime(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date can not after end date");
        }
    }

    private void checkCategory(List<Long> categories) {
        for (Long id : categories) {
            categoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", id)));
        }
    }
}
