package ru.practicum.main.service.publics.categories;

import ru.practicum.main.dto.category.CategoryDto;

import java.util.List;

public interface CategoriesPublicService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long categoryId);
}
