package ru.yandex.practicum.util;

import ru.practicum.compilation.CompilationDto;
import ru.practicum.compilation.NewCompilationDto;
import ru.yandex.practicum.model.CompilationEntity;

import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDto toDto(CompilationEntity compilationEntity) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setPinned(compilationEntity.getPinned());
        compilationDto.setTitle(compilationEntity.getTitle());
        compilationDto.setId(compilationEntity.getId());
        compilationDto.setEvents(compilationEntity.getEvents().stream().map(EventMapper::toShortDto).collect(Collectors.toSet()));
        return compilationDto;
    }
    public static CompilationEntity toEntity(NewCompilationDto dto) {
        CompilationEntity entity = new CompilationEntity();
        entity.setPinned(dto.getPinned());
        entity.setTitle(dto.getTitle());
        return entity;
    }

}
