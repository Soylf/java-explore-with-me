package ru.practicum.evm.dto.compilation;

import javax.validation.constraints.Size;
import java.util.Set;

public class CompilationUpdateRequest {
    private Long id;

    private Set<Long> events;

    private Boolean pinned;

    @Size(min = 1, max = 50, message = "Длина названия должна быть больше 1 и меньше 50 символов")
    private String title;

}
