package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StateClient;
import ru.practicum.event.EventDto;
import ru.practicum.event.NewEventDto;
import ru.practicum.event.State;
import ru.practicum.event.UpdateEventRequest;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.RequestDeniedException;
import ru.yandex.practicum.model.CategoryEntity;
import ru.yandex.practicum.model.EventEntity;
import ru.yandex.practicum.model.UserEntity;
import ru.yandex.practicum.repository.CategoryRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.repository.LocationRepository;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.service.EventService;
import ru.yandex.practicum.util.EventMapper;
import ru.yandex.practicum.util.EventsSortedBy;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.stat.Constants.dateTimeFormatter;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StateClient stateClient;

    @Override
    public List<EventDto> getEvents(String text, List<Long> categoriesIds, Boolean paid, String rangeStart,
                                    String rangeEnd, Boolean onlyAvailable, EventsSortedBy sortedBy, Integer from,
                                    Integer size, HttpServletRequest request) {

        LocalDateTime start = null;
        LocalDateTime end;

        if (rangeStart == null) {
            start = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            end = LocalDateTime.now();
        } else {
            start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
            end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        }
        if (text != null) {
            text = text.toLowerCase();
        }

        final PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(EventsSortedBy.EVENT_DATE.equals(sortedBy) ? "start" : "view"));
        List<EventEntity> eventEntities = eventRepository.searchPublishedEvents(text, categoriesIds, paid, start, end,
                pageRequest).getContent();

        if (eventEntities.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> eventIds = eventEntities.stream().map(EventEntity::getId).collect(Collectors.toSet());
//        Map<Long, Long> viewStatsMap = stateClient.getSetViewsByEventId(eventIds);

        List<EventDto> events = eventEntities.stream().map(EventMapper::toPublicApiDto).collect(Collectors.toList());
//        events.forEach(eventFullDto ->
//                eventFullDto.setViews(viewStatsMap.getOrDefault(eventFullDto.getId(), 0L)));
        return events;
    }


    @Override
    public EventDto getEventById(Long eventId, HttpServletRequest request) {
        EventEntity event = eventRepository.findEventByIdAndStatePublished(eventId)
                .orElseThrow(() -> new NotFoundException("Такого события нет " + eventId));

        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());

        Long views = stateClient.getStatisticsByEventId(eventId);

        EventDto eventDto = EventMapper.toPublicApiDto(event);
        eventDto.setViews(views);

        stateClient.saveHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return eventDto;

    }

    @Transactional
    @Override
    public EventDto updateEvent(Long eventId, UpdateEventRequest event) {
        EventEntity eventToUpdate = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Такого события нет " + eventId));
        if (event.getEventDate() != null) {
            validateTime(event.getEventDate());
        }
        if (event.getStateAction() != null) {
            if (event.getStateAction().equals("PUBLISH_EVENT")) {
                if (eventToUpdate.getState().equals(State.PENDING)) {
                    eventToUpdate.setState(State.PUBLISHED);
                    eventToUpdate.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new RequestDeniedException("Событие можно публиковать, только если оно в состоянии ожидания публикации" +
                            event.getStateAction());
                }
            }
            if (event.getStateAction().equals("REJECT_EVENT")) {
                if (eventToUpdate.getState().equals(State.PUBLISHED)) {
                    throw new RequestDeniedException("Событие можно отклонить, только если оно еще не опубликовано" +
                            event.getStateAction());
                }
                eventToUpdate.setState(State.CANCELED);
            }
        }
        updateEventEntity(event, eventToUpdate);

        eventRepository.save(eventToUpdate);
        return EventMapper.toPublicApiDto(eventToUpdate);
    }

    @Override
    public List<EventDto> searchEvents(List<Long> userIds, List<String> states, List<Long> categories,
                                       String rangeStart, String rangeEnd, Integer from, Integer size, HttpServletRequest
                                               request) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        if (states == null & rangeStart == null & rangeEnd == null) {
            return eventRepository.findAll().stream().map(EventMapper::toPublicApiDto).collect(Collectors.toList());
        }

        List<State> stateList = states.stream().map(State::valueOf).collect(Collectors.toList());

        LocalDateTime start;
        if (rangeStart != null && !rangeStart.isEmpty()) {
            start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        } else {
            start = LocalDateTime.now().plusYears(5);
        }

        LocalDateTime end;
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        } else {
            end = LocalDateTime.now().plusYears(5);
        }

        if (userIds.size() != 0 && states.size() != 0 && categories.size() != 0) {
            Page<EventEntity> eventsWithPage = eventRepository.findAllWithAllParameters(userIds, stateList, categories, start, end,
                    pageRequest);
            return eventsWithPage.getContent().stream().map(EventMapper::toPublicApiDto).collect(Collectors.toList());
        }
        if (userIds.size() == 0 && categories.size() != 0) {
            Page<EventEntity> eventsWithPage = eventRepository.findAllEventsWithoutIdList(categories, stateList, start, end, pageRequest);
            return eventsWithPage.getContent().stream().map(EventMapper::toPublicApiDto).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    @Override
    public EventDto createEvent(Long userId, NewEventDto event) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет " + userId));
        validateTime(event.getEventDate());
        EventEntity eventToSave = EventMapper.toEntity(event);
        eventToSave.setState(State.PENDING);
        eventToSave.setConfirmedRequests(0L);
        eventToSave.setCreatedOn(LocalDateTime.now());

        CategoryEntity category = categoryRepository.findById(event.getCategory()).orElseThrow(() -> new NotFoundException("Такой категории нет"));
        eventToSave.setCategory(category);
        eventToSave.setInitiator(user);
        EventEntity saved = eventRepository.save(eventToSave);
        return EventMapper.toFullDto(saved);
    }

    @Override
    public EventDto getEventByUserId(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя нет");
        }
        EventEntity event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Такого события нет"));
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя нет");
        }
        Page<EventEntity> eventsWithPage = eventRepository.findAllByUserWithPage(userId, PageRequest.of(from / size, size));
        List<EventEntity> events = eventsWithPage.getContent();
        return events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest event) {
        EventEntity eventFromDb = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Такого события нет " + eventId));
        if (eventFromDb.getState().equals(State.CANCELED) || eventFromDb.getState().equals(State.PENDING)) {
            if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new RequestDeniedException("Дата и время на которые намечено событие не может быть раньше, " +
                        "чем через два часа от текущего момента ");
            }
            if (event.getStateAction().equals("SEND_TO_REVIEW")) {
                eventFromDb.setState(State.PENDING);
            }
            if (event.getStateAction().equals("CANCEL_REVIEW")) {
                eventFromDb.setState(State.CANCELED);
            }
        } else {
            throw new RequestDeniedException("Изменить можно только отмененные события или события в состоянии ожидания модерации, " +
                    "статус события = " + eventFromDb.getState());
        }

        updateEventEntity(event, eventFromDb);
        eventRepository.save(eventFromDb);
        return EventMapper.toFullDto(eventFromDb);
    }

    private void validateTime(LocalDateTime start) {
        if (start.isBefore(LocalDateTime.now())) {
            throw new RequestDeniedException("Дата начала события должна быть не ранее чем за час от даты публикации");
        }
    }

    private void updateEventEntity(UpdateEventRequest event, EventEntity eventToUpdate) {
        eventToUpdate.setAnnotation(Objects.requireNonNullElse(event.getAnnotation(), eventToUpdate.getAnnotation()));
        eventToUpdate.setCategory(event.getCategory() == null
                ? eventToUpdate.getCategory()
                : categoryRepository.findById(event.getCategory()).orElseThrow(() -> new NotFoundException("Category not fount")));
        eventToUpdate.setDescription(Objects.requireNonNullElse(event.getDescription(), eventToUpdate.getDescription()));
        eventToUpdate.setEventDate(Objects.requireNonNullElse(event.getEventDate(), eventToUpdate.getEventDate()));
        eventToUpdate.setLocation(event.getLocation() == null
                ? eventToUpdate.getLocation()
                : locationRepository.findByLatAndLon(event.getLocation().getLat(), event.getLocation().getLon())
                .orElseThrow(() -> new NotFoundException("Location not found")));
        eventToUpdate.setPaid(Objects.requireNonNullElse(event.getPaid(), eventToUpdate.getPaid()));
        eventToUpdate.setParticipantLimit(Objects.requireNonNullElse(event.getParticipantLimit(), eventToUpdate.getParticipantLimit()));
        eventToUpdate.setRequestModeration(Objects.requireNonNullElse(event.getRequestModeration(), eventToUpdate.getRequestModeration()));
        eventToUpdate.setTitle(Objects.requireNonNullElse(event.getTitle(), eventToUpdate.getTitle()));
    }

}