package ru.practicum.evm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.dto.compilation.CompilationNewDto;
import ru.practicum.evm.dto.compilation.CompilationUpdateRequest;
import ru.practicum.evm.model.Compilation;
import ru.practicum.evm.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mapper
public interface CompilationMapper {
    CompilationMapper MAPPER = Mappers.getMapper(CompilationMapper.class);

    default CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(EventMapper.MAPPER.toEventShortDtoList(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
    Compilation fromNewDto(CompilationNewDto dto);
    Compilation fromUpdateRequest(CompilationUpdateRequest dto);
    List<CompilationDto> toDtoList(List<Compilation> compilations);

    default List<Event> mapEventIdsToEvents(Set<Long> eventIds) {
        List<Event> events = new ArrayList<>();
        for (Long eventId : eventIds) {
            Event event = new Event();
            event.setId(eventId);
            events.add(event);
        }
        return events;
    }
}
