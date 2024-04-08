package ru.practicum.evm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.dto.compilation.CompilationNewDto;
import ru.practicum.evm.model.Compilation;

import java.util.List;

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

    default Compilation fromNewDto(CompilationNewDto dto) {
        return Compilation.builder()
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .events(List.of())
                .build();
    }

    List<CompilationDto> toDtoList(List<Compilation> compilations);
}
