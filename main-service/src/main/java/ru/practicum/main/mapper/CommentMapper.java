package ru.practicum.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommentMapper {
    CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "author.id", target = "author")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDtoList(List<Comment> comments);

    default Comment fromDto(CommentDto commentDto, Long userId, Long eventId) {
        return Comment.builder()
                .text(commentDto.getText())
                .author(User.builder().id(userId).build())
                .event(Event.builder().id(eventId).build())
                .createdOn(LocalDateTime.now())
                .build();
    }
}
