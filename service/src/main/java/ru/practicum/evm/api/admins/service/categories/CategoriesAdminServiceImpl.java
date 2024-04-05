package ru.practicum.evm.api.admins.service.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.evm.api.repository.CategoryRepository;
import ru.practicum.evm.dto.category.CategoryDto;
import ru.practicum.evm.error.exception.NotFoundException;
import ru.practicum.evm.mapper.CategoryMapper;
import ru.practicum.evm.model.Category;

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
    public boolean deleteCategories(Long catId) {
        checkCategory(catId);
        repository.deleteById(catId);
        return true;
    }

    @Override
    public CategoryDto updateCategories(CategoryDto categoryDto, Long catId) {
        try {
            Category newCategory = getCategory(catId);
            if (categoryDto.getName() != null && !categoryDto.getName().isBlank()) {
                newCategory.setName(categoryDto.getName());
            }
            return CategoryMapper.MAPPER.toDto(newCategory);
        }catch (DataIntegrityViolationException e) {
            throw new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", catId));
        }
    }

    private void checkCategory(Long catId) {
        repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", catId)));
    }
    private Category getCategory(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Что-то пошло не так с id: '%s' не найдена", catId)));
    }
}
