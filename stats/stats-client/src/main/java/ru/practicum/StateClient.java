package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stat.EndpointHit;
import ru.practicum.stat.ViewStats;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.stat.Constants.dateTimeFormatter;

@Service
public class StateClient extends BaseClient {
    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<List<ViewStats>> mapType = new TypeReference<>() {
    };

    @Autowired
    public StateClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveHit(String app,
                                          String uri,
                                          String ip,
                                          LocalDateTime timestamp) {
        EndpointHit endpointHitDto = new EndpointHit(app, uri, ip, timestamp);
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStatistics(String start, String end, String uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public Long getStatisticsByEventId(Long eventId) {
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(1000).format(dateTimeFormatter),
                "end", LocalDateTime.now().plusYears(1000).format(dateTimeFormatter),
                "uris", List.of("/events/" + eventId),
                "unique", Boolean.FALSE
        );
        ResponseEntity<Object> response = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        List<ViewStats> viewStatsList = response.hasBody() ? mapper.convertValue(response.getBody(), mapType) : Collections.emptyList();
        return viewStatsList != null && !viewStatsList.isEmpty() ? viewStatsList.get(0).getHits() : 0L;
    }

    public Map<Long, Long> getSetViewsByEventId(Set<Long> eventIds) {
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(1000).format(dateTimeFormatter),
                "end", LocalDateTime.now().plusYears(1000).format(dateTimeFormatter),
                "uris", (eventIds.stream().map(id -> "/events/" + id).collect(Collectors.toList())),
                "unique", Boolean.FALSE
        );
        ResponseEntity<Object> response = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        return response.hasBody() ? mapper.convertValue(response.getBody(), mapType).stream()
                .collect(Collectors.toMap(
                        this::getEventIdFromURI, ViewStats::getHits))
                : Collections.emptyMap();
    }

    private Long getEventIdFromURI(ViewStats e) {
        return Long.parseLong(e.getUri().substring(e.getUri().lastIndexOf("/") + 1));
    }

    private String toString(List<String> strings) {
        return Arrays.toString(strings.toArray()).replace("[", "").replace("]", "");
    }

}
