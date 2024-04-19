package ru.practicum.main.dto.compilation;

import lombok.*;
import ru.practicum.main.dto.event.EventShortDto;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events;

    private Long id;

    private Boolean pinned;

    private String title;
}
