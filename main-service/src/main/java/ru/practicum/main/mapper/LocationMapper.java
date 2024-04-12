package ru.practicum.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.main.dto.location.LocationDto;
import ru.practicum.main.model.Location;

@Mapper
public interface LocationMapper {
    LocationMapper MAPPER = Mappers.getMapper(LocationMapper.class);

    LocationDto toLocationDto(Location location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lat", source = "lat")
    Location toLocation(LocationDto locationDto);
}