package ru.yandex.practicum.util;

import ru.practicum.compilation.CompilationDto;
import ru.practicum.compilation.NewCompilationDto;
import ru.yandex.practicum.model.CompilationEntity;

import java.util.HashSet;
import java.util.stream.Collectors;

public class CompilationMapper {
    private final EventMapper eventMapper = new EventMapper();

    public CompilationDto toDto(CompilationEntity compilationEntity) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setPinned(compilationEntity.getPinned());
        compilationDto.setTitle(compilationEntity.getTitle());
        compilationDto.setId(compilationEntity.getId());
        compilationDto.setEvents(compilationEntity.getEvents() == null
                ? new HashSet<>()
                : compilationEntity.getEvents().stream().map(eventMapper::toShortDto).collect(Collectors.toSet()));
        return compilationDto;
    }

    public CompilationEntity toEntity(NewCompilationDto dto) {
        CompilationEntity entity = new CompilationEntity();
        entity.setPinned(dto.getPinned() != null && dto.getPinned());
        entity.setTitle(dto.getTitle());
        return entity;
    }

}
