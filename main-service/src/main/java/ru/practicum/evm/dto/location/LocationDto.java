package ru.practicum.evm.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class LocationDto {
    @NotNull(message = "Широта не может быть пустой или отсутствовать")
    private Float lat;

    @NotNull(message = "Долгота не может быть пустой или отсутствовать")
    private Float lon;
}
