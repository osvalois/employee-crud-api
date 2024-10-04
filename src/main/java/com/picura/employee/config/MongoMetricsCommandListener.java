package com.picura.employee.config;

import java.util.concurrent.TimeUnit;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

public class MongoMetricsCommandListener implements CommandListener {
    private final MeterRegistry meterRegistry;

    public MongoMetricsCommandListener(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void commandStarted(CommandStartedEvent event) {
        // Implementar si es necesario
    }

    @Override
    public void commandSucceeded(CommandSucceededEvent event) {
        Timer.builder("mongodb.command")
             .tag("command", event.getCommandName())
             .tag("status", "success")
             .register(meterRegistry)
             .record(event.getElapsedTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }

    @Override
    public void commandFailed(CommandFailedEvent event) {
        Timer.builder("mongodb.command")
             .tag("command", event.getCommandName())
             .tag("status", "failed")
             .register(meterRegistry)
             .record(event.getElapsedTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }
}
