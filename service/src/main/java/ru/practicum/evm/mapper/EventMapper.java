package ru.practicum.evm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.evm.dto.event.EventFullDto;
import ru.practicum.evm.dto.event.EventNewDto;
import ru.practicum.evm.dto.event.EventShortDto;
import ru.practicum.evm.model.Category;
import ru.practicum.evm.model.Event;
import ru.practicum.evm.model.User;

import java.util.List;

@Mapper
public interface EventMapper {
    EventMapper MAPPER = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    Event toEvent(EventNewDto eventNewDto, Category category, User initiator);

    EventShortDto toEventShortDto(Event event);

    EventFullDto toEventFullDto(Event event);

    List<EventShortDto> toEventShortDtoList(List<Event> events);

    List<EventFullDto> toEventFullDtoList(List<Event> events);
}