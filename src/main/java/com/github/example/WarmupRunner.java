package com.github.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.IntStream;

/**
 * This class is a warm-up runner.
 * It sends a request to warm up the application.
 */
@Component
public class WarmupRunner implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(WarmupRunner.class);
    private final Environment environment;

    // Using plain RestTemplate, because it is disposable.
    private final RestTemplate restTemplate = new RestTemplate();

    public WarmupRunner(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        warmupEveryEndpoints();
    }

    /**
     * Warm up every endpoints.
     * This sample sends a request to the sample endpoint.
     * In this sample, repeated requests are sent to a fixed endpoint.
     * In an actual application, please send requests to the necessary endpoints.
     */
    private void warmupEveryEndpoints() {
        logger.info("Warmup start");
        IntStream.range(0, 10).forEach(ignored -> warmup());
        logger.info("Warmup end");
    }

    private void warmup() {
        var host = "http://localhost:" + environment.getProperty("local.server.port");
        var path = "sample";
        var uri = UriComponentsBuilder
                .fromUriString(host)
                .pathSegment(path)
                .toUriString();
        var request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<Object> result;
        try {
            result = restTemplate.exchange(request, Object.class);
            if (result.getStatusCode().is2xxSuccessful()) {
                logger.info("Warmup succeeded. Status:{}, Url:{}", result.getStatusCode(), uri);
            } else {
                logger.warn("Warmup failed. Status:{}, Url:{}", result.getStatusCode(), uri);
            }
            Thread.sleep(3000);
        } catch (Exception e) {
            logger.warn("Warmup failed. host:{}, path:{}", host, path, e);
        }
    }
}
