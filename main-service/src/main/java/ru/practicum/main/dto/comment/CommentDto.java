package ru.practicum.main.dto.comment;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private Long eventId;
    private Long author;

    @Size(min = 10, max = 2000, message = "Минимальная длина комментария 10 символов, максимальная - 2000")
    @NotBlank(message = "Комментарий не может быть пустым или отсутствовать")
    private String text;
}