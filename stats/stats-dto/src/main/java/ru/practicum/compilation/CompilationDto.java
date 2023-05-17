package ru.practicum.compilation;

import lombok.Data;

@Data
public class CompilationDto {

    private Long id;

    private Boolean pinned;

    private String title;
}
