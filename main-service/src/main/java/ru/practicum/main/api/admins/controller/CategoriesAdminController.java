package ru.practicum.main.api.admins.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.api.admins.service.categories.CategoriesAdminService;
import ru.practicum.main.dto.category.CategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoriesAdminController {
    private final CategoriesAdminService service;

    @PostMapping
    public CategoryDto addCategories(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("CategoriesAdminController: запрос на создания категории (CategoryDto)");
        return service.addCategories(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public boolean deleteCategories(@PathVariable Long catId) {
        log.info("CategoriesAdminController: запрос на удалении категории (CategoryDto)");
        return service.deleteCategories(catId);
    }

    @PostMapping("/{catId}")
    public CategoryDto updateCategories(@Valid @RequestBody CategoryDto categoryDto,
                                        @PathVariable Long catId) {
        log.info("CategoriesAdminController: запрос на обновления категории (CategoryDto)");
        return service.updateCategories(categoryDto, catId);
    }
}
