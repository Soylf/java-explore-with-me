package ru.practicum.evm.api.publics.service.categories;

import ru.practicum.evm.dto.category.CategoryDto;

import java.util.List;

public interface CategoriesPublicService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long categoryId);
}
