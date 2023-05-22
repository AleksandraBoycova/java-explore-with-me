package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.CategoryDto;
import ru.yandex.practicum.exception.AlreadyExistsException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.RequestDeniedException;
import ru.yandex.practicum.model.CategoryEntity;
import ru.yandex.practicum.model.EventEntity;
import ru.yandex.practicum.repository.CategoryRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.service.CategoriesService;
import ru.yandex.practicum.util.CategoryMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoriesService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        if (size != 0) {
            final PageRequest pageRequest = PageRequest.of(from / size, size);
            List<CategoryEntity> categories = categoryRepository.findAll(pageRequest).getContent();
            return categories.stream().map(CategoryMapper::toDto).collect(Collectors.toList());
        }
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        CategoryEntity category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Такой категории нет " + catId));
        return CategoryMapper.toDto(category);
    }

    @Transactional
    @Override
    public CategoryDto addCategory(CategoryDto category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistsException("Категория с таким именем уже существует: " + category.getName());
        }
        CategoryEntity savedCategory = categoryRepository.save(CategoryMapper.toEntity(category));
        return CategoryMapper.toDto(savedCategory);
    }

    @Transactional
    @Override
    public void deleteCategory(Long catId) {
        Optional<List<EventEntity>> eventsWithSameCat = eventRepository.findAllByCategoryId(catId);
        if (eventsWithSameCat.get().size() != 0) {
            throw new RequestDeniedException("Нельзя удалить категорию, если с ней связаны события " + catId);
        }
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Такой категории нет " + catId);
        }
        categoryRepository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto update(CategoryDto category, Long catId) {
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new RuntimeException("Название категории не может быть пустым");
        }
        CategoryEntity savedCategory = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Такой категории нет " + catId));
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistsException("Категория с таким именем уже существует: " + category.getName());
        } else {
            savedCategory.setName(category.getName());
            return CategoryMapper.toDto(categoryRepository.save(savedCategory));
        }
    }
}
