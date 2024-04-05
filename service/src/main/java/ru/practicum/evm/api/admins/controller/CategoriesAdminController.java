package ru.practicum.evm.api.admins.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.api.admins.service.categories.CategoriesAdminService;
import ru.practicum.evm.dto.category.CategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoriesAdminController {
    private final CategoriesAdminService service;

    @PostMapping
    public CategoryDto addCategories(@Valid @RequestBody CategoryDto categoryDto) {
        return service.addCategories(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public boolean deleteCategories(@PathVariable Long catId) {
        return service.deleteCategories(catId);
    }

    @PostMapping("/{catId}")
    public CategoryDto updateCategories(@Valid @RequestBody CategoryDto categoryDto,
                                        @PathVariable Long catId) {
        return service.updateCategories(categoryDto,catId);
    }
}
