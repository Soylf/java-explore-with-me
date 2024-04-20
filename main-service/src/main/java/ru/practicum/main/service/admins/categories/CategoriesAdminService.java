package ru.practicum.main.service.admins.categories;

import ru.practicum.main.dto.category.CategoryDto;

public interface CategoriesAdminService {
    CategoryDto addCategories(CategoryDto categoryDto);

    void deleteCategories(Long catId);

    CategoryDto updateCategories(CategoryDto categoryDto, Long catId);
}
