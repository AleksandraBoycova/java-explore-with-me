package ru.yandex.practicum.service;


import ru.practicum.compilation.CompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

}
