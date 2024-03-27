package ru.practicum.stat.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
    private String app;
    private String uri;
    private boolean unique;
    private int hits;
}
