package ru.practicum.main.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
