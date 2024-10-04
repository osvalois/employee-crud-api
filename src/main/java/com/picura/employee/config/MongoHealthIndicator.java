package com.picura.employee.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;

public class MongoHealthIndicator implements ReactiveHealthIndicator {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public MongoHealthIndicator(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Health> health() {
        return reactiveMongoTemplate.executeCommand("{ ping: 1 }")
                .map(result -> Health.up().build())
                .onErrorResume(ex -> Mono.just(Health.down().build()));
    }
}