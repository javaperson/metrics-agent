package com.happyelements.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

public class HelloServiceImpl implements HelloService {
    static MetricRegistry metricRegistry = new MetricRegistry();

    static {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(5, TimeUnit.SECONDS);
    }

    public String sayHello(String name) {
        return "hello " + name;
    }

    @Override
    public String mark() {
        metricRegistry.meter("test").mark();
        return null;
    }
}