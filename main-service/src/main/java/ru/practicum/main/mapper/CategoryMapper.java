package ru.practicum.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.model.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper MAPPER = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);

    List<CategoryDto> toDtoList(List<Category> categoryDos);

    Category fromDto(CategoryDto categoryDto);

}