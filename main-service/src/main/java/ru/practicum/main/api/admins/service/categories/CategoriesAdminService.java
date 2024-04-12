package ru.practicum.main.api.admins.service.categories;

import ru.practicum.main.dto.category.CategoryDto;

public interface CategoriesAdminService {
    CategoryDto addCategories(CategoryDto categoryDto);

    boolean deleteCategories(Long catId);

    CategoryDto updateCategories(CategoryDto categoryDto, Long catId);
}
