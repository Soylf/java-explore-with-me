package ru.practicum.main.api.admins.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.api.repository.CommentRepository;
import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.CommentMapper;
import ru.practicum.main.model.Comment;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentRepository repository;

    @Override
    @Transactional
    public void deleteCommentById(Long commentId) {
        checkComment(commentId);
        repository.deleteById(commentId);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        return CommentMapper.MAPPER.toDto(getComment(commentId));
    }

    //Дополнительные методы
    private Comment getComment(Long commentId) {
        return repository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("комментарий в базе с id='%s' не найдено", commentId)));
    }

    private void checkComment(Long commentId) {
        repository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("комментарий в базе с id='%s' не найдено", commentId)));
    }
}
