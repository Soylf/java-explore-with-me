package ru.practicum.main.api.admins.service.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.api.repository.CategoryRepository;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.error.exception.NotFoundException;
import ru.practicum.main.mapper.CategoryMapper;
import ru.practicum.main.model.Category;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriesAdminServiceImpl implements CategoriesAdminService {
    private final CategoryRepository repository;

    @Override
    public CategoryDto addCategories(CategoryDto categoryDto) {
        Category newCategory = repository.save(CategoryMapper.MAPPER.fromDto(categoryDto));
        return CategoryMapper.MAPPER.toDto(newCategory);
    }

    @Override
    public void deleteCategories(Long catId) {
        // Проверка существования категории перед удалением
        Category category = getCategory(catId);
        repository.delete(category);
    }

    @Transactional
    @Override
    public CategoryDto updateCategories(CategoryDto categoryDto, Long catId) {
            // Получение категории по ID
            Category category = getCategory(catId);
            // Обновление имени категории
            category.setName(categoryDto.getName());

            return CategoryMapper.MAPPER.toDto(category);
    }

    //Дополнительыне методы
    private Category getCategory(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", catId)));
    }
}
