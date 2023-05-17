package ru.yandex.practicum.util;

import ru.practicum.compilation.CompilationDto;
import ru.yandex.practicum.model.CompilationEntity;

public class CompilationMapper {

    public static CompilationDto toDto(CompilationEntity compilationEntity) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setPinned(compilationEntity.getPinned());
        compilationDto.setTitle(compilationEntity.getTitle());
        compilationDto.setId(compilationEntity.getId());
        return compilationDto;
    }
}
