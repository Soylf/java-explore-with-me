package ru.practicum.stat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @PositiveOrZero
    private long hits;
}