package ru.practicum.main.api.privats.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.api.repository.EventRepository;
import ru.practicum.main.api.repository.RequestRepository;
import ru.practicum.main.api.repository.UserRepository;
import ru.practicum.main.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.main.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.main.dto.request.RequestDto;
import ru.practicum.main.error.exception.BadRequestException;
import ru.practicum.main.error.exception.ConflictException;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.RequestMapper;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Request;
import ru.practicum.main.model.User;
import ru.practicum.main.model.state.EventState;
import ru.practicum.main.model.state.RequestStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestPrivateServiceImpl implements RequestPrivateService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        checkUser(userId);
        Event event = getEventAndInitiatorId(eventId, userId);
        // Проверка достижения лимита заявок на участие в событии
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит заявок на участие в событии");
        }

        // Списки подтвержденных и отклоненных запросов
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        // Получение всех запросов по их идентификаторам
        List<Request> requests = requestRepository.findAllById(request.getRequestIds());
        for (Request item : requests) {
            // Обработка запросов только в статусе ожидания
            if (Objects.equals(item.getStatus(), RequestStatus.PENDING)) {
                // Если лимит участников равен нулю, то все запросы сразу подтверждаются
                if (event.getParticipantLimit() == 0) {
                    item.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                    // Если лимит участников больше количества подтвержденных запросов,
                    // то осуществляется проверка на модерацию запроса
                    if (!event.getRequestModeration() || (Objects.equals(request.getStatus(), RequestStatus.CONFIRMED))) {
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

        // Возвращение результата обновления статуса запросов
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

        // Проверка, что пользователь является инициатором события
        if (!eventRepository.existsByInitiatorIdAndId(userId, eventId)) {
            throw new ConflictException("Пользователь не является инициатором события");
        }

        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return RequestMapper.MAPPER.toDtoList(requests);
    }

    @Transactional
    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        checkEvent(eventId);

        User user = getUser(userId);
        Event event = getEvent(eventId);

        // Проверка, что пользователь не является инициатором события
        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new ConflictException(String.format("Пользователь с id='%s' - автор события с id='%s' " +
                    "и не может создать запрос", userId, eventId));
        }

        // Проверка наличия уже существующего запроса на участие в событии от пользователя
        if (!requestRepository.findByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new ConflictException("Нельзя добавить повторный запрос на участие в событии");
        }

        // Проверка, что событие опубликовано
        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя добавить запрос на участие в неопубликованном событии");
        }

        Request request = new Request(null, LocalDateTime.now(), event, user, RequestStatus.PENDING);

        // Получение количества подтвержденных запросов и лимита участников
        int confirmed = event.getConfirmedRequests();
        int limit = event.getParticipantLimit();

        if (limit == 0) {
            event.setConfirmedRequests(confirmed + 1);
            eventRepository.save(event);
            request.setStatus(RequestStatus.CONFIRMED);
        } else if (confirmed < limit) {
            // Если модерация не требуется, запрос сразу подтверждается
            if (!event.getRequestModeration()) {
                event.setConfirmedRequests(confirmed + 1);
                eventRepository.save(event);
                request.setStatus(RequestStatus.PENDING);
            }
        } else {
            throw new ConflictException(String.format("Свободных мест для записи на события с id='%s' нет", eventId));
        }

        Request savedRequest = requestRepository.save(request);
        return RequestMapper.MAPPER.toDto(savedRequest);
    }

    // Метод для получения списка всех запросов пользователя
    @Override
    public List<RequestDto> getAllRequests(Long userId) {
        checkUser(userId);

        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return RequestMapper.MAPPER.toDtoList(requests);
    }

    @Override
    @Transactional
    public RequestDto updateRequest(Long userId, Long requestId) {
        checkUser(userId);
        checkRequest(requestId);

        // Получение запроса по его ID и ID пользователя-реквестора
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(() ->
                new NotFoundException(String.format("Запрос с id='%s' и реквестором с id='%s' не найден",
                        requestId, userId)));

        // Проверка статуса запроса
        if ((Objects.equals(request.getStatus(), RequestStatus.CANCELED))
                || (Objects.equals(request.getStatus(), RequestStatus.REJECTED))) {
            throw new BadRequestException("Запрос уже отменен");
        }

        // Отмена запроса и сохранение изменений
        request.setStatus(RequestStatus.CANCELED);
        Request savedRequest = requestRepository.save(request);

        // Возврат DTO отмененного запроса
        return RequestMapper.MAPPER.toDto(savedRequest);
    }


    //Дополнительные методы

    private void checkUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id: '%s' не найден", userId)));
    }

    private void checkEvent(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id: '%s' не найдено", eventId)));
    }

    private void checkRequest(Long requestId) {
        requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id: '%s' не найден", requestId)));
    }

    private Event getEventAndInitiatorId(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id: '%s' и инициатором с id: '%s' не найдено", eventId, userId)));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id: '%s' не найден", userId)));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id: '%s' не найдено", eventId)));
    }

}