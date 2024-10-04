package com.picura.employee.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.picura.employee.repository")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.max-connection-pool-size:100}")
    private int maxConnectionPoolSize;

    @Value("${spring.data.mongodb.min-connection-pool-size:0}")
    private int minConnectionPoolSize;

    @Value("${spring.data.mongodb.max-connection-idle-time:60000}")
    private int maxConnectionIdleTime;

    @Value("${spring.data.mongodb.connect-timeout:10000}")
    private int connectTimeout;

    @Value("${spring.data.mongodb.socket-timeout:0}")
    private int socketTimeout;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    @Bean
    public MongoClient reactiveMongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToConnectionPoolSettings(builder -> builder
                .maxSize(maxConnectionPoolSize)
                .minSize(minConnectionPoolSize)
                .maxConnectionIdleTime(maxConnectionIdleTime, TimeUnit.MILLISECONDS))
            .applyToClusterSettings(builder -> 
                builder.serverSelectionTimeout(5000, TimeUnit.MILLISECONDS))
            .applyToSocketSettings(builder -> 
                builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                       .readTimeout(socketTimeout, TimeUnit.MILLISECONDS))
            .build();
        return MongoClients.create(settings);
    }

    @Bean
    @Primary
    public ReactiveMongoTemplate reactiveMongoTemplate() throws Exception {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }

    @Bean
    @ConditionalOnMissingBean
    public MongoMetricsCommandListener mongoMetricsCommandListener(MeterRegistry meterRegistry) {
        return new MongoMetricsCommandListener(meterRegistry);
    }

    @Bean
    public MongoHealthIndicator mongoHealthIndicator(ReactiveMongoTemplate reactiveMongoTemplate) {
        return new MongoHealthIndicator(reactiveMongoTemplate);
    }
}