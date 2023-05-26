package ru.practicum.event;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {

    @NotNull
    private Float lat;

    @NotNull
    private Float lon;
}
