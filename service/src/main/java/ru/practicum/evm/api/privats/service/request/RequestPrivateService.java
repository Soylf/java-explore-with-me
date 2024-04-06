package ru.practicum.evm.api.privats.service.request;

import ru.practicum.evm.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.evm.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.evm.dto.request.RequestDto;

import java.util.List;

public interface RequestPrivateService {
    EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<RequestDto> getRequestByEvent(Long userId, Long eventId);
    RequestDto addRequest(Long userId, Long eventId);

    List<RequestDto> getAllRequests(Long userId);

    RequestDto updateRequest(Long userId, Long requestId);
}
