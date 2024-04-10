package ru.practicum.evm.api.publics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.api.publics.service.categories.CategoriesPublicService;
import ru.practicum.evm.dto.category.CategoryDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesPublicController {
    private final CategoriesPublicService service;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("CategoryPublicController: Запрос на получение всех категорий");
        return service.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") @Positive Long categoryId) {
        log.info("CategoryPublicController: Запрос на получение категории с id='{}'", categoryId);
        return service.getCategoryById(categoryId);
    }
}