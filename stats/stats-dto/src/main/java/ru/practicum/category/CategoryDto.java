package ru.practicum.category;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Valid
public class CategoryDto {

    private Long id;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String name;
}
