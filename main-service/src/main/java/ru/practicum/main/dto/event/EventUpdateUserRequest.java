package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main.dto.location.LocationDto;
import ru.practicum.main.model.state.EventStateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class EventUpdateUserRequest {
    @Size(min = 20, max = 2000, message = "Длина краткого описания должна быть больше 3 и меньше 120 символов")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Длина описания должна быть больше 3 и меньше 120 символов")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    @Size(min = 3, max = 120, message = "Длина названия должна быть больше 3 и меньше 120 символов")
    private String title;
}
