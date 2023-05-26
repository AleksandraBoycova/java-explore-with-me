package ru.yandex.practicum.util;

import ru.practicum.event.LocationDto;
import ru.yandex.practicum.model.LocationEntity;

public class LocationMapper {

    public LocationDto toDto(LocationEntity entity) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLon(entity.getLon());
        locationDto.setLat(entity.getLat());
        return locationDto;
    }

    public LocationEntity toEntity(LocationDto dto) {
        LocationEntity entity = new LocationEntity();
        entity.setLon(dto.getLon());
        entity.setLat(dto.getLat());
        return entity;
    }

}
