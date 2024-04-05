package ru.practicum.evm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.dto.compilation.CompilationNewDto;
import ru.practicum.evm.dto.compilation.CompilationUpdateRequest;
import ru.practicum.evm.model.Compilation;

import java.util.List;

@Mapper
public interface CompilationMapper {
    CompilationMapper MAPPER = Mappers.getMapper(CompilationMapper.class);
    CompilationDto toDto(Compilation compilation);
    Compilation fromNewDto(CompilationNewDto dto);
    Compilation fromUpdateRequest(CompilationUpdateRequest dto);
    List<CompilationDto> toDtoList(List<Compilation> compilations);
}
