package ru.practicum.evm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.evm.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events;

    private Long id;

    private Boolean pinned;

    private String title;
}
