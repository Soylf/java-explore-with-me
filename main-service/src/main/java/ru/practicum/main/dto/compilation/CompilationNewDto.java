package ru.practicum.main.dto.compilation;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationNewDto {
    private Set<Long> events;

    private Boolean pinned;

    @Size(min = 1, max = 50, message = "Длина названия должна быть больше 1 и меньше 50 символов")
    @NotBlank(message = "Название не может быть пустым или отсутствовать")
    private String title;
}

