package ru.practicum.main.api.publics.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.api.repository.CommentRepository;
import ru.practicum.main.api.repository.EventRepository;
import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.CommentMapper;
import ru.practicum.main.model.Comment;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentPublicServiceImpl implements CommentPublicService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> getComments(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Comment> comments = commentRepository.findAll(pageable).getContent();

        return CommentMapper.MAPPER.toDtoList(comments);
    }

    public List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size) {
        checkExistEvent(eventId);

        PageRequest pageRequest = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId, pageRequest).getContent();

        return CommentMapper.MAPPER.toDtoList(comments);
    }

    //Дополнительные методы
    private void checkExistEvent(Long eventId) {
        eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Событие с id='%s' не найден", eventId)));
    }
}
