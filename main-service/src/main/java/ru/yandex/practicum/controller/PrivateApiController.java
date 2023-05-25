package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.EventDto;
import ru.practicum.event.NewEventDto;
import ru.practicum.event.UpdateEventRequest;
import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.EventRequestStatusUpdateResult;
import ru.practicum.request.ParticipationRequestDto;
import ru.yandex.practicum.service.EventService;
import ru.yandex.practicum.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("users")
public class PrivateApiController {

    private final RequestService requestService;
    private final EventService eventService;

    @PostMapping("{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto event) {
        log.debug("Private: Вызван метод createEvent, userId {} ", userId);
        return eventService.createEvent(userId, event);
    }

    @GetMapping("{userId}/events")
    public List<EventDto> getEventsByUserWithPage(@PathVariable Long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Private: Вызван метод getEventsByUserWithPage, userId, from, size {} {} {}", userId, from, size);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @GetMapping("{userId}/events/{eventId}")
    public EventDto getEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("Private: Вызван метод getEventByUser, userId, eventId {} {}", userId, eventId);
        return eventService.getEventByUserId(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}")
    public EventDto updateEventByUser(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      @Valid @RequestBody UpdateEventRequest event) {
        log.debug("Private: Вызван метод updateEventByUser, userId eventId {} {}", userId, eventId);
        return eventService.updateEventByUser(userId, eventId, event);
    }

    @GetMapping("{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserRequestsInEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("Private: Вызван метод getUserRequestsInEvent, userId eventId {} {}", userId, eventId);
        return requestService.getUserRequestsInEvent(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult confirmOrRejectRequestsByUser(@PathVariable Long userId, @PathVariable
    Long eventId, @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        log.debug("Private: Вызван метод confirmOrRejectRequestsByUser, userId eventId {} {}", userId, eventId);
        return requestService.confirmOrRejectRequestsByUser(userId, eventId, request);
    }

    @GetMapping("{userId}/requests")
    List<ParticipationRequestDto> getUserRequestsInForeignEvents(@PathVariable Long userId) {
        log.debug("Private: Вызван метод getUserRequestsInForeignEvents, userId {}", userId);
        return requestService.getUserRequestsInForeignEvents(userId);
    }

    @PostMapping("{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable Long userId, @RequestParam(name = "eventId") Long eventId) {
        log.debug("Private: Вызван метод addRequest, userId {}", userId);
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable Long userId, @PathVariable Long requestId) {
        log.debug("Private: Вызван метод cancelRequestByUser, userId requestId {} {}", userId, requestId);
        return requestService.cancelRequestByUser(userId, requestId);
    }
}
