package ru.practicum.evm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.evm.dto.category.CategoryDto;
import ru.practicum.evm.model.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper MAPPER = Mappers.getMapper(CategoryMapper.class);
    CategoryDto toDto(Category category);
    List<CategoryDto> toDtoList(List<Category> categoryDos);
    Category fromDto(CategoryDto categoryDto);

}
