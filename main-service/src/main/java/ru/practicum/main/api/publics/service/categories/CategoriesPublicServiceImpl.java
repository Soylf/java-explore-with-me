package ru.practicum.main.api.publics.service.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.api.repository.CategoryRepository;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.CategoryMapper;
import ru.practicum.main.model.Category;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesPublicServiceImpl implements CategoriesPublicService {
    private final CategoryRepository repository;

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Page<Category> categories = repository.findAll(PageRequest.of(from, size));

        return CategoryMapper.MAPPER.toDtoList(categories.getContent());
    }

    public CategoryDto getCategoryById(Long categoryId) {
        Category category = getCategory(categoryId);

        return CategoryMapper.MAPPER.toDto(category);
    }

    //Дополнительные методы
    private Category getCategory(Long categoryId) {
        return repository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Категория с id='%s' не найдена", categoryId)));
    }
}
