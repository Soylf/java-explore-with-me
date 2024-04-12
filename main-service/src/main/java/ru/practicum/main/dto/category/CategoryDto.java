package ru.practicum.main.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @Size(min = 1, max = 50, message = "Имя не может быть пустым и быть длиной более 50 символов")
    @NotBlank(message = "Имя не может быть пустым или отсутствовать")
    private String name;
}
