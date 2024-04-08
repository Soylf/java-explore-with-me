package ru.practicum.evm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.evm.dto.location.LocationDto;
import ru.practicum.evm.model.Location;

@Mapper
public interface LocationMapper {
    LocationMapper MAPPER = Mappers.getMapper(LocationMapper.class);

    LocationDto toLocationDto(Location location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lat", source = "lat")
    Location toLocation(LocationDto locationDto);
}