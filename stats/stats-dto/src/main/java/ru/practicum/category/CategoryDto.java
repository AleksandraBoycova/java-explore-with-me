package ru.practicum.category;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class CategoryDto {

    private Long id;
    @NotNull
    private String name;
}
