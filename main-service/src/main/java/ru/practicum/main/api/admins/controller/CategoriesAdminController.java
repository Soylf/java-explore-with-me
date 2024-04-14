package ru.practicum.main.api.admins.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.api.admins.service.categories.CategoriesAdminService;
import ru.practicum.main.dto.category.CategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoriesAdminController {
    private final CategoriesAdminService service;

    @PostMapping
    public CategoryDto addCategories(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("CategoriesAdminController: запрос на создания категории (CategoryDto)");
        return service.addCategories(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable(name = "catId") @Positive Long categoryId) {
        log.info("CategoriesAdminController: запрос на удалении категории (CategoryDto)");
        service.deleteCategories(categoryId);
    }

    @PostMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable(name = "catId") @Positive Long categoryId,
                                      @RequestBody @Valid CategoryDto request) {
        log.info("CategoriesAdminController: запрос на обновления категории (CategoryDto)");
        return service.updateCategories(request, categoryId);
    }
}
