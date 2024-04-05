package ru.practicum.evm.api.privats.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.api.repository.EventRepository;
import ru.practicum.evm.api.repository.RequestRepository;
import ru.practicum.evm.api.repository.UserRepository;
import ru.practicum.evm.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.evm.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.evm.dto.request.RequestDto;
import ru.practicum.evm.error.exception.ConflictException;
import ru.practicum.evm.error.exception.NotFoundException;
import ru.practicum.evm.mapper.RequestMapper;
import ru.practicum.evm.model.Event;
import ru.practicum.evm.model.Request;
import ru.practicum.evm.model.state.RequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestPrivateServiceImpl implements RequestPrivateService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    @Override
    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        checkUser(userId);
        Event event = getEvent(eventId,userId);

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит заявок на участие в событии");
        }

        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        List<Request> requests = requestRepository.findAllById(request.getRequestIds());
        for (Request item : requests) {
            if (Objects.equals(item.getStatus(), RequestStatus.PENDING)) {
                if (event.getParticipantLimit() == 0) {
                    item.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);

                } else if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                    if (!event.getRequestModeration() ||
                            (Objects.equals(request.getStatus(), RequestStatus.CONFIRMED))) {
                        item.setStatus(RequestStatus.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        confirmed.add(item);
                    } else {
                        item.setStatus(RequestStatus.REJECTED);
                        rejected.add(item);
                    }
                } else {
                    throw new ConflictException("Статус можно изменить только у заявок в статусе WAITING");
                }

            }
        }
        eventRepository.save(event);

        return new EventRequestStatusUpdateResult(confirmed.stream()
                .map(RequestMapper.MAPPER::toDto)
                .collect(Collectors.toList()),
                rejected.stream()
                        .map(RequestMapper.MAPPER::toDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public List<RequestDto> getRequestByEvent(Long userId, Long eventId) {
        checkUser(userId);

        if (!eventRepository.existsByInitiatorIdAndId(userId, eventId)) {
            throw new ConflictException("Пользователь не инициатор события");
        }

        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return RequestMapper.MAPPER.toDtoList(requests);
    }


    private void checkUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", userId)));
    }

    private Event getEvent(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с событием id: '%s' и инициатором с id= '%s' не найдена", eventId, userId)));
    }
}
