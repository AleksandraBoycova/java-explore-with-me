package ru.yandex.practicum.service;

import ru.practicum.event.EventDto;
import ru.practicum.event.NewEventDto;
import ru.practicum.event.UpdateEventRequest;
import ru.yandex.practicum.util.EventsSortedBy;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<EventDto> getEvents(String text, List<Long> categoriesIds, Boolean paid, String rangeStart,
                             String rangeEnd, Boolean onlyAvailable, EventsSortedBy sortedBy, Integer from, Integer size,
                             HttpServletRequest request);

    EventDto getEventByUserId(Long userId, Long eventId);

    EventDto createEvent(Long userId, NewEventDto event);

    EventDto getEventById(Long eventId, HttpServletRequest request);

    EventDto updateEvent(Long eventId, UpdateEventRequest event);

    List<EventDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest event);

    List<EventDto> searchEvents(List<Long> userIds, List<String> states, List<Long> categories, String rangeStart,
                                String rangeEnd, Integer from, Integer size, HttpServletRequest request);
}
