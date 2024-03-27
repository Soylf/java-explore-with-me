package ru.practicum.stat.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stat {
    private String start;
    private String end;
    private String[] uris;
    private boolean unique = false;
}