package ru.practicum.main.api.admins.service.comment;

import ru.practicum.main.dto.comment.CommentDto;

public interface CommentAdminService {
    void deleteCommentById(Long commentId);

    CommentDto getCommentById(Long commentId);
}
