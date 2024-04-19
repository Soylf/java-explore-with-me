package ru.practicum.main.api.privats.service.comment;

import ru.practicum.main.dto.comment.CommentDto;

public interface CommentPrivateService {
    CommentDto createComment(Long userId, Long eventId, CommentDto commentDto);

    void deleteCommentById(Long commentId, Long userId);

    CommentDto updateCommentById(Long commentId, Long userId, CommentDto commentDto);
}
