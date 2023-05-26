package ru.yandex.practicum.service;

import ru.practicum.category.CategoryDto;

import java.util.List;

public interface CategoriesService {

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);

    CategoryDto addCategory(CategoryDto category);

    void deleteCategory(Long catId);

    CategoryDto update(CategoryDto category, Long catId);
}
