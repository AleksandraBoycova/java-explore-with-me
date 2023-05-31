package ru.practicum.comment;


import lombok.Data;
import ru.practicum.event.EventDto;
import ru.practicum.user.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Valid
@Data
public class CommentDto {

    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 1024)
    private String text;

    private UserDto author;

    private EventDto event;

    private LocalDateTime createdOn;
}
