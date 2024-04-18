package ru.practicum.main.api.admins.service.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.api.repository.CategoryRepository;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.CategoryMapper;
import ru.practicum.main.model.Category;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriesAdminServiceImpl implements CategoriesAdminService {
    private final CategoryRepository repository;

    @Override
    @Transactional
    public CategoryDto addCategories(CategoryDto categoryDto) {
        Category newCategory = repository.save(CategoryMapper.MAPPER.fromDto(categoryDto));
        return CategoryMapper.MAPPER.toDto(newCategory);
    }

    @Override
    @Transactional
    public void deleteCategories(Long catId) {
        // Проверка существования категории перед удалением
        Category category = getCategory(catId);
        repository.delete(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategories(CategoryDto categoryDto, Long catId) {
        // Получение категории по ID
        Category category = getCategory(catId);
        // Обновление имени категории
        category.setName(categoryDto.getName());

        return CategoryMapper.MAPPER.toDto(repository.save(category));
    }

    //Дополнительные методы
    private Category getCategory(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", catId)));
    }
}