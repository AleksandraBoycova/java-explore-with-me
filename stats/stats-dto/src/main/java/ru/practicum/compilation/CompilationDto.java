package ru.practicum.compilation;

import lombok.Data;
import ru.practicum.event.EventDto;

import java.util.Set;

@Data
public class CompilationDto {

    private Long id;

    private Boolean pinned;

    private String title;

    private Set<EventDto> events;
}
