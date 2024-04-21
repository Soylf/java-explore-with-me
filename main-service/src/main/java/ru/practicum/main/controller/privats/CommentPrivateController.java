package ru.practicum.main.controller.privats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.privats.comment.CommentPrivateService;
import ru.practicum.main.dto.comment.CommentDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {
    private final CommentPrivateService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestParam Long eventId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("CommentUserController: запрос пользователя с id='{}' на создание комментария {} к событию с id='{}'",
                userId, commentDto, eventId);
        return service.createComment(userId, eventId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.info("CommentUserController: запрос пользователя с id='{}' на удаление комментария с id='{}'",
                userId, commentId);
        service.deleteCommentById(commentId, userId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("CommentUserController: запрос пользователя с id='{}' на изменение комментария с id='{}', " +
                "новый комментарий: {}", userId, commentId, commentDto);
        return service.updateCommentById(commentId, userId, commentDto);
    }
}
