package ru.practicum.compilation;

import lombok.*;

import javax.validation.Valid;
import java.util.Set;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateCompilationRequest {

    private Boolean pinned;

    private String title;

    private Set<Long> events;
}
