package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.dto.location.LocationDto;
import ru.practicum.main.model.state.EventStateAction;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
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
    @Positive
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    @Size(min = 3, max = 120, message = "Длина названия должна быть больше 3 и меньше 120 символов")
    private String title;
}
