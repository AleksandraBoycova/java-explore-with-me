package ru.yandex.practicum.util;

import ru.practicum.event.EventDto;
import ru.practicum.event.NewEventDto;
import ru.yandex.practicum.model.EventEntity;

public class EventMapper {
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final UserMapper userMapper = new UserMapper();
    private final LocationMapper locationMapper = new LocationMapper();

    public EventDto toPublicApiDto(EventEntity eventEntity) {
        EventDto eventDto = new EventDto();
        eventDto.setId(eventEntity.getId());
        eventDto.setTitle(eventEntity.getTitle());
        eventDto.setAnnotation(eventEntity.getAnnotation());
        eventDto.setCategory(categoryMapper.toDto(eventEntity.getCategory()));
        eventDto.setPaid(eventEntity.getPaid());
        eventDto.setEventDate(eventEntity.getEventDate());
        eventDto.setInitiator(userMapper.toShortDto(eventEntity.getInitiator()));
        eventDto.setDescription(eventEntity.getDescription());
        eventDto.setParticipantLimit(eventEntity.getParticipantLimit());
        eventDto.setState(eventEntity.getState());
        eventDto.setCreatedOn(eventEntity.getCreatedOn());
        eventDto.setPublishedOn(eventEntity.getPublishedOn());
        eventDto.setLocation(locationMapper.toDto(eventEntity.getLocation()));
        eventDto.setRequestModeration(eventEntity.getRequestModeration());
        return eventDto;
    }

    public EventDto toFullDto(EventEntity eventEntity) {
        EventDto eventDto = new EventDto();
        eventDto.setId(eventEntity.getId());
        eventDto.setAnnotation(eventEntity.getAnnotation());
        eventDto.setCategory(categoryMapper.toDto(eventEntity.getCategory()));
        eventDto.setConfirmedRequests(eventEntity.getConfirmedRequests());
        eventDto.setCreatedOn(eventEntity.getCreatedOn());
        eventDto.setDescription(eventEntity.getDescription());
        eventDto.setInitiator(userMapper.toDto(eventEntity.getInitiator()));
        eventDto.setLocation(locationMapper.toDto(eventEntity.getLocation()));
        eventDto.setEventDate(eventEntity.getEventDate());
        eventDto.setPaid(eventEntity.getPaid());
        eventDto.setParticipantLimit(eventEntity.getParticipantLimit());
        eventDto.setPublishedOn(eventEntity.getPublishedOn());
        eventDto.setRequestModeration(eventEntity.getRequestModeration());
        eventDto.setState(eventEntity.getState());
        eventDto.setTitle(eventEntity.getTitle());
        eventDto.setViews(eventEntity.getViews());
        return eventDto;
    }

    public EventDto toShortDto(EventEntity eventEntity) {
        EventDto eventDto = new EventDto();
        eventDto.setId(eventEntity.getId());
        eventDto.setAnnotation(eventEntity.getAnnotation());
        eventDto.setCategory(categoryMapper.toDto(eventEntity.getCategory()));
        eventDto.setConfirmedRequests(eventEntity.getConfirmedRequests());
        eventDto.setEventDate(eventEntity.getEventDate());
        eventDto.setInitiator(userMapper.toShortDto(eventEntity.getInitiator()));
        eventDto.setPaid(eventEntity.getPaid());
        eventDto.setTitle(eventEntity.getTitle());
        eventDto.setViews(eventEntity.getViews());
        return eventDto;
    }

    public EventEntity toEntity(NewEventDto event) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setAnnotation(event.getAnnotation());
        eventEntity.setDescription(event.getDescription());
        eventEntity.setEventDate(event.getEventDate());
        eventEntity.setPaid(event.getPaid() != null && event.getPaid());
        eventEntity.setParticipantLimit(event.getParticipantLimit() == null ? 0 : event.getParticipantLimit());
        eventEntity.setRequestModeration(event.getRequestModeration() == null || event.getRequestModeration());
        eventEntity.setTitle(event.getTitle());
        eventEntity.setLocation(locationMapper.toEntity(event.getLocation()));
        return eventEntity;
    }
}
