package ru.practicum.evm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.evm.model.state.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RequestDto {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private Long event;

    private Long requester;

    private RequestStatus status;
}
