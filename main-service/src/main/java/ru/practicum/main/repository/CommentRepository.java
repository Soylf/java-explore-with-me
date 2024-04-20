package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByEventIdAndAuthorId(Long eventId, Long userId);

    Page<Comment> findAllByEventIdOrderByCreatedOnDesc(Long eventId, PageRequest pageRequest);
}
