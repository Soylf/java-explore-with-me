package ru.practicum.evm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationUpdateRequest {
    private Long id;

    private Set<Long> events;

    private Boolean pinned;

    @Size(min = 1, max = 50, message = "Длина названия должна быть больше 1 и меньше 50 символов")
    private String title;

}
