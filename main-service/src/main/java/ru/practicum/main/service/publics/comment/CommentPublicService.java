package ru.practicum.main.service.publics.comment;

import ru.practicum.main.dto.comment.CommentDto;

import java.util.List;

public interface CommentPublicService {
    List<CommentDto> getComments(Integer from, Integer size);

    List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size);
}
