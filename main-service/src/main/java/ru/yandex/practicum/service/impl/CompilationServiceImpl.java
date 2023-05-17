package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.CompilationDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.CompilationEntity;
import ru.yandex.practicum.repository.CompilationRepository;
import ru.yandex.practicum.service.CompilationService;
import ru.yandex.practicum.util.CompilationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<CompilationEntity> compilationEntities = compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size));
        return compilationEntities.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long id) {
        CompilationEntity compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такой подборки нет " + id));
        return CompilationMapper.toDto(compilation);
    }
}
