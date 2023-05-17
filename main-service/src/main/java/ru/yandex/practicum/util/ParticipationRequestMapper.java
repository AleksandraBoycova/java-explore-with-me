package ru.yandex.practicum.util;

import ru.practicum.request.EventRequestStatusUpdateResult;
import ru.practicum.request.ParticipationRequestDto;
import ru.yandex.practicum.model.ParticipationRequestEntity;

import java.util.List;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toDto(ParticipationRequestEntity entity) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setCreated(entity.getCreated());
        dto.setEvent(entity.getEvent().getId());
        dto.setRequester(entity.getRequester().getId());
        dto.setStatus(entity.getState());
        dto.setId(entity.getId());
        return dto;
    }

    public static EventRequestStatusUpdateResult toUpdateResult(List<ParticipationRequestDto> con, List<ParticipationRequestDto> rej) {
        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();
        updateResult.setRejectedRequests(rej);
        updateResult.setConfirmedRequests(con);
        return updateResult;
    }
}
