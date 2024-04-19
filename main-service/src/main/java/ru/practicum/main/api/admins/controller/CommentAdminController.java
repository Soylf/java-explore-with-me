package ru.practicum.main.api.admins.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.api.admins.service.comment.CommentAdminService;
import ru.practicum.main.dto.comment.CommentDto;

@Slf4j
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentAdminService service;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId) {
        log.info("CommentAdminController: запрос на удаление комментария с id='{}'", commentId);
        service.deleteCommentById(commentId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.info("CommentAdminController: запрос на получение отдельного комита: '{}'", commentId);
        return service.getCommentById(commentId);
    }
}
