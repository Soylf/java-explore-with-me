package ru.practicum.main.api.publics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.api.publics.service.comment.CommentPublicService;
import ru.practicum.main.dto.comment.CommentDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentPublicController {
    private final CommentPublicService service;

    @GetMapping
    public List<CommentDto> getComments(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("CommentPublicController: получен запрос на получение списка из комментариев");
        return service.getComments(from, size);
    }

    @GetMapping("/{eventId}")
    public List<CommentDto> getEventComments(@PathVariable Long eventId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("EventPublicController запрос на получение комментариев к событию с id='{}'", eventId);
        return service.getAllCommentsByEventId(eventId, from, size);
    }
}
