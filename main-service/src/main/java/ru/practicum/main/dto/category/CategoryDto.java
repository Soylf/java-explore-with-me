package ru.practicum.main.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @Size(min = 1, max = 50, message = "Имя не может быть пустым и быть длиной более 50 символов")
    @NotBlank(message = "Имя не может быть пустым или отсутствовать")
    private String name;
}
