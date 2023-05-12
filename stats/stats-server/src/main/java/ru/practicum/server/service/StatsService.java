package ru.practicum.server.service;

import ru.practicum.stat.EndpointHit;
import ru.practicum.stat.ViewStats;

import java.util.List;

public interface StatsService {

    void saveHit(EndpointHit endpointHit);

    List<ViewStats> getStatistics(String start, String end, List<String> uris, boolean unique);
}
