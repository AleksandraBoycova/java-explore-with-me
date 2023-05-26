package ru.practicum.request;

import lombok.*;

import javax.validation.Valid;
import java.util.List;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private String status;
}
