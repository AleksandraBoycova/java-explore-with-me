package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.repository.HitRepository;
import ru.practicum.server.util.HitMapper;
import ru.practicum.stat.EndpointHit;
import ru.practicum.stat.ViewStats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.server.util.Constants.dateTimeFormatter;


@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HitRepository repository;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        repository.save(HitMapper.toEntity(endpointHit));
    }


    @Override
    public List<ViewStats> getStatistics(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            startDate = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), dateTimeFormatter);
            endDate = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), dateTimeFormatter);
        } catch (Exception e) {
            throw new ValidationException("Wrong date format");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Wrong start and end dates");
        }
        if (unique) {
            if (uris != null && !uris.isEmpty()) {
                uris.replaceAll(s -> s.replace("[", ""));
                uris.replaceAll(s -> s.replace("]", ""));
                return repository.findAllByTimestampBetweenAndUriInUnique(startDate, endDate, uris);
            } else {
                return repository.findAllByTimestampBetweenUnique(startDate, endDate);
            }
        } else {
            if (uris != null && !uris.isEmpty()) {
                uris.replaceAll(s -> s.replace("[", ""));
                uris.replaceAll(s -> s.replace("]", ""));
                return repository.findAllByTimestampBetweenAndUriIn(startDate, endDate, uris);
            } else {
                return repository.findAllByTimestampBetween(startDate, endDate);
            }
        }
    }
}

