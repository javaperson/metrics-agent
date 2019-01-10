package com.happyelements.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

public class MetricsServiceImpl implements MetricsService {
    private final MetricRegistry metricRegistry;

    public MetricsServiceImpl(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }

    @Override
    public void meterMark(String name, long n) {
        metricRegistry.meter(name).mark(n);
    }

    @Override
    public void counterInc(String name) {
        metricRegistry.counter(name).inc();
    }

    @Override
    public void counterInc(String name, long vaule) {
        metricRegistry.counter(name).inc(vaule);
    }

    @Override
    public void counterDec(String name) {
        metricRegistry.counter(name).dec();
    }

    @Override
    public void counterDec(String name, long value) {
        metricRegistry.counter(name).dec(value);
    }

    @Override
    public void histogramUpdate(String name, long value) {
        metricRegistry.histogram(name).update(value);
    }
}