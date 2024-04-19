package ru.practicum.main.dto.event;

import lombok.*;
import ru.practicum.main.model.state.RequestStatus;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds;
    private RequestStatus status;
}
