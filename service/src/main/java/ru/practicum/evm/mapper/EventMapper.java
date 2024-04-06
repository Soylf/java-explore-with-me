package ru.practicum.evm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.evm.dto.event.EventFullDto;
import ru.practicum.evm.dto.event.EventNewDto;
import ru.practicum.evm.dto.event.EventShortDto;
import ru.practicum.evm.model.Category;

import ru.practicum.evm.model.Event;
import ru.practicum.evm.model.Location;
import ru.practicum.evm.model.User;
import ru.practicum.evm.model.state.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EventMapper {
    EventMapper MAPPER = Mappers.getMapper(EventMapper.class);


     default Event toEvent(EventNewDto eventNewDto, Category category, User initiator) {
        return Event.builder()
                .annotation(eventNewDto.getAnnotation())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .description(eventNewDto.getDescription())
                .eventDate(eventNewDto.getEventDate())
                .initiator(initiator)
                .location(Location.builder()
                        .lat(eventNewDto.getLocation().getLat())
                        .lon(eventNewDto.getLocation().getLon())
                        .build())
                .paid(eventNewDto.getPaid())
                .participantLimit(eventNewDto.getParticipantLimit())
                .requestModeration(eventNewDto.getRequestModeration())
                .title(eventNewDto.getTitle())
                .state(EventState.PENDING)
                .publishedOn(LocalDateTime.now())
                .views(0)
                .build();
    }

    EventShortDto toEventShortDto(Event event);

    EventFullDto toEventFullDto(Event event);

    List<EventShortDto> toEventShortDtoList(List<Event> events);

    List<EventFullDto> toEventFullDtoList(List<Event> events);
}