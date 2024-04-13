package ru.practicum.main.api.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.api.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl {
    private final CommentRepository repository;

}
