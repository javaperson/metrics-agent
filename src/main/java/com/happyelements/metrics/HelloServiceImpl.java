package com.happyelements.metrics;

import com.codahale.metrics.MetricRegistry;

public class HelloServiceImpl implements HelloService {
    MetricRegistry metricRegistry = new MetricRegistry();

    public String sayHello(String name) {
        return "hello " + name;
    }

    @Override
    public String mark() {
        metricRegistry.meter("test").mark();
        return null;
    }
}