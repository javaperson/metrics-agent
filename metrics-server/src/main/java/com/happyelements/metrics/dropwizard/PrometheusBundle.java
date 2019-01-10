package com.happyelements.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class PrometheusBundle implements Bundle {
    MetricRegistry metricRegistry;

    public PrometheusBundle setMetricRegistry(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
        return this;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    @Override
    public void run(Environment environment) {
        if (metricRegistry == null) {
            metricRegistry = environment.metrics();
        }
        environment.admin().addServlet("prometheus-metrics",
                new PrometheusServlet(metricRegistry)).addMapping("/prometheus-metrics");
    }

}
