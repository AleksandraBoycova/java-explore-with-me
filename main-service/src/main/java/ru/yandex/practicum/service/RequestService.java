package ru.yandex.practicum.service;

import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.EventRequestStatusUpdateResult;
import ru.practicum.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequestsInForeignEvents(Long userId);

    List<ParticipationRequestDto> getUserRequestsInEvent(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);

    EventRequestStatusUpdateResult confirmOrRejectRequestsByUser(Long userId, Long eventId, EventRequestStatusUpdateRequest
            request);
}
