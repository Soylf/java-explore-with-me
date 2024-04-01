package ru.practicum.evm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.evm.dto.category.CategoryDto;
import ru.practicum.evm.dto.user.UserShortDto;

import java.time.LocalDateTime;

public class EventShortDto {
    private Long id;

    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Integer views;
}
