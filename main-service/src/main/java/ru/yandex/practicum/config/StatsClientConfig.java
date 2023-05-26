package ru.yandex.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StateClient;

@Configuration
public class StatsClientConfig {
    @Value("${stats-server.url}")
    private String url;

    @Bean
    StateClient statClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StateClient(url, builder);
    }
}

