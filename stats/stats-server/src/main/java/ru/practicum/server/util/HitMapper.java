package ru.practicum.server.util;

import ru.practicum.server.model.HitEntity;
import ru.practicum.stat.EndpointHit;

public class HitMapper {

    public static EndpointHit toDto(HitEntity hitEntity) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(endpointHit.getIp());
        endpointHit.setApp(hitEntity.getApp());
        endpointHit.setTimestamp(hitEntity.getTimestamp());
        endpointHit.setUri(hitEntity.getUri());
        return endpointHit;
    }

    public static HitEntity toEntity(EndpointHit endpointHit) {
        HitEntity hitEntity = new HitEntity();
        hitEntity.setIp(endpointHit.getIp());
        hitEntity.setApp(endpointHit.getApp());
        hitEntity.setTimestamp(endpointHit.getTimestamp());
        hitEntity.setUri(endpointHit.getUri());
        return hitEntity;
    }
}
