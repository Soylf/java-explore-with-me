package ru.practicum.main.api.privats.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.main.api.repository.CommentRepository;
import ru.practicum.main.api.repository.EventRepository;
import ru.practicum.main.api.repository.RequestRepository;
import ru.practicum.main.api.repository.UserRepository;
import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.error.exception.AccessException;
import ru.practicum.main.error.exception.ConflictException;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.CommentMapper;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Request;
import ru.practicum.main.model.User;
import ru.practicum.main.model.state.EventState;
import ru.practicum.main.model.state.RequestStatus;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentPrivateServiceImpl implements CommentPrivateService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);

        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new ConflictException("Статус события должен быть 'PUBLISHED'");
        }

        Optional<Request> optionalRequest = requestRepository.findByIdAndRequesterId(userId, eventId);

        if (!Objects.equals(user.getId(), event.getInitiator().getId()) && (optionalRequest.isEmpty()
                || (!Objects.equals(optionalRequest.get().getStatus(), RequestStatus.CONFIRMED)))) {
            throw new AccessException(String.format("Пользователь с id='%s' не участвовал в событии с id='%s' " +
                    "и не может оставить комментарий", userId, eventId));
        }


        Optional<Comment> foundComment = commentRepository.findByEventIdAndAuthorId(eventId, userId);
        if (foundComment.isPresent()) {
            throw new AccessException(String.format("Пользователь с id='%s' уже оставлял комментарий к событию " +
                    "с id='%s'", userId, eventId));
        }

        Comment comment = CommentMapper.MAPPER.fromDto(commentDto, userId, eventId);

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.MAPPER.toDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteCommentById(Long commentId, Long userId) {
        checkComment(commentId);
        checkUser(userId);

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDto updateCommentById(Long commentId, Long userId, CommentDto commentDto) {
        Comment foundComment = getCommentById(commentId);

        checkUserIsAuthorComment(foundComment.getAuthor().getId(), userId, commentId);

        String newText = commentDto.getText();
        if (StringUtils.hasLength(newText)) {
            foundComment.setText(newText);
        }

        Comment savedComment = commentRepository.save(foundComment);
        return CommentMapper.MAPPER.toDto(savedComment);
    }

    //Дополнительные методы
    private void checkUserIsAuthorComment(Long authorId, Long userId, Long commentId) {
        if (!Objects.equals(authorId, userId)) {
            throw new AccessException(
                    String.format("Пользователь с id='%s' не является автором комментария с id='%s' и не может его удалить / изменить",
                            userId, commentId));
        }
    }

    private void checkComment(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Комментарий с id='%s' не найден", commentId)));
    }

    private void checkUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("пользовтаель с id='%s' не найден", userId)));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Комментарий с id='%s' не найден", commentId)));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("пользовтаель с id='%s' не найден", userId)));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("событие с id='%s' не найден", eventId)));
    }
}