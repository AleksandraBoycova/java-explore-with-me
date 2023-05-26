package ru.practicum.compilation;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCompilationDto {

    private Boolean pinned;

    @NotBlank
    private String title;

    private Set<Long> events;
}
