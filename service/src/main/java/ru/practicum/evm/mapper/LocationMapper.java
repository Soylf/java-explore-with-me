package ru.practicum.evm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.evm.dto.location.LocationDto;
import ru.practicum.evm.model.Location;

@Mapper
public interface LocationMapper {
    LocationMapper MAPPER = Mappers.getMapper(LocationMapper.class);

    LocationDto toLocationDto(Location location);

    Location toLocation(LocationDto locationDto);
}