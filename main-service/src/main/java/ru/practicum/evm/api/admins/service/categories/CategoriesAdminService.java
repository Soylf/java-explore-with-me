package ru.practicum.evm.api.admins.service.categories;

import ru.practicum.evm.dto.category.CategoryDto;

public interface CategoriesAdminService {
    CategoryDto addCategories(CategoryDto categoryDto);

    boolean deleteCategories(Long catId);

    CategoryDto updateCategories(CategoryDto categoryDto, Long catId);
}
