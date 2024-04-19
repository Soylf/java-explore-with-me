package ru.practicum.main.dto.location;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotNull(message = "Широта не может быть пустой или отсутствовать")
    private Float lat;

    @NotNull(message = "Долгота не может быть пустой или отсутствовать")
    private Float lon;
}
