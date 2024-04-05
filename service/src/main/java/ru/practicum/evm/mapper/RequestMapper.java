package ru.practicum.evm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.evm.dto.request.RequestDto;
import ru.practicum.evm.model.Request;

import java.util.List;

@Mapper
public interface RequestMapper {
    RequestMapper MAPPER = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "event", source = "request.event.id")
    @Mapping(target = "requester", source = "request.requester.id")
    RequestDto toDto(Request request);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "requester", ignore = true)
    Request toObject(RequestDto dto);

    List<RequestDto> toDtoList(List<Request> requests);
}